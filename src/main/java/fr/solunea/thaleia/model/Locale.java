package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._Locale;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * <p>Locale class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Locale extends _Locale implements Serializable {

    protected static final Logger logger = Logger.getLogger(Locale.class.getName());

    public java.util.Locale getJavaLocale() {
        java.util.Locale result = null;
        try {
            result = java.util.Locale.forLanguageTag(this.name);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        if (result == null) {
            logger.warn("La locale " + this.name + " ne peut pas être reconnue comme locale Java.");
        }
        return result;
    }

}
