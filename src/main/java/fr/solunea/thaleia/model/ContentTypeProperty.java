package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._ContentTypeProperty;
import org.apache.cayenne.PersistenceState;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * <p>ContentTypeProperty class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentTypeProperty extends _ContentTypeProperty implements Serializable {

    private static final Logger logger = Logger.getLogger(ContentTypeProperty.class);

    /**
     * <p>getName.</p>
     *
     * @param locale       la locale
     * @param defaultValue la valeur par défaut.
     * @return le nom de la ContentPropertyName défini pour cette locale, si
     * elle existe. Sinon, renvoie la valeur par défaut.
     */
    public String getName(Locale locale, String defaultValue) {

        if (locale == null) {
            return defaultValue;
        }

        if (this.getPersistenceState() == PersistenceState.HOLLOW) {
            logger.debug("Impossible de récupérer le nom de la ContentProperty " + this
                    + " : L'objet est hollow. La valeur par défaut '" + defaultValue + "' est renvoyée.");
            return defaultValue;
        }

        try {
            for (ContentPropertyName name : this.getNames()) {
                if (locale.equals(name.getLocale())) {
                    return name.getName();
                }
            }
        } catch (Exception e) {
            // Il peut y avoir une Exception si l'objet courant est dans l'état
            // hollow
            logger.debug("Impossible de récupérer le nom de la ContentProperty " + this + " : " + e.toString()
                    + " La valeur par défaut '" + defaultValue + "' est renvoyée.");
        }

        return defaultValue;
    }

    /**
     * <p>getContentPropertyName.</p>
     *
     * @param locale la locale
     * @return La ContentPropertyName définie pour cette locale, si elle existe.
     * Sinon, renvoie null.
     */
    public ContentPropertyName getContentPropertyName(Locale locale) {

        if (locale == null) {
            return null;
        }

        for (ContentPropertyName name : this.getNames()) {
            if (locale.equals(name.getLocale())) {
                return name;
            }
        }

        return null;
    }

}
