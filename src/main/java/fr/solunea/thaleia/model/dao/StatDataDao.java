package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Publication;
import fr.solunea.thaleia.model.StatData;
import fr.solunea.thaleia.utils.CastUtils;
import fr.solunea.thaleia.utils.DateUtils;
import fr.solunea.thaleia.utils.LogUtils;
import fr.solunea.thaleia.utils.MailUtils;
import org.apache.cayenne.CayenneException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.ResultIterator;
import org.apache.cayenne.map.SQLResult;
import org.apache.cayenne.query.QueryCacheStrategy;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;


/**
 * <p>StatDataDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class StatDataDao extends CayenneDao<StatData> {

    /**
     * Constant <code>VERB_COMPLETED="completed"</code>
     */
    public static final String VERB_COMPLETED = "completed";

    // Liste des verbes
    /**
     * Constant <code>VERB_ATTEMPTED="attempted"</code>
     */
    public static final String VERB_ATTEMPTED = "attempted";
    /**
     * Constant <code>VERB_PASSED="passed"</code>
     */
    public static final String VERB_PASSED = "passed";
    /**
     * Constant <code>VERB_FAILED="failed"</code>
     */
    public static final String VERB_FAILED = "failed";
    /**
     * Constant <code>VERB_ANSWERED="answered"</code>
     */
    public static final String VERB_ANSWERED = "answered";
    /**
     * Constant <code>VERB_SUSPENDED="suspended"</code>
     */
    public static final String VERB_SUSPENDED = "suspended";
    /**
     * Constant <code>VERB_TERMINATED="terminated"</code>
     */
    public static final String VERB_TERMINATED = "terminated";
    /**
     * Constant <code>VERB_INITIALIZED="initialized"</code>
     */
    public static final String VERB_INITIALIZED = "initialized";
    /**
     * Le format du champ timestamp transmis par le conteneur SCROM2xAPI
     */
    public static final String STATDATA_TIMESTAMP_FORMAT_SHORT = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * Le format du champ timestamp transmis par la lib xAPI
     */
    public static final String STATDATA_TIMESTAMP_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    protected static final Logger logger = Logger.getLogger(StatDataDao.class.getName());
    /**
     * Le format des jours dans les timestamp des statData
     */
    private static final String STATDATA_DAY_FORMAT = "yyyy-MM-dd";
    private static PublicationsCache accessedRegistrationsCache;

    public StatDataDao(ObjectContext context) {
        super(context);
    }

    private PublicationsCache getAccessedRegistrationsCache() {
        if (null == accessedRegistrationsCache) {
            accessedRegistrationsCache = new PublicationsCache();
        }
        return accessedRegistrationsCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(StatData object, Locale locale) {
        return Integer.toString(getPK(object));
    }

    /**
     * @return le ou les noms sous lesquels a ??t?? enregistr?? cet utilisateur dans ces publications.
     */
    public List<String> getNames(List<Publication> publications, String email) {
        if (publications.isEmpty() || email.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            String sql = "SELECT concat(jsonobject::jsonb -> 'actor' -> 'account' ->> 'firstname' , ' ' , jsonobject::jsonb -> 'actor' -> 'account' ->> 'lastname') as name\n" +
                    "FROM stat_data\n" +
                    "WHERE actor_mbox = 'mailto:$user'\n" +
                    "AND publication_reference IN ($reference)\n" +
                    "AND jsonobject::jsonb -> 'actor' -> 'account' ->> 'firstname' <> ''\n" +
                    "AND jsonobject::jsonb -> 'actor' -> 'account' ->> 'lastname' <> ''\n" +
                    "GROUP BY name\n" +
                    ";";

            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            @SuppressWarnings("rawtypes") Map params = getReferencesParamForPublications("reference", publications);
            params.put("user", escapeUsername(email));
            query.setParameters(params);

            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("name");
            query.setResult(resultDescriptor);

            @SuppressWarnings("rawtypes") List result = getContext().performQuery(query);
            List<String> resultString = new ArrayList<>();
            for (Object line : result) {
                resultString.add(line.toString());
            }
            return resultString;

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return new ArrayList<>();
        }
    }

    /**
     * Pr??pare une table avec comme cl?? le param??tre $paramName et comme valeur la liste des valeurs de ces r??f??rences,
     * sous la forme "'valeur1', 'valeur2', 'valeur3'".
     *
     * @param paramName    le nom du param??tre
     * @param publications les publications
     */
    @SuppressWarnings({"rawtypes"})
    private Map getReferencesParamForPublications(String paramName, List<Publication> publications) {
        return new ReferencesParamGenerator<Publication>(paramName, publications) {
            @Override
            protected String getValue(Publication publication) {
                return publication.getReference();
            }
        }.getMap();
    }

    /**
     * <p>getAccessedRegistrations.</p>
     *
     * @param publications les publications
     * @return la liste des couples user/publicationReference pour lesquels au moins une interaction existe en base.
     */
    @SuppressWarnings({"rawtypes"})
    public List<Couple> getAccessedRegistrations(List<Publication> publications) {
        return getAccessedRegistrations(publications, 60);
    }

    /**
     * <p>getAccessedRegistrations.</p>
     *
     * @param publications les publications
     * @param start        le d??but de la p??riode
     * @param end          la fin de la p??riode
     * @return la liste des couples user/publicationReference pour lesquels au moins une interaction existe en base.
     */
    @SuppressWarnings({"rawtypes"})
    public List<Couple> getAccessedRegistrations(List<Publication> publications, Date start, Date end) {
        return getAccessedRegistrations(publications, start, end, 60);
    }

    /**
     * <p>getAccessedRegistrations.</p>
     *
     * @param publications           les publications
     * @param cacheDurationInSeconds on demande d'utiliser un requestCache pour r??pondre ?? cette requ??te.
     * @return la liste des couples user/publicationReference pour lesquels au moins une interaction existe en base.
     */
    @SuppressWarnings({"rawtypes"})
    private List<Couple> getAccessedRegistrations(List<Publication> publications, int cacheDurationInSeconds) {
        return getAccessedRegistrationsCache().getAccessedRegistrations(publications, cacheDurationInSeconds);
    }

    /**
     * <p>getAccessedRegistrations.</p>
     *
     * @param publications           les publications
     * @param start                  le d??but de la p??riode
     * @param end                    la fin de la p??riode
     * @param cacheDurationInSeconds on demande d'utiliser un requestCache pour r??pondre ?? cette requ??te.
     * @return la liste des couples user/publicationReference pour lesquels au moins une interaction existe en base.
     */
    @SuppressWarnings({"rawtypes"})
    private List<Couple> getAccessedRegistrations(List<Publication> publications, Date start, Date end,
                                                  int cacheDurationInSeconds) {
        return getAccessedRegistrationsCache().getAccessedRegistrations(publications, start, end,
                cacheDurationInSeconds);
    }

    /**
     * <p>getAccessedRegistrations.</p>
     *
     * @return la liste des couples user/publicationReference pour lesquels au moins une interaction existe en base.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Couple> getAccessedRegistrationsNoCache(Date start, Date end) {

        Date startRequest = Calendar.getInstance().getTime();

        List<Couple> result = new ArrayList<>();

        String sqlString = "SELECT DISTINCT actor_mbox u, publication_reference p" + " FROM stat_data";


        // Si on a demand?? entre 01/01/1970 et 01/01/1970, alors on ne filtre pas sur la date.
        if (start.getTime() != 0 && end.getTime() != 0) {
            sqlString = sqlString
                    + " WHERE timestamp :: TIMESTAMP >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP < '$end' :: "
                    + "TIMESTAMP";
        }

        // On fixe la valeur des r??f??rences de publications
        SQLTemplate template = new SQLTemplate(StatData.class, sqlString + ";");
        Map<String, String> param = new HashMap<>();

        // Si on a demand?? entre 01/01/1970 et 01/01/1970, alors on ne filtre pas sur la date.
        if (start.getTime() != 0 && end.getTime() != 0) {
            addStartEndParams(start, end, param);
        }

        template.setParameters(param);

        ResultIterator it = null;
        try {
            it = ((DataContext) getContext()).performIteratedQuery(template);

            while (it.hasNextRow()) {
                Map row = (Map) it.nextRow();
                String user = (String) row.get("u");
                String publication = (String) row.get("p");

                result.add(new Couple(user, publication));
            }
        } catch (Exception e) {
            logger.warn("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));

        } finally {
            try {
                assert it != null;
                it.close();
            } catch (Exception closeEx) {
                logger.warn("Impossible de fermer la connexion : " + LogUtils.getStackTrace(closeEx.getStackTrace()));
            }
        }

        Date endRequest = Calendar.getInstance().getTime();
        logger.debug("Dur??e de pr??paration de la r??ponse : " + (endRequest.getTime() - startRequest.getTime()) + " ms");
        logger.debug("Nombre d'acc??s aux publications : " + result.size());
        return result;
    }

    private void addStartEndParams(Date start, Date end, Map<String, String> param) {
        Timestamp startTimestamp = new Timestamp(start.getTime());
        param.put("start", startTimestamp.toString());
        Timestamp endTimestamp = new Timestamp(end.getTime());
        param.put("end", endTimestamp.toString());
    }

    /**
     * <p>getAccessedAccounts.</p>
     *
     * @param publications les publications
     * @return la liste des utilisateurs qui ont au moins une interaction avec une des publications.
     */
    public List<String> getAccessedAccounts(List<Publication> publications) {
        Date start = new Date(0);
        Date end = Calendar.getInstance().getTime();
        return getAccessedAccounts(publications, start, end);
    }

    /**
     * <p>getAccessedAccounts.</p>
     *
     * @param publications les publications
     * @return la liste des utilisateurs qui ont au moins une interaction avec une des publications.
     */
    public List<String> getAccessedAccounts(List<Publication> publications, Date start, Date end) {

        List<String> result = new ArrayList<>();

        if (publications.isEmpty()) {
            return result;
        }

        List<Couple> activeRegistrations = getAccessedRegistrations(publications, start, end);
        for (Couple couple : activeRegistrations) {
            result.add(couple.getLeft());
        }

        return result;

    }

    /**
     * <p>findByTimeStampAndActorMBoxAndVerb.</p>
     *
     * @param timeStamp a {@link java.lang.String} object.
     * @param actorMBox a {@link java.lang.String} object.
     * @param verb      a {@link java.lang.String} object.
     * @return a {@link fr.solunea.thaleia.model.StatData} object.
     */
    public StatData findByTimeStampAndActorMBoxAndVerb(String timeStamp, String actorMBox, String verb) {
        List<StatData> listStatData = findMatchByProperties(StatData.TIMESTAMP.getName(), timeStamp,
                StatData.ACTOR_MBOX.getName(), actorMBox, StatData.VERB_DISPLAY_ENUS.getName(), verb);
        if (listStatData.size() > 0) {
            return listStatData.get(0);
        } else {
            return null;
        }
    }

    /**
     * <p>findByReferenceAndVerbAndActorMBox.</p>
     *
     * @param reference a {@link java.lang.String} object.
     * @param verb      a {@link java.lang.String} object.
     * @param actorMBox a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List<StatData> findByReferenceAndVerbAndActorMBox(String reference, String verb, String actorMBox) {
        return findMatchByProperties(StatData.PUBLICATION_REFERENCE.getName(), reference,
                StatData.VERB_DISPLAY_ENUS.getName(), verb, StatData.ACTOR_MBOX.getName(), actorMBox);
    }

    /**
     * <p>getAllVerbs.</p>
     *
     * @param reference la r??f??rence
     * @param user      l'adresse mail de l'utilisateur concern??
     * @return tous les verbes (distincts) des interactions cette publication et cet utilisateur
     */
    @SuppressWarnings({"unused", "rawtypes"})
    public List<String> getAllVerbs(String reference, String user) {
        Date start = new Date(0);
        Date end = Calendar.getInstance().getTime();
        return getAllVerbs(reference, user, start, end);
    }

    /**
     * <p>getAllVerbs.</p>
     *
     * @param reference la r??f??rence
     * @param user      l'adresse mail de l'utilisateur concern??
     * @return tous les verbes (distincts) des interactions cette publication et cet utilisateur
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<String> getAllVerbs(String reference, String user, Date start, Date end) {

        List<String> verbs = new ArrayList<>();

        try {
            SQLTemplate query = new SQLTemplate(StatData.class, "SELECT DISTINCT verb_display_enus v FROM stat_data "
                    + "WHERE publication_reference = '$reference' AND actor_mbox = 'mailto:$user' AND timestamp :: "
                    + "TIMESTAMP >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP < '$end' :: TIMESTAMP;");

            // On active le requestCache pour ces requ??tes
            query.setCacheStrategy(QueryCacheStrategy.LOCAL_CACHE);

            Map params = new HashMap();
            params.put("reference", reference);
            params.put("user", escapeUsername(user));
            addStartEndParams(start, end, params);
            query.setParameters(params);

            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("v");
            query.setResult(resultDescriptor);

            verbs = getContext().performQuery(query);
            // logger.debug("les verbes (distincts) des interactions de la
            // publication "
            // + reference
            // + " et de l'utilisateur "
            // + user
            // + " : "
            // + verbs);

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
        }

        // On s'assure qu'aucun verbe n'est null.
        List<String> result = new ArrayList<>();
        for (String verb : verbs) {
            if (verb != null) {
                result.add(verb);
            }
        }

        return result;
    }

    private String escapeUsername(String string) {
        if (string == null) {
            return null;
        }

        if (MailUtils.isValidEmailAddress(string)) {
            return string;
        } else {
            // Si "email" contient autre chose d'une adresse email, on ??chappe les '
            // Car ils ne seront pas ??chapp??s lors de la construction des requ??tes SQL.
            return StringUtils.replace(string, "'", "''");
        }
    }

    /**
     * <p>getStatData.</p>
     *
     * @param day                 on ne renvoie que les donn??es de ce jour
     * @param publications        on ne consulte que ces publications
     * @param verb                VERB_?
     * @param authorityMboxIsNull si true, alors on filtre pour ne compter que les occurrences pour lesquelles
     *                            authority_mbox est null ou vide.
     * @return les statistiques qui r??pondent ?? ces crit??res
     */
    @SuppressWarnings({"unused"})
    public List<StatData> countStatData(Calendar day, List<Publication> publications, String verb,
                                        boolean authorityMboxIsNull) {
        String sql = "SELECT count(*) FROM stat_data WHERE stat_data.verb_display_enus = '$verb' AND timestamp :: DATE"
                + " = " + "'%$date%' :: DATE AND publication_reference IN ($reference)";
        return executeStatDataRequest(day, publications, verb, authorityMboxIsNull, sql);
    }

    private List<StatData> executeStatDataRequest(Calendar day, List<Publication> publications, String verb,
                                                  boolean authorityMboxIsNull, String sql) {
        List<StatData> result;
        result = new ArrayList<>();

        if (day == null || verb == null || publications == null || publications.size() == 0) {
            return result;
        }

        DateFormat dateFormater = new SimpleDateFormat(StatDataDao.STATDATA_DAY_FORMAT);

        if (authorityMboxIsNull) {
            sql = sql + " AND (authority_mbox IS NULL OR authority_mbox = '');";
        } else {
            sql = sql + " AND authority_mbox <> '' AND authority_mbox IS NOT NULL;";
        }

        result = getStatData(day, publications, verb, result, dateFormater, sql);
        return result;

    }

    @SuppressWarnings({"unchecked"})
    private List<StatData> getStatData(Calendar day, List<Publication> publications, String verb,
                                       List<StatData> result, DateFormat dateFormater, String sql) {
        try {
            SQLTemplate query = new SQLTemplate(StatData.class, sql);
            @SuppressWarnings("rawtypes") Map<String, String> params = getReferencesParamForPublications("reference",
                    publications);
            params.put("verb", verb);
            params.put("date", dateFormater.format(day.getTime()));
            query.setParameters(params);

            result = CastUtils.castList(getContext().performQuery(query), StatData.class);
        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la requ??te SQL : " + LogUtils.getStackTrace(e.getStackTrace()));
        }
        return result;
    }

    /**
     * <p>getStatData.</p>
     *
     * @param day                 on ne renvoie que les donn??es de ce jour
     * @param publications        on ne consulte que ces publications
     * @param verb                VERB_?
     * @param authorityMboxIsNull si true, alors on filtre pour ne compter que les occurrences pour lesquelles
     *                            authority_mbox est null ou vide.
     * @return les statistiques qui r??pondent ?? ces crit??res
     */
    public List<StatData> getStatData(Calendar day, List<Publication> publications, String verb,
                                      boolean authorityMboxIsNull) {
        String sql = "SELECT * FROM stat_data WHERE stat_data.verb_display_enus = '$verb' AND timestamp :: DATE = "
                + "'%$date%' :: DATE AND publication_reference IN ($reference)";
        return executeStatDataRequest(day, publications, verb, authorityMboxIsNull, sql);
    }

    /**
     * La somme des temps pass?? pour toutes les interactions suspended, succeeded, failed, completed dans ces
     * publications.
     *
     * @param publications les publications
     * @param email        si null, alors pas de conditions sur l'email.
     * @return la somme du temps pass?? par cet utilisateur dans toutes les publications.
     */
    @SuppressWarnings({"unused"})
    public Integer getSpentTimeInSeconds(List<Publication> publications, String email) {
        Date start = new Date(0);
        Date end = Calendar.getInstance().getTime();
        return getSpentTimeInSeconds(publications, email, start, end);
    }

    /**
     * La somme des temps pass?? pour toutes les sessions dans ces publications.
     *
     * @param publications les publications
     * @param email        si null, alors pas de conditions sur l'email.
     * @return la somme du temps pass?? par cet utilisateur dans toutes les publications.
     */
    @SuppressWarnings("unchecked")
    public Integer getSpentTimeInSeconds(List<Publication> publications, String email, Date start, Date end) {

        if (publications.isEmpty()) {
            return 0;
        }

        String sql = "SELECT SUM(r) FROM (SELECT MAX(EXTRACT(seconds from result_duration::INTERVAL) + EXTRACT"
                + "(minutes from result_duration::INTERVAL) *\n"
                + "                                                                   60 + EXTRACT(hours from "
                + "result_duration::INTERVAL) *\n"
                + "                                                                        3600\n"
                + "               )                             r,\n"
                + "       context_contextactivities_other                      c\n" + "FROM stat_data\n"
                + "WHERE publication_reference IN ($reference)\n"
                + "  AND timestamp :: TIMESTAMP >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP < '$end' :: "
                + "TIMESTAMP\n";
        if (email != null) {
            sql = sql + "AND actor_mbox = 'mailto:$user' ";
        }
        sql = sql + " GROUP BY c) as durations;";
        try {
            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            @SuppressWarnings("rawtypes") Map params = getReferencesParamForPublications("reference", publications);
            if (email != null) {
                params.put("user", escapeUsername(email));
            }
            addStartEndParams(start, end, params);

            query.setParameters(params);

            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("sum");
            query.setResult(resultDescriptor);

            List response = getContext().performQuery(query);

            Object result = response.get(0);
            if (result != null) {
                String publicationsList = "";
                String emailToString = "";
                for (Publication publication : publications) {
                    publicationsList = publicationsList + publication.getReference() + " ";
                }
                if (email != null) {
                    emailToString = "et l'email " + email;
                }
                logger.debug("Temps pass?? pour les publications " + publicationsList + emailToString + " = " + result);
                String resultAsString = result.toString();
                if (resultAsString.contains(".")) {
                    resultAsString = resultAsString.substring(0, resultAsString.indexOf("."));
                }
                return Integer.parseInt(resultAsString);
            } else {
                return 0;
            }

        } catch (Exception e) {
            logger.debug("Erreur pour la requ??te " + sql);
            logger.debug("Publications : ");
            for (Publication publication : publications) {
                logger.debug(publication.getObjectId());
            }
            logger.debug("Email : " + email);
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
    }

    /**
     * Contenus SCORM : somme du nombre d'initialized. Contenus non SCORM : somme du nombre d'attempted. Somme de ces
     * sommes.
     *
     * @param publications peut ??tre vide, mais pas null
     * @param email        si null, alors on ne filtre pas sur l'email.
     * @return a {@link java.lang.Integer} object.
     */
    @SuppressWarnings("unused")
    public Integer countAttempts(List<Publication> publications, String email) {
        return countAttempts(publications, email, new Date(0), Calendar.getInstance().getTime());
    }

    /**
     * Contenus SCORM : somme du nombre d'initialized. Contenus non SCORM : somme du nombre d'attempted. Somme de ces
     * sommes.
     *
     * @param publications peut ??tre vide, mais pas null
     * @param email        si null, alors on ne filtre pas sur l'email.
     * @return a {@link java.lang.Integer} object.
     */
    public Integer countAttempts(List<Publication> publications, String email, Date start, Date end) {
        int result = 0;

        if (publications.isEmpty()) {
            return result;
        }

        // Recherche de la somme du nombre d'initialized pour les contenus
        // SCORM. Les contenus SCORM ont une authority_mbox non nulle.
        result = countInPublications(publications, email, StatDataDao.VERB_INITIALIZED, false, start, end);
        // logger.debug("Publications pour le verbe initialized : " + result);

        // Recherche du nombre d'attempted dans les contenus non SCORM : les
        // contenus non SCORM ont une authority_mbox nulle.
        result = result + countInPublications(publications, email, StatDataDao.VERB_ATTEMPTED, true, start, end);
        // logger.debug("Publications pour le verbe initialized ET attempted : "
        // + result);

        return result;
    }

    /**
     * Compte le nombre d'occurences qui correspondent ?? ces crit??res.
     *
     * @param publications        les publications
     * @param email               si null, on ne filtre pas sur l'email
     * @param authorityMboxIsNull si true, alors on filtre pour ne compter que les occurrences pour lesquelles
     *                            authority_mbox est null ou vide.
     */
    @SuppressWarnings("unchecked")
    private int countInPublications(List<Publication> publications, String email, String verb,
                                    boolean authorityMboxIsNull, Date start, Date end) {

        if (publications.isEmpty()) {
            return 0;
        }

        try {
            String sql = "SELECT count(*) c FROM stat_data WHERE publication_reference IN ($reference) " + "AND "
                    + "verb_display_enus = '" + verb + "' ";

            if (email != null) {
                sql = sql + " AND actor_mbox = 'mailto:$user' ";
            }

            if (authorityMboxIsNull) {
                sql = sql + " AND (authority_mbox IS NULL OR authority_mbox = '')";
            } else {
                sql = sql + " AND authority_mbox <> '' AND authority_mbox IS NOT NULL";
            }

            sql = sql + " AND timestamp :: TIMESTAMP >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP < '$end' :: "
                    + "TIMESTAMP;";
            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            @SuppressWarnings("rawtypes") Map params = getReferencesParamForPublications("reference", publications);
            if (email != null) {
                params.put("user", escapeUsername(email));
            }
            addStartEndParams(start, end, params);
            query.setParameters(params);

            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("c");
            query.setResult(resultDescriptor);

            @SuppressWarnings("rawtypes") List result = getContext().performQuery(query);
            // logger.debug("Nombre de tentatives pour les publications "
            // + params.get("reference") + " = " + result);
            String count = result.get(0).toString();
            return new Integer(count);

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
    }

    /**
     * <p>getBestScoreScaled.</p>
     *
     * @param email       l'email
     * @param publication la publication
     * @return la meilleure note obtenue par cet utilisateur ?? cette publication, ou null si aucune note n'a ??t??
     * trouv??e.
     */
    @SuppressWarnings({"rawtypes", "unused"})
    public Double getBestScoreScaled(String email, Publication publication) {
        Date start = new Date(0);
        Date end = Calendar.getInstance().getTime();
        return getBestScoreScaled(email, publication, start, end);
    }

    /**
     * <p>getBestScoreScaled.</p>
     *
     * @param email       l'email
     * @param publication la publication
     * @return la meilleure note obtenue par cet utilisateur ?? cette publication, ou null si aucune note n'a ??t??
     * trouv??e.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Double getBestScoreScaled(String email, Publication publication, Date start, Date end) {
        try {
            String sql = "SELECT result_score_scaled r FROM stat_data WHERE publication_reference = '$reference' "
                    + "AND actor_mbox = 'mailto:$user' AND result_score_scaled IS NOT NULL AND timestamp :: TIMESTAMP"
                    + " >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP < '$end' :: TIMESTAMP";
            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            Map params = new HashMap();
            params.put("user", escapeUsername(email));
            params.put("reference", publication.getReference());
            addStartEndParams(start, end, params);
            query.setParameters(params);

            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("r");
            query.setResult(resultDescriptor);

            List result = getContext().performQuery(query);
            Double bestScore = null;
            for (Object aResult : result) {
                try {
                    Float score = (Float) aResult;
                    // logger.debug("Score='" + score + "'");
                    Double scoreAsDouble = Double.valueOf(score);
                    if (bestScore == null || scoreAsDouble > bestScore) {
                        bestScore = scoreAsDouble;
                    }
                } catch (Exception e) {
                    // ignor??
                    logger.debug("Score ignor?? : " + e);
                }
            }
            // logger.debug("bestScore=" + bestScore);
            return bestScore;

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @param email        l'email
     * @param publications les publications
     * @return la plus ancienne statData concernant cet email et l'une de ces publications, ou null si aucune StatData
     * n'existe pour cet email et ces publications.
     */
    @SuppressWarnings({"rawtypes"})
    private StatData getStatData(String email, List<Publication> publications, boolean first) {
        Date start = new Date(0);
        Date end = Calendar.getInstance().getTime();
        return getStatData(email, publications, first, start, end);
    }

    /**
     * @param email        l'email
     * @param publications les publications
     * @return la plus ancienne statData concernant cet email et l'une de ces publications, ou null si aucune StatData
     * n'existe pour cet email et ces publications.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private StatData getStatData(String email, List<Publication> publications, boolean first, Date start, Date end) {

        if (publications.isEmpty()) {
            return null;
        }

        try {
            String sql = "SELECT * FROM stat_data WHERE publication_reference IN ($reference) AND actor_mbox = "
                    + "'mailto:$user' AND timestamp :: TIMESTAMP >= '$start' :: TIMESTAMP AND timestamp :: TIMESTAMP "
                    + "< '$end' :: TIMESTAMP ORDER BY received ASC;";

            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            Map params = getReferencesParamForPublications("reference", publications);
            params.put("user", escapeUsername(email));
            addStartEndParams(start, end, params);
            query.setParameters(params);

            List<StatData> result = CastUtils.castList(getContext().performQuery(query), StatData.class);

            if (result.size() > 0) {
                if (first) {
                    // Le plus vieux enregistrement est le premier de la liste
                    return result.get(0);
                } else {
                    // Le plus jeune enregistrement est le dernier de la liste
                    return result.get(result.size() - 1);
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * <p>getFirstStatData.</p>
     *
     * @param email        l'email
     * @param publications les publications
     * @return la plus ancienne statData concernant cet email et l'une de ces publications, ou null si aucune StatData
     * n'existe pour cet email et ces publications.
     */
    public StatData getFirstStatData(String email, List<Publication> publications) {
        return getStatData(email, publications, true);
    }

    /**
     * <p>getFirstStatData.</p>
     *
     * @param email        l'email
     * @param publications les publications
     * @return la plus ancienne statData concernant cet email et l'une de ces publications, ou null si aucune StatData
     * n'existe pour cet email et ces publications.
     */
    public StatData getFirstStatData(String email, List<Publication> publications, Date start, Date end) {
        return getStatData(email, publications, true, start, end);
    }

    /**
     * <p>getLastStatData.</p>
     *
     * @param email        l'email
     * @param publications les publications
     * @return la plus r??cente statData concernant cet email et l'une de ces publications, ou null si aucune StatData
     * n'existe pour cet email et ces publications.
     */
    public StatData getLastStatData(String email, List<Publication> publications) {
        return getStatData(email, publications, false);
    }

    /**
     * <p>findAfter.</p>
     *
     * @param from         la date de d??but
     * @param publications les publications
     * @return toutes les donn??es enregistr??es apr??s la date pass??e en param??tre, de la plus vieille ?? la plus r??cente.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<StatData> findAfter(List<Publication> publications, Date from) {
        try {
            String sql = "SELECT * FROM stat_data WHERE publication_reference IN ($reference)  AND received "
                    + "::TIMESTAMP > '$date' :: TIMESTAMP ORDER BY received ASC ;";

            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            Map params = getReferencesParamForPublications("reference", publications);
            // On formatte la date pour obtenir '2015-09-04 17:00:00'
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            params.put("date", format.format(from.getTime()));
            query.setParameters(params);

            return CastUtils.castList(getContext().performQuery(query), StatData.class);

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * <p>findWithDuration.</p>
     *
     * @param publications les publications
     * @param user         l'utilisateur
     * @return toutes les donn??es pour ces publications et cet utilisateur, pour lesquelles une result_duration a ??t??
     * d??finie.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<StatData> findWithDuration(List<Publication> publications, String user) {
        if (publications.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            String sql = "SELECT * FROM stat_data WHERE publication_reference IN ($reference) " + "AND actor_mbox = "
                    + "'mailto:$user' AND result_duration <> '';";

            SQLTemplate query = new SQLTemplate(StatData.class, sql);

            // On fixe la valeur des param??tres dans la requ??te
            Map params = getReferencesParamForPublications("reference", publications);
            params.put("user", escapeUsername(user));
            query.setParameters(params);

            return CastUtils.castList(getContext().performQuery(query), StatData.class);

        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @param user
     * @return Liste des statements de publications
     * @brief R??cup??re la liste des statements de publications en DB
     * @detail La requ??te n'??tant pas li??e aux objets DAO il est n??cessaire de passer par "QueryRunner"
     */
    public List<List<String>> findPublicationsStatements(String user) {
        try {
            String sql = "SELECT " +
                    "       P.reference                                                                 AS publication_id,\n" +
                    "       P.name                                                                      AS publication_name,\n" +
                    "       SD.actor_name                                                               AS actor_name,\n" +
                    "       jsonobject::JSON -> 'object' ->> 'id'                                       AS object_id,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US'   AS object_description,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' ->> 'interactionType'          AS object_interactionType,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' ->> 'correctResponsesPattern'  AS object_correctResponsesPattern,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' ->> 'learnerResponse'          AS object_learnerResponse,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' ->> 'latency'                  AS object_latency,\n" +
                    "       jsonobject::JSON -> 'object' -> 'definition' ->> 'result'                   AS object_result,\n" +
                    "       SD.verb_display_enus                                                        AS verb_display,\n" +
                    "       SD.timestamp                                                                AS result_timestamp,\n" +
                    "       SD.result_duration                                                          AS result_duration,\n" +
                    "       SD.result_success                                                           AS result_success,\n" +
                    "       SD.result_completion                                                        AS result_completion,\n" +
                    "       SD.result_score_min                                                         AS score_min,\n" +
                    "       SD.result_score_max                                                         AS score_max,\n" +
                    "       SD.result_score_raw                                                         AS score_raw,\n" +
                    "       SD.result_score_scaled                                                      AS score_scaled,\n" +
                    "       jsonobject::JSON -> 'result' ->> 'success'                                  AS score_success\n" +
                    "FROM stat_data AS SD\n" +
                    "         left JOIN publication AS P ON (SD.publication_reference = P.reference)\n" +
                    "         left JOIN user_account AS UA ON (P.user_id = UA.id)\n" +
                    "WHERE UA.login = '" + escapeUsername(user) + "'\n" +
                    "ORDER BY publication_id ASC, SD.actor_name ASC;";

            // Obtention de la connexion
            Connection connection = null;
            try {
                Context initialContext = new InitialContext();
                Context envContext = (Context) initialContext.lookup("java:/comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/database");
                connection = ds.getConnection();
            } catch (Exception e) {
                logger.warn(LogUtils.getStackTrace(e.getStackTrace()));
                throw e;
            }

            // Ex??cution de la requ??te
            try {
                ResultSetHandler<List<List<String>>> resultSetHandler = rs -> {
                    if (!rs.next()) {
                        return null;
                    }

                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();
                    List<List<String>> result = new ArrayList<>();

                    // premi??re ligne : les noms des colonnes
                    {
                        List<String> line = new ArrayList<>();
                        for (int i = 0; i < cols; i++) {
                            //                            line.add(String.valueOf(rs.getObject(i + 1)));
                            line.add(meta.getColumnLabel(i + 1));
                        }
                        result.add(line);
                    }

                    // les lignes de donn??es
                    {
                        // On a d??j?? appel?? rs.next, donc on a une ligne d??j?? charg??e.
                        addRSCurrentRowToResult(rs, cols, result);
                        // On parcourt toutes les lignes suivantes
                        while (rs.next()) {
                            addRSCurrentRowToResult(rs, cols, result);
                        }
                    }

                    return result;
                };

                QueryRunner run = new QueryRunner();
                List<List<String>> result = run.query(connection, sql, resultSetHandler);

                return result;

            } finally {
                // Use this helper method so we don't have to check for null
                DbUtils.close(connection);
            }
        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te de r??cup??ration des interactions de la publication : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @param user
     * @return Liste des r??sultats de r??ponse aux questions des publications.
     * @brief Retourne la liste des r??sultats de r??ponse aux questions des publications.
     * @detail La requ??te n'??tant pas li??e aux objets DAO il est n??cessaire de passer par "QueryRunner"
     */
    public List<List<String>> findPublicationsQuestionsAnswers(String user, String reference) {
        try {
            String sql = "SELECT\n" +
                    "    publication_id,\n" +
                    "    publication_name,\n" +
                    "    object_description,\n" +
                    "    interaction_type,\n" +
                    "    correct,\n" +
                    "    incorrect,\n" +
                    "    hours,\n" +
                    "    minutes,\n" +
                    "    secs,\n" +
                    "    (\n" +
                    "        CASE WHEN hours IS NOT NULL     THEN hours * 3600 ELSE 0 END\n" +
                    "        + CASE WHEN minutes IS NOT NULL THEN minutes * 60   ELSE 0 END\n" +
                    "        + CASE WHEN secs IS NOT NULL    THEN secs         ELSE 0 END\n" +
                    "    ) as duration,\n" +
                    "    CASE WHEN ((correct + incorrect) IS NOT NULL AND (correct + incorrect) <> 0 )THEN ((\n"
                    + "               CASE WHEN hours IS NOT NULL THEN hours * 3600 ELSE 0 END\n"
                    + "               + CASE WHEN minutes IS NOT NULL THEN minutes * 60 ELSE 0 END\n"
                    + "               + CASE WHEN secs IS NOT NULL THEN secs ELSE 0 END\n"
                    + "           ) / (correct + incorrect)) END as average_duration,\n" +
                    "    learner_responses,\n" +
                    "    choices,\n" +
                    "    anchors\n" +
                    "FROM\n" +
                    "    (\n" +
                    "        SELECT\n" +
                    "            T.publication_id,\n" +
                    "            T.publication_name,\n" +
                    "            T.interaction_type,\n" +
                    "            T.object_description,\n" +
                    "            SUM (CASE WHEN T.result LIKE 'correct' THEN 1 ELSE 0 END)      AS correct,\n" +
                    "            SUM (CASE WHEN T.result LIKE 'incorrect' THEN 1 ELSE 0 END)    AS incorrect,\n" +
                    "            SUM(CAST(hours AS BIGINT)) as hours,\n" +
                    "            SUM(CAST(minutes AS BIGINT)) as minutes,\n" +
                    "            SUM(CAST(secs AS BIGINT)) as secs,\n" +
                    "            array_to_string(array_agg(learner_response), ' _;_ ') AS learner_responses,\n" +
                    "            T.choices,\n" +
                    "            T.anchors\n" +
                    "        FROM (\n" +
                    "                 SELECT\n" +
                    "                     P.reference                                                                   AS publication_id,\n" +
                    "                     P.name                                                                        AS publication_name,\n" +
                    "                     jsonobject::JSON -> 'object' -> 'definition' ->> 'interactionType'            AS interaction_type,\n" +
                    "                     jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US'     AS object_description,\n" +
                    "                     jsonobject::JSON -> 'object' -> 'definition' ->>  'result'                    AS result,\n" +
                    "                     SUBSTRING(\n" +
                    "                         jsonobject::JSON -> 'object' -> 'definition' ->> 'latency',\n" +
                    "                         '([0-9]{1,2})[H][0-9]{1,2}[M][0-9]{1,2}[S]'\n" +
                    "                     )                                                                             AS hours,\n" +
                    "                     SUBSTRING(\n" +
                    "                         jsonobject::JSON -> 'object' -> 'definition' ->> 'latency',\n" +
                    "                         '([0-9]{1,2})[M][0-9]{1,2}[S]'\n" +
                    "                     )                                                                             AS minutes,\n" +
                    "                     SUBSTRING(\n" +
                    "                         jsonobject::JSON -> 'object' -> 'definition' ->> 'latency',\n" +
                    "                         '([0-9]{1,2})[S]'\n" +
                    "                     )                                                                             AS secs,\n" +
                    "                     REPLACE(\n" +
                    "                         jsonobject::JSON -> 'object' -> 'definition' ->> 'learnerResponse',\n" +
                    "                         '{case_matters:false}',\n" +
                    "                         ''\n" +
                    "                     )                                                                             AS learner_response,\n" +
                    "                     jsonobject::JSON -> 'object' -> 'definition' ->> 'choices'                    AS choices,\n" +
                    "                     jsonobject::JSON -> 'object' -> 'definition' ->> 'target'                   AS anchors" +
                    "                 FROM\n" +
                    "                     stat_data AS SD\n" +
                    "                         left JOIN publication AS P ON (SD.publication_reference = P.reference)\n" +
                    "                         left JOIN user_account AS UA ON (P.user_id = UA.id)\n" +
                    "                 WHERE\n" +
                    "                     UA.login = '" + escapeUsername(user) + "'\n" +
                    "                     AND jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US' is not null\n" +
                    "                     AND P.reference = '" + reference + "'\n" +
                    "             ) AS T\n" +
                    "        GROUP BY\n" +
                    "            T.publication_id,\n" +
                    "            T.interaction_type,\n" +
                    "            T.publication_name,\n" +
                    "            T.object_description,\n" +
                    "            T.choices,\n" +
                    "            T.anchors\n" +
                    "    ) as T2;\n";


//            String sql = "SELECT\n" +
//                    "       P.reference                                                                 AS publication_id,\n" +
//                    "       P.name                                                                       AS publication_name,\n" +
//                    "       jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US'    AS object_description,\n" +
//                    "       SUM (CASE WHEN jsonobject::JSON -> 'object' -> 'definition' ->> 'result' LIKE 'correct' THEN 1 ELSE 0 END) AS correct,\n" +
//                    "       SUM (CASE WHEN jsonobject::JSON -> 'object' -> 'definition' ->> 'result' LIKE 'incorrect' THEN 1 ELSE 0 END) AS incorrect\n" +
//                    "FROM\n" +
//                    "       stat_data AS SD\n" +
//                    "       left JOIN publication AS P ON (SD.publication_reference = P.reference)\n" +
//                    "       left JOIN user_account AS UA ON (P.user_id = UA.id)\n" +
//                    "WHERE\n" +
//                    "       UA.login = '" + escapeUsername(user) + "'\n" +
//                    "       AND jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US' is not null\n" +
//                    "       AND P.reference = '" + reference + "'\n" +
//                    "GROUP BY\n" +
//                    "       P.reference,\n" +
//                    "       P.name,\n" +
//                    "       jsonobject::JSON -> 'object' -> 'definition' -> 'description' ->> 'en-US';";

            // Obtention de la connexion
            Connection connection = null;
            try {
                Context initialContext = new InitialContext();
                Context envContext = (Context) initialContext.lookup("java:/comp/env");
                DataSource ds = (DataSource) envContext.lookup("jdbc/database");
                connection = ds.getConnection();
            } catch (Exception e) {
                logger.warn(LogUtils.getStackTrace(e.getStackTrace()));
                throw e;
            }

            // Ex??cution de la requ??te
            try {
                ResultSetHandler<List<List<String>>> resultSetHandler = rs -> {
                    if (!rs.next()) {
                        return null;
                    }

                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();
                    List<List<String>> result = new ArrayList<>();

                    // premi??re ligne : les noms des colonnes
                    {
                        List<String> line = new ArrayList<>();
                        for (int i = 0; i < cols; i++) {
                            line.add(meta.getColumnLabel(i + 1));
                        }
                        result.add(line);
                    }

                    // les lignes de donn??es
                    {
                        // On a d??j?? appel?? rs.next, donc on a une ligne d??j?? charg??e.
                        addRSCurrentRowToResult(rs, cols, result);
                        // On parcourt toutes les lignes suivantes
                        while (rs.next()) {
                            addRSCurrentRowToResult(rs, cols, result);
                        }
                    }

                    return result;

                };

                QueryRunner run = new QueryRunner();
                List<List<String>> result = run.query(connection, sql, resultSetHandler);

                return result;

            } finally {
                // Use this helper method so we don't have to check for null
                DbUtils.close(connection);
            }
        } catch (Exception e) {
            logger.info("Impossible d'ex??cuter la requ??te de r??cup??ration des interactions de la publication : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    private void addRSCurrentRowToResult(ResultSet rs, int cols, List<List<String>> result) throws SQLException {
        List<String> line = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            line.add(String.valueOf(rs.getObject(i + 1)));
        }
        result.add(line);
    }

    /**
     * <p>countIdentifications.</p>
     *
     * @param usernames uniquement pour ces usernames. Si vide, alors la somme concernera tous les usernames.
     * @param success   si true, on compte les identifications qui ont r??ussi
     * @param start     la date de d??but
     * @param end       la date de fin
     * @return le nombre d'identifications ?? l'application Thaleia.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public int countIdentifications(List<String> usernames, boolean success, Date start, Date end) {

        SQLTemplate query = new SQLTemplate(StatData.class, "");
        Map params;

        try {
            String actorCondition;
            // Le crit??re de filtre sur les utilisateurs, si demand??.
            if (!usernames.isEmpty()) {
                actorCondition = "WHERE actor_mbox IN ($usernames) AND ";
            } else {
                actorCondition = "WHERE";
            }

            String sql = "SELECT count(*) c FROM stat_data " + actorCondition + " verb_display_enus = "
                    + "'logged-in' AND result_success =  " + success
                    + " AND received :: TIMESTAMP > '$start' :: TIMESTAMP AND " + "received :: TIMESTAMP < "
                    + "'$end' :: TIMESTAMP";

            query = new SQLTemplate(StatData.class, sql);
            params = new HashMap();

            // Si crit??re de filtre sur les utilisateurs, alors il faut peupler
            // la liste.
            if (!usernames.isEmpty()) {
                // On fixe la valeur du param??tres $usernames dans la requ??te
                params.putAll(new ReferencesParamGenerator<String>("usernames", usernames) {
                    @Override
                    protected String getValue(String username) {
                        return "mailto:" + escapeUsername(username);
                    }
                }.getMap());
            }

            // Formattage des dates
            // On formatte la date pour obtenir '2015-09-04 17:00:00'
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            params.put("start", format.format(start.getTime()));
            params.put("end", format.format(end.getTime()));

            query.setParameters(params);
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("c");
            query.setResult(resultDescriptor);
        } catch (Exception e) {
            logger.debug("Requ??te = " + query);
            logger.warn("Impossible de pr??parer la requ??te : " + e + "\n" + LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
        try {
            List result = getContext().performQuery(query);
            logger.debug("Nombre d'identifications pour usernames=" + params.get("usernames") + " du "
                    + DateUtils.formatDateHour(start, Locale.FRENCH) + " au "
                    + DateUtils.formatDateHour(end, Locale.FRENCH) + " = " + result);
            String count = result.get(0).toString();
            return new Integer(count);

        } catch (Exception e) {
            logger.debug("Requ??te = " + query);
            logger.warn("Impossible d'ex??cuter la requ??te : " + e + "\n" + LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
    }

    private boolean contains(List<Publication> publications, Publication publication) {
        if (publication == null || publication.getReference() == null) {
            return false;
        }
        for (Publication candidate : publications) {
            if (candidate != null && candidate.getReference() != null
                    && candidate.getReference().equals(publication.getReference())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Un couple de cha??ne de caract??res, avec un membre gauche et un membre droit.
     */
    public static class Couple implements Serializable {
        private String left;
        private String right;

        Couple(String left, String right) {
            this.left = left;
            this.right = right;
            // logger.debug("left=" + left + " right=" + right);
        }

        public String getLeft() {
            return left;
        }

        public void setLeft(String left) {
            this.left = left;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }
    }

    /**
     * Pr??pare une table avec comme cl?? le param??tre $paramName et comme valeur la liste des valeurs de ces r??f??rences,
     * sous la forme "'valeur1', 'valeur2', 'valeur3'".
     * <p>
     * En fonction de l'objet T, la mani??re de r??cup??rer la valeur est ?? impl??menter dans getValue.
     */
    public static abstract class ReferencesParamGenerator<T> {

        private final String paramName;
        private final List<T> objects;

        public ReferencesParamGenerator(String paramName, List<T> objects) {
            this.paramName = paramName;
            this.objects = objects;
        }

        /**
         * @param object l'objet
         * @return la valeur qui correspond ?? cet objet, et qui est ?? affecter ?? la r??f??rence.
         */
        protected abstract String getValue(T object);

        @SuppressWarnings({"rawtypes", "unchecked"})
        public Map getMap() {
            // On r??cup??re les valeurs des r??f??rences
            List<String> values = new ArrayList<>();
            for (T object : objects) {
                values.add(getValue(object));
            }

            // On veut fabriquer un param??tre de la forme :
            // paramName = 'valeur1', 'valeur2', 'valeur3'
            StringBuilder valuesString = new StringBuilder();
            for (String value : values) {
                valuesString.append("'").append(value).append("', ");
            }
            // On supprime le dernier ", ".
            if (valuesString.toString().endsWith(", ")) {
                valuesString = new StringBuilder(valuesString.substring(0, valuesString.length() - ", ".length()));
            }

            Map param = new HashMap();
            param.put(paramName, valuesString.toString());
            return param;
        }

    }

    private class PublicationsCache implements Serializable {

        // Initialisation du requestCache des requ??tes / couples
        Map<Request, List<Couple>> requestCache = new HashMap<>();
        // Initialisation du requestCache de l'ensemble des AccessedRegistration : distinct (user -> publication)
        Map<Period, List<Couple>> userAccessToPublicationCache = new HashMap<>();
        // Date de derni??re requ??te ?? ce Publication Cache
        Date lastRequestDate = new Date(0);

        List<Couple> getAccessedRegistrations(List<Publication> publications, long cacheDurationInSeconds) {
            Date epoch = new Date(0);
            return getAccessedRegistrations(publications, epoch, epoch, cacheDurationInSeconds);
        }

        List<Couple> getAccessedRegistrations(List<Publication> publications, Date start, Date end,
                                              long cacheDurationInSeconds) {

            Request incomingRequest = new Request(publications, start, end);

            // Remise ?? 0 du requestCache si besoin
            if (Calendar.getInstance().getTime().getTime() - lastRequestDate.getTime()
                    > cacheDurationInSeconds * 1000) {
                logger.debug("Remise ?? z??ro du cache.");
                requestCache = new HashMap<>();
                userAccessToPublicationCache = new HashMap<>();
            }
            lastRequestDate = Calendar.getInstance().getTime();

            // On compare la requ??te re??ue avec la liste des requ??tes en requestCache
            for (Request requestsInCache : requestCache.keySet()) {
                // Est-ce les m??mes dates de d??but et fin ?
                // Cette liste de publication du requestCache est-elle ??gale ?? celle de la requ??te pass??e en
                // param??tres ?
                if (requestsInCache.equals(incomingRequest)) {
                    // Oui : on renvoie les couples correspondants du requestCache
                    logger.debug("Utilisation du requestCache pour renvoyer la liste des publications. Requ??te : "
                            + incomingRequest);
                    return requestCache.get(requestsInCache);
                } else {
                    logger.debug(
                            "Pas d'utilisation du requestCache pour renvoyer la liste des publications. Requ??te" + " : "
                                    + incomingRequest);
                }
            }
            //logger.debug("Fabrication de la liste de publications depuis la base de donn??es");

            // On fabrique la r??ponse : on commence par r??colter tous les acc??s aux publications
            // On cherche dans le cache les donn??es de la requ??tes = les acc??s pour la p??riode
            List<Couple> userAccessToPublication = null;
            for (Period period : userAccessToPublicationCache.keySet()) {
                if (period.end.equals(end) && period.start.equals(start)) {
                    // L'objet du cache contient les valeurs pour cette p??riode
                    logger.debug("R??cup??ration des acc??s aux publications dans le cache...");
                    userAccessToPublication = userAccessToPublicationCache.get(period);
                }
            }
            // Si pas trouv?? dans le cache, on le calcule
            if (userAccessToPublication == null) {
                DateFormat format = new SimpleDateFormat();
                logger.debug("Ajout dans le cache des acc??s aux publications du " + format.format(start) + " au " + ""
                        + format.format(end) + "...");
                List<Couple> data = StatDataDao.this.getAccessedRegistrationsNoCache(start, end);
                userAccessToPublicationCache.put(new Period(start, end), data);
                logger.debug("Cache des acc??s aux publications : " + userAccessToPublicationCache.size() + " ??l??ments"
                        + ".");
                userAccessToPublication = data;
            }

            // Dans la liste des acc??s, on ne garde que ceux des publications demand??es
            PublicationDao publicationDao = new PublicationDao(context);
            List<Couple> result = new ArrayList<>();
            for (Couple couple : userAccessToPublication) {
                String user = couple.left;
                String publicationId = couple.right;
                Publication publication = publicationDao.findByReference(publicationId);

                if (contains(publications, publication)) {
                    if (user.startsWith("mailto:")) {
                        user = user.substring("mailto:".length());
                    }
                    result.add(new Couple(user, publicationId));
                }
            }

            // On la stocke en requestCache
            requestCache.put(incomingRequest, result);

            logger.debug("Nombre d'acc??s aux " + publications.size() + " publications demand??es : " + result.size());
            return result;
        }

        /**
         * On stocke les param??tres des requ??tes, afin de les comparer avec les prochaines requ??tes, et utiliser le
         * requestCache.
         */
        private class Request implements Serializable {

            final List<Publication> publications;
            final Date start;
            final Date end;

            Request(List<Publication> publications, Date start, Date end) {
                this.publications = publications;
                this.start = start;
                this.end = end;
            }

            public List<Publication> getPublications() {
                return publications;
            }

            protected boolean equals(Request request) {
                // Est-ce les m??mes dates de d??but et fin ?
                // Cette liste de publication du requestCache est-elle ??gale ?? celle de la requ??te pass??e en
                // param??tres ?
                return this.start.getTime() == request.start.getTime() && this.end.getTime() == request.end.getTime()
                        && CollectionUtils.isEqualCollection(this.publications, request.publications,
                        new Equator<Publication>() {
                            @Override
                            public boolean equate(Publication o1, Publication o2) {
                                return o1 == o2 || Objects.equals(o1.getObjectId().toString(), o2.getObjectId().toString());
                            }

                            @Override
                            public int hash(Publication o) {
                                return Objects.hash(o.getObjectId().toString());
                            }
                        });
            }

            @Override
            public String toString() {
                StringBuilder publicationsString = new StringBuilder();
                for (Publication publication : publications) {
                    publicationsString.append(publication.getReference()).append(",");
                }
                if (publicationsString.toString().endsWith(",")) {
                    publicationsString = new StringBuilder(publicationsString.substring(0,
                            publicationsString.length() - 1));
                }
                DateFormat format = new SimpleDateFormat();
                return "Publications=" + publicationsString + " start=" + format.format(start) + " end" + "="
                        + format.format(end);
            }
        }

        private class Period implements Serializable {
            private final Date start;
            private final Date end;

            Period(Date start, Date end) {
                this.start = start;
                this.end = end;
            }
        }
    }
}
