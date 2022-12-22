package fr.solunea.thaleia.model.dao;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.CayenneDataObject;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("unchecked")
public class Cache<X extends BaseDataObject> implements Serializable {

    private final CayenneDao cayenneDao;
    private Map<String, List<X>> objectsCache = new HashMap<>();
    private Date lastRequestDate = Calendar.getInstance().getTime();

    Cache(CayenneDao cayenneDao) {
        this.cayenneDao = cayenneDao;
    }

    /**
     * On recherche tous les objets dont la property = value
     */
    List<X> getObjects(String property, String value, long cacheDurationInSeconds) {
        String key = property + "." + value;

        // Remise à 0 du requestCache si besoin
        if (Calendar.getInstance().getTime().getTime() - lastRequestDate.getTime() > cacheDurationInSeconds * 1000) {
            objectsCache = new HashMap<>();
        }
        lastRequestDate = Calendar.getInstance().getTime();

        // On cherche dans le requestCache
        List<X> result = objectsCache.get(key);
        if (result == null) {
            // Absence de cette clé en requestCache : on effectue la requête en base
            result = (List<X>) cayenneDao.findMatchByProperty(property, value);
            objectsCache.put(key, result);
            return result;
        } else {
            // Trouvé : on le renvoie
            return result;
        }
    }
}
