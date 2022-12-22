package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Publication;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.LogUtils;
import fr.solunea.thaleia.utils.TimeFormatter;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

/**
 * <p>PublicationDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class PublicationDao extends CayenneDao<Publication> {

    protected static final Logger logger = Logger.getLogger(PublicationDao.class.getName());

    private final PublicationsCache activePublicationsCache = new PublicationsCache(true);
    private final PublicationsCache inactivePublicationsCache = new PublicationsCache(false);

    private class PublicationsCache implements Serializable {

        private final boolean onlyActivePublications;

        PublicationsCache(boolean onlyActivePublications) {
            this.onlyActivePublications = onlyActivePublications;
        }

        Map<User, List<Publication>> publicationsCache = new HashMap<>();
        Date lastRequestDate = Calendar.getInstance().getTime();
        long cacheDurationInSeconds;

        public List<Publication> getPublications(User user, long cacheDurationInSeconds) {
            this.cacheDurationInSeconds = cacheDurationInSeconds;

            // Remise à 0 du requestCache si besoin
            if (Calendar.getInstance().getTime().getTime() - lastRequestDate.getTime()
                    > cacheDurationInSeconds * 1000) {
                publicationsCache = new HashMap<>();
            }
            lastRequestDate = Calendar.getInstance().getTime();

            // On cherche dans le requestCache
            List<Publication> result = publicationsCache.get(user);
            if (result == null) {
                // Absence de ce user en requestCache : on effectue la requête en base
                //logger.debug("Absence de données pour le user " + user.getLogin());
                result = PublicationDao.this.getPublications(user, onlyActivePublications);
                publicationsCache.put(user, result);
                return result;
            } else {
                // Trouvé : on le renvoie
                //logger.debug("Données en requestCache pour le user " + user.getLogin());
                return result;
            }
        }
    }

    public PublicationDao(ObjectContext context) {
        super(context);
    }

    /**
     * <p>getPublications.</p>
     *
     * @param user                   a {@link fr.solunea.thaleia.model.User} object.
     * @param onlyActivePublications si true, on ne prend en compte que les publications actives
     * @return la liste de toutes les publications de cet utilisateur
     */
    @SuppressWarnings("unchecked")
    public List<Publication> getPublications(User user, boolean onlyActivePublications) {
        if (user == null) {
            return new ArrayList<>();
        }
        SelectQuery query = getPublicationsSelectQuery(user, onlyActivePublications);
        return executeQuery(query);
    }

    /**
     * <p>getPublications.</p>
     *
     * @param user                   a {@link fr.solunea.thaleia.model.User} object.
     * @param onlyActivePublications si true, on ne prend en compte que les publications actives
     * @return la liste de toutes les publications de cet utilisateur
     */
    @SuppressWarnings("unchecked")
    public List<Publication> getPublicationsWithCache(User user, boolean onlyActivePublications) {

        if (user == null) {
            return new ArrayList<>();
        }
        //        SelectQuery query = getPublicationsSelectQuery(user, onlyActivePublications);
        //        query.setCacheStrategy(QueryCacheStrategy.LOCAL_CACHE);
        //        query.setCacheGroups("publications");
        //        return executeQuery(query);
        //        return publicationsCache.getPublications(user, onlyActivePublications, 60);
        if (onlyActivePublications) {
            return activePublicationsCache.getPublications(user, 60);
        } else {
            return inactivePublicationsCache.getPublications(user, 60);
        }

    }

    @SuppressWarnings("unchecked")
    private List<Publication> executeQuery(SelectQuery query) {
        List<Publication> result = new ArrayList<>();
        try {
            result = getContext().performQuery(query);
        } catch (Exception e) {
            logger.warn("Erreur d'exécution de la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
        }
        return result;
    }

    private SelectQuery getPublicationsSelectQuery(User user, boolean onlyActivePublications) {
        Expression expression = ExpressionFactory.matchExp(Publication.USER.getName(), getPK(user));

        if (onlyActivePublications) {
            expression = expression.andExp(ExpressionFactory.matchExp(Publication.ACTIVE.getName(), true));
        }
        SelectQuery query = new SelectQuery(Publication.class, expression);
        query.setDistinct(true);
        return query;
    }

    /**
     * <p>findByReference.</p>
     *
     * @param reference a {@link java.lang.String} object.
     * @return la publication qui porte cette référence, ou null si elle n'a pas été trouvée.
     */
    @SuppressWarnings("unchecked")
    public Publication findByReference(String reference) {
        if (reference == null) {
            return null;
        }
        
        //        List<Publication> publications = findMatchByProperty(Publication.REFERENCE.getName(), reference);
        List<Publication> publications = findMatchByProperty(Publication.REFERENCE.getName(), reference, 60);
        if (publications.size() > 0) {
            return publications.get(0);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(Publication object, Locale locale) {

        if (object == null || locale == null) {
            logger.warn("publication = " + object + " locale = " + locale);
            return "";
        }

        // Le nom de la publication, et entre parenthèse la date de publication
        return object.getName() + " (" + TimeFormatter.localizeTimestamp(object.getDate().getTime(), locale) + ")";
    }

    /**
     * <p>getCreationDateComparator.</p>
     *
     * @return un comparateur par date de création de la publication.
     */
    public Comparator<Publication> getCreationDateComparator() {
        return new CreationDateComparator();
    }

    class CreationDateComparator implements Comparator<Publication>, Serializable {

        public int compare(final Publication o1, final Publication o2) {

            if (o1 == null || o2 == null) {
                logger.debug("Comparaison impossible d'objets nuls.");
                return 0;
            }

            // Récupération des dates de publication
            Date date1 = o1.getDate();
            Date date2 = o2.getDate();

            // Comparaison n'est pas raison
            int result;
            try {
                result = date1.compareTo(date2);
            } catch (Exception e) {
                // Peut arriver : NPE
                logger.debug("Problème de comparaison ? " + e);
                result = 0;
            }

            return result;
        }
    }
}
