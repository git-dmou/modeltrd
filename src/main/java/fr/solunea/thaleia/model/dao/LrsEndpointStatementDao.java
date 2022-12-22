package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.LrsEndpointStatement;
import fr.solunea.thaleia.utils.CastUtils;
import fr.solunea.thaleia.utils.DetailedException;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.SQLResult;
import org.apache.cayenne.query.SQLTemplate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public class LrsEndpointStatementDao extends CayenneDao<LrsEndpointStatement> implements Serializable {

    public LrsEndpointStatementDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(LrsEndpointStatement object, Locale locale) {
        return object.getName();
    }

    /**
     * @return le premier objet LrsEndpointStatement qui porte cet identifiant, ou bien null s'il n'a pas été trouvé.
     */
    @SuppressWarnings({"unchecked", "unused"})
    public LrsEndpointStatement findByStatementId(String statementId) {

        if (statementId == null) {
            return null;
        }

        try {
            String sql =
                    "SELECT * FROM lrs_endpoint_statement WHERE " + LrsEndpointStatement.JSONDATA.getName() + " " + "@> "
                            + "'{\"id\": \"$statementId\"}'";

            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            params.put("statementId", statementId);
            query.setParameters(params);

            List<LrsEndpointStatement> result = CastUtils.castList(getContext().performQuery(query),
                    LrsEndpointStatement.class);
            if (result.size() > 0) {
                return result.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.info("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @return le premier objet LrsEndpointStatement qui porte cet identifiant et qui est associé à ce LRS, ou bien null
     * s'il n'a pas été trouvé.
     */
    @SuppressWarnings("unchecked")
    public LrsEndpointStatement find(String statementId, int lrsEndpointId) {

        if (statementId == null) {
            return null;
        }

        List<LrsEndpointStatement> list = findMatchByProperties(LrsEndpointStatement.LRS_ENDPOINT.getName(),
                lrsEndpointId, LrsEndpointStatement.STATEMENT_ID.getName(), statementId);

        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @return toutes les activités pour lesquelles il existe au moins un statement, dans tous les lrs.
     */
    public List<String> getActivities() {
        return getActivities(null);
    }

    /**
     * @return toutes les activités pour lesquelles il existe au moins un statement dans ce lrs.
     */
    @SuppressWarnings({"unchecked"})
    private List<String> getActivities(LrsEndpoint lrs) {

        try {
            String sql = "SELECT DISTINCT jsondata :: JSONB -> 'object' ->> 'id' AS activity_id\n"
                    + "FROM lrs_endpoint_statement\n WHERE";

            if (lrs != null) {
                sql = sql + " lrs_endpoint_id = '$lrsId'\n" + "  AND\n";
            }
            sql = sql + "  jsondata :: JSONB -> 'object' ->> 'objectType' = 'Activity' ;";

            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            if (lrs != null) {
                params.put("lrsId", Integer.toString(getPK(lrs)));
            }
            query.setParameters(params);

            // Pour la récupération des valeurs par Cayenne
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("activity_id");
            query.setResult(resultDescriptor);

            return (List<String>) getContext().performQuery(query);

        } catch (Exception e) {
            logger.info("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * @return le nombre d'acteurs distincts pour lesquels il existe au moins un statement pour cette activité,
     * enregistré entre ces deux dates. Si objectId est null, alors on calcule pour toutes les activités. Si lrs est
     * null, alors on calcule pour tous les LRS.
     */
    @SuppressWarnings("unused")
    public Long countActors(LrsEndpoint lrs, String objectId, Date start, Date end) {
        return countSomething(lrs, Collections.singletonList(objectId), start, end,
                "DISTINCT jsondata :: JSONB -> 'actor' ->> " + "'name'", "");
    }

    /**
     * @return le nombre d'acteurs distincts pour lesquels il existe au moins un statement pour ces activités,
     * enregistré entre ces deux dates. Si objectId est null ou vide, alors on calcule pour toutes
     * les activités. Si lrs est null, alors on calcule pour tous les LRS.
     */
    public Long countActors(LrsEndpoint lrs, List<String> objectsId, Date start, Date end) {
        return countSomething(lrs, objectsId, start, end, "DISTINCT jsondata :: JSONB -> 'actor' ->> " + "'name'", "");
    }

    /**
     * @return le nombre d'activités distinctes pour lesquelles il existe au moins un statement pour ce LRS,
     * enregistré entre ces deux dates. Si lrs est null, alors on calcule pour tous les LRS.
     */
    public Long countActivities(LrsEndpoint lrs, Date start, Date end) {
        return countSomething(lrs, null, start, end, "DISTINCT jsondata :: JSONB -> 'object' ->> 'id'", "");
    }

    /**
     * @return le nombre de statements pour ce LRS, enregistré entre ces deux dates. Si lrs est null, alors on
     * calcule pour tous les LRS.
     */
    public Long countStatements(LrsEndpoint lrs, Date start, Date end) {
        return countSomething(lrs, null, start, end, "*", "");
    }

    /**
     * @param lrs       le lrs. Si null, alors on cherche dans tous les LRS
     * @param objectsId les id d'objects concernés. Si vide, alors on fait la recherche quel que soit l'id d'object.
     * @param start     le début de la période
     * @param end       la fin de la période
     * @param something le champ du statement dont il faut compter les occurences. Par
     *                  exemple, pour compter le nombre d'acteurs : "DISTINCT jsondata :: JSONB -> 'actor' ->> " +
     *                  "'name'"
     * @param condition Si non nulle ou vide, alors on va ajouter cette condition à la requête
     * @return le nombre de "quelque chose" pour lesquels il existe au moins un statement pour ces
     * activités et ce LRS, enregistré entre ces deux dates. Si objectId est null ou vide, alors on calcule pour toutes
     * les activités. Si lrs est null, alors on calcule pour tous les LRS.
     */
    @SuppressWarnings("unchecked")
    private Long countSomething(LrsEndpoint lrs, List<String> objectsId, Date start, Date end, String something,
                                String condition) {

        if (start == null || end == null) {
            return 0L;
        }

        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

        try {
            String sql = "SELECT COUNT(" + something + ") AS something FROM " + "lrs_endpoint_statement WHERE ";

            if (condition != null && condition.length() > 0) {
                sql = sql + " " + condition + " AND ";
            }

            if (lrs != null) {
                sql = sql + " lrs_endpoint_id = '$lrsId' AND ";
            }
            if (objectsId != null && !objectsId.isEmpty()) {
                sql = sql + " jsondata :: JSONB -> 'object' ->> 'id' IN ($objectsId) AND ";
            }
            sql = sql + " (jsondata :: JSONB ->> 'timestamp') ::"
                    + " TIMESTAMP  >= '$start' :: TIMESTAMP AND (jsondata :: JSONB ->>'timestamp') "
                    + ":: TIMESTAMP  < '$end' :: TIMESTAMP ;";
            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            if (lrs != null) {
                params.put("lrsId", Integer.toString(getPK(lrs)));
            }
            if (objectsId != null && !objectsId.isEmpty()) {
                // On fixe la valeur du paramètres $objectsId dans la requête
                params.putAll(new StatDataDao.ReferencesParamGenerator<String>("objectsId", objectsId) {
                    @Override
                    protected String getValue(String object) {
                        return object;
                    }
                }.getMap());
            }
            params.put("start", formatTimestamp(start));
            params.put("end", formatTimestamp(end));
            query.setParameters(params);

            // Pour la récupération des valeurs par Cayenne
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("something");
            query.setResult(resultDescriptor);

            List<Long> result = getContext().performQuery(query);
            String details = "";
            if (lrs != null) {
                details = details + " pour le LRS " + lrs.getName();
            }
            if (objectsId != null) {
                details = details + " pour les activités " + String.join(", ", objectsId);
            }

            logger.debug("On compte un nombre d'entités (" + something + ") " + details + " entre "
                    + dateTimeInstance.format(start) + " et " + "" + dateTimeInstance.format(end) + " : "
                    + Arrays.toString(result.toArray()));

            return returnLongFromResultSet(result);

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + e.getMessage() + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            return 0L;
        }
    }

    /**
     * @return la date formattée au format '2016-03-01 01:00:00.0' : ISO 8601
     */
    private String formatTimestamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.format(date.getTime());
    }

    /**
     * Le nombre moyen d'activités différentes pour lesquelles les utilisateurs ont des statements sur cette période.
     */
    @SuppressWarnings("unchecked")
    public Double averageActivities(LrsEndpoint lrs, Date start, Date end) {
        if (start == null || end == null) {
            return 0d;
        }

        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

        try {
            String sql = "SELECT AVG(count) FROM (SELECT COUNT(DISTINCT jsondata :: JSONB -> 'object' ->> 'id'), "
                    + "jsondata :: JSONB -> 'actor' ->> 'name' AS actor FROM lrs_endpoint_statement WHERE ";

            if (lrs != null) {
                sql = sql + " lrs_endpoint_id = '$lrsId' AND ";
            }

            sql = sql + "(jsondata :: JSONB ->> 'timestamp') ::  TIMESTAMP  >= '$start' :: TIMESTAMP AND (jsondata :: "
                    + "JSONB  ->>'timestamp') :: TIMESTAMP  < '$end' :: TIMESTAMP GROUP BY actor) AS temptable;";

            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            if (lrs != null) {
                params.put("lrsId", Integer.toString(getPK(lrs)));
            }
            params.put("start", formatTimestamp(start));
            params.put("end", formatTimestamp(end));
            query.setParameters(params);

            // Pour la récupération des valeurs par Cayenne
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("something");
            query.setResult(resultDescriptor);

            List<BigDecimal> result = getContext().performQuery(query);
            String details = "";
            if (lrs != null) {
                details = details + " pour le LRS " + lrs.getName();
            }

            logger.debug("On compte la moyenne du nombre d'activités par utilsiateur " + details + " entre "
                    + dateTimeInstance.format(start) + " et " + "" + dateTimeInstance.format(end) + " : "
                    + Arrays.toString(result.toArray()));

            return returnBigDecimalFromResultSet(result).doubleValue();

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + e.getMessage() + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            return 0d;
        }
    }

    private Long returnLongFromResultSet(List<Long> result) {
        if (result.size() > 0) {
            if (result.get(0) == null) {
                return 0L;
            }
            try {
                return result.get(0);
            } catch (NumberFormatException e) {
                logger.debug("Le nombre d'entités distinctes retrouvé n'est pas du bon type : " + e);
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    private Double returnDoubleFromResultSet(List<Double> result) {
        if (result.size() > 0) {
            if (result.get(0) == null) {
                return 0d;
            }
            try {
                return result.get(0);
            } catch (NumberFormatException e) {
                logger.debug("Le nombre d'entités distinctes retrouvé n'est pas du bon type : " + e);
                return 0d;
            }
        } else {
            return 0d;
        }
    }

    private BigDecimal returnBigDecimalFromResultSet(List<BigDecimal> result) {
        if (result.size() > 0) {
            if (result.get(0) == null) {
                return BigDecimal.valueOf(0);
            }
            try {
                return result.get(0);
            } catch (NumberFormatException e) {
                logger.debug("Le nombre d'entités distinctes retrouvé n'est pas du bon type : " + e);
                return BigDecimal.valueOf(0);

            }
        } else {
            return BigDecimal.valueOf(0);

        }
    }

    /**
     * @return le nombre de statements pour lesquels result.success = result (<- passé en paramètre)
     */
    public Long countResults(LrsEndpoint lrs, List<String> activities, Date start, Date end, String result) {
        return countSomething(lrs, activities, start, end, "DISTINCT id",
                "jsondata :: JSONB -> 'result' ->> 'success' = " + "'" + result + "'");
    }

    /**
     * @return la moyenne des scores moyens des acteurs sur cette période.
     */
    @SuppressWarnings("unchecked")
    public Double meanScore(LrsEndpoint lrs, List<String> activities, Date start, Date end) {
        if (start == null || end == null) {
            return 0d;
        }

        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

        try {
            String sql = "SELECT AVG(actor_score) as meanscore FROM ( SELECT actor, AVG(score) as actor_score FROM ( "
                    + "SELECT (jsondata :: JSONB -> 'result' -> 'score' ->> 'raw') :: FLOAT AS score, jsondata :: "
                    + "JSONB -> 'actor' ->> 'name' AS actor FROM lrs_endpoint_statement WHERE  ";

            if (lrs != null) {
                sql = sql + " lrs_endpoint_id = '$lrsId' AND ";
            }
            if (activities != null && !activities.isEmpty()) {
                sql = sql + " jsondata :: JSONB -> 'object' ->> 'id' IN ($objectsId) AND ";
            }

            sql = sql + " (jsondata :: JSONB -> 'result' -> 'score' ->> 'raw') :: FLOAT NOTNULL AND (jsondata :: JSONB "
                    + "->> 'timestamp') ::  TIMESTAMP  >= '$start' :: TIMESTAMP AND (jsondata :: "
                    + "JSONB  ->>'timestamp') :: TIMESTAMP  < '$end' :: TIMESTAMP ) AS temptable GROUP BY actor ) as "
                    + "temptable2;";

            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            if (lrs != null) {
                params.put("lrsId", Integer.toString(getPK(lrs)));
            }
            if (activities != null && !activities.isEmpty()) {
                // On fixe la valeur du paramètres $objectsId dans la requête
                params.putAll(new StatDataDao.ReferencesParamGenerator<String>("objectsId", activities) {
                    @Override
                    protected String getValue(String object) {
                        return object;
                    }
                }.getMap());
            }
            params.put("start", formatTimestamp(start));
            params.put("end", formatTimestamp(end));
            query.setParameters(params);

            // Pour la récupération des valeurs par Cayenne
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("meanscore");
            query.setResult(resultDescriptor);

            List<Double> result = getContext().performQuery(query);
            String details = "";
            if (lrs != null) {
                details = details + " pour le LRS" + lrs.getName();
            }
            if (activities != null) {
                details = details + " pour les activités " + String.join(", ", activities);
            }

            logger.debug("On compte la moyenne des scores moyens des acteurs" + details + " entre "
                    + dateTimeInstance.format(start) + " et " + "" + dateTimeInstance.format(end) + " : "
                    + Arrays.toString(result.toArray()));

            return returnDoubleFromResultSet(result);

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + e.getMessage() + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            return 0d;
        }
    }

    /**
     * @return la dernière date de réception d'un statement pour ce LrsEndpoint (ou pour tous les Lrsenndoints s'il
     * est nul). Renvoie null si aucune Date n'est retrouvée.
     */
    public Date lastStatementRetrievedDate(LrsEndpoint lrsEndpoint) throws DetailedException {


        try {
            String sql = "SELECT date_retrieved FROM lrs_endpoint_statement LEFT JOIN lrs_endpoint le ON "
                    + "lrs_endpoint_statement.lrs_endpoint_id = le.id ";

            if (lrsEndpoint != null) {
                sql = sql + " WHERE lrs_endpoint_id = '$lrsId' ";
            }
            sql = sql + "ORDER BY  date_retrieved DESC LIMIT 1;";

            SQLTemplate query = new SQLTemplate(LrsEndpointStatement.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            if (lrsEndpoint != null) {
                params.put("lrsId", Integer.toString(getPK(lrsEndpoint)));
            }
            query.setParameters(params);

            // Pour la récupération des valeurs par Cayenne
            SQLResult resultDescriptor = new SQLResult();
            resultDescriptor.addColumnResult("date_retrieved");
            query.setResult(resultDescriptor);

            List<Date> results = getContext().performQuery(query);

            if (results.size() >= 1) {
                logger.debug("Date de dernier statement retrouvé : " + results.get(0));
                return results.get(0);
            } else {
                logger.debug("Aucun dernier statement retrouvé.");
                return null;
            }


        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + e.getMessage() + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            throw new DetailedException(e);
        }
    }
}
