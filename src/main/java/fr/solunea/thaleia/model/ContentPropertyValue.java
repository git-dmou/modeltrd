package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._ContentPropertyValue;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * <p>ContentPropertyValue class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentPropertyValue extends _ContentPropertyValue implements Serializable {

    private static final Logger logger = Logger.getLogger(ContentPropertyValue.class);

    /**
     * <p>getName.</p>
     *
     * @param locale la locale
     * @return le nom de cette propriété, d'après le type de contenu qui possède
     * cette valeur sur la propriété. Par exemple, pour la
     * ContentProperty "Title", le nom de la propriété sera
     * "Titre de l'écran" pour un ContentType "Screen" et sera
     * "Titre du document" pour un ContentType "Document".
     */
    public String getName(Locale locale) {
        try {
            ContentType contentType = this.getContentVersion().getContentType();
            List<ContentTypeProperty> contentTypeProperties = this.getProperty().getContentTypes();
            for (ContentTypeProperty contentTypeProperty : contentTypeProperties) {
                if (contentTypeProperty.getContentType().equals(contentType)) {
                    List<ContentPropertyName> names = contentTypeProperty.getNames();
                    for (ContentPropertyName name : names) {
                        if (name.getLocale().equals(locale)) {
                            return name.getName();
                        }
                    }
                    // Si pas de ContentPropertyName trouvé pour ce ContentType
                    // dans la locale, alors on renvoie la valeur non localisée.
                    return contentTypeProperty.getContentProperty().getName();

                }
            }
            // Pas de ContentType trouvé pour cette ContentPropertyValue. On a
            // un problème de cohérence de données !
            logger.warn("Pas de ContentType trouvé pour " + this + " : vérifiez la cohérences des données !");
            return "";

        } catch (Exception e) {
            logger.warn("Impossible de récupérer la localisation du nom de la propriété '" + this + "' : " + e);
            return "";
        }

    }

    /**
     * @return true si la valeur localisée de cette propriété correspond au booléen True.
     */
    public boolean isValueIsTrue() {
        return ("vrai".equals(getValue().toLowerCase()) || "true".equals(getValue().toLowerCase())
                || "verdarero".equals(getValue().toLowerCase()) || "juist".equals(getValue().toLowerCase()));
    }

    /**
     * @return true si la valeur localisée de cette propriété correspond à "Oui".
     */
    public boolean isValueIsYes() {
        return ("oui".equals(getValue().toLowerCase()) || "yes".equals(getValue().toLowerCase())
                || "si".equals(getValue().toLowerCase()) || "ja".equals(getValue().toLowerCase()));
    }

}
