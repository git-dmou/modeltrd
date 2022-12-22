package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.ContentPropertyName;
import fr.solunea.thaleia.model.ContentType;
import fr.solunea.thaleia.model.ContentTypeProperty;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>ContentTypeDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentTypeDao extends CayenneDao<ContentType> {

    /**
     * Dans la table des paramètres de l'application, le préfixe des variables
     * de localisation des content Type. Pour le contentType "name", on doit
     * avoir le paramètre : [CONTENT_TYPE_PARAM].[name du contenttype].[locale].
     * Par exemple : contenttype.localisation.video.fr = Vidéo
     */
    public static final String CONTENT_TYPE_PARAM = "contenttype.localisation.";

    /**
     * Dans la table des paramètres de l'application, le nom du type de contenu
     * par défaut, qui sera associé aux contenus créés.
     */
    private static final String DEFAULT_CONTENT_TYPE_PARAM = "default.contenttype.name";

    /**
     * Dans la table des paramètres de l'application, le nom du type de contenu
     * par défaut, qui sera associé aux modules créés.
     */
    private static final String DEFAULT_MODULE_TYPE_PARAM = "default.moduletype.name";

    public ContentTypeDao(ObjectContext context) {
        super(context);
    }

    /**
     * <p>getDefaultContentType.</p>
     *
     * @return le contentType par défaut, ou null s'il n'est pas défini.
     */
    public ContentType getDefaultContentType() {
        return getContentType(DEFAULT_CONTENT_TYPE_PARAM);
    }

    /**
     * <p>getContentType.</p>
     *
     * @param key le nom du paramètre d'application qui contient comme valeur le nom du contentType
     * @return le contentType par défaut correspondant à la clé demandé, ou null s'il n'est pas défini.
     */
    public ContentType getContentType(String key) {
        // On recherche dans la base de données le nom du contentTYpe par défaut
        try {
            String contentTypeName = new ApplicationParameterDao(context).getValue(key, "");

            return (findByName(contentTypeName));

        } catch (Exception e) {
            logger.warn("Impossible de récupérer le contentType par défaut dans le paramètre '" + key + "' : " + e);
            return null;
        }
    }

    /**
     * <p>findByName.</p>
     *
     * @param contentTypeName : nom du ContentType à rechercher
     * @return le ContentType ayant pour nom 'contentTypeName'
     */
    public ContentType findByName(String contentTypeName) {
        List<ContentType> contentTypes = findAllByName(contentTypeName);
        if (contentTypes.size() > 0) {
            return contentTypes.get(0);
        } else {
            logger.warn("Le contentType '" + contentTypeName + "' n'existe pas !");
            return null;
        }
    }

    /**
     * <p>findAllByName.</p>
     *
     * @param contentTypeName : nom du ContentType à rechercher
     * @return les ContentType ayant pour nom 'contentTypeName'
     */
    public List<ContentType> findAllByName(String contentTypeName) {
        return findMatchByProperty(ContentType.NAME.getName(), contentTypeName);
    }

    /**
     * <p>getDefaultModuleType.</p>
     *
     * @return a {@link fr.solunea.thaleia.model.ContentType} object.
     */
    public ContentType getDefaultModuleType() {
        return getContentType(DEFAULT_MODULE_TYPE_PARAM);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(ContentType object, Locale locale) {
        // On recherche dans la base de données la localisation de ce
        // contentType
        // logger.debug("Recherche du nom du contentType " + object
        // + " pour la locale " + locale);
        try {
            // La locale de ce dao a été initialisée avec la locale de la
            // session, par exemple fr_FR. Or, on veut s'assurer que la
            // recherche de la localisation n'utilise que les deux premiers
            // caractères : fr.
            String twoCharsLocale = locale.getLanguage();

            // logger.debug("Recherche de la valeur de présentation du ContentType "
            // + object
            // + " pour la locale "
            // + locale
            // + " (= '"
            // + twoCharsLocale + "')");
            return new ApplicationParameterDao(context).getValue(
                    CONTENT_TYPE_PARAM + object.getName() + "." + twoCharsLocale, object.getName());

        } catch (Exception e) {
            logger.warn("Impossible de récupérer la localisation du contentType '" + object.toString() + "' : " + e);
            return object.getName();
        }
    }

    /**
     * <p>find.</p>
     *
     * @param forModules a boolean.
     * @return la liste de tous les contentType, pour modules seulement, ou pour les contenus qui ne sont pas des
     * modules seulement.
     */
    public List<ContentType> find(boolean forModules) {
        List<ContentType> result = new ArrayList<>();
        for (ContentType contentType : find()) {
            if (contentType.getIsModuleType() == forModules) {
                result.add(contentType);
            }
        }

        return result;
    }

    /**
     * <p>existsWithName.</p>
     *
     * @param name        a {@link java.lang.String} object.
     * @param contentType a {@link fr.solunea.thaleia.model.ContentType} object.
     * @return true si un contentType existe avec ce nom, à l'exclusion du contentType passé en paramètre.
     */
    public boolean existsWithName(String name, ContentType contentType) {
        List<ContentType> contentTypes = findMatchByProperty(ContentType.NAME.getName(), name);

        return containsObjects(contentTypes, contentType);
    }

    /**
     * <p>getAttributesNames.</p>
     *
     * @param contentType a {@link fr.solunea.thaleia.model.ContentType} object.
     * @param locale      a {@link fr.solunea.thaleia.model.Locale} object.
     * @return La liste des noms des propriétés à présenter dans les IHM pour ce type de contenu.
     */
    @SuppressWarnings("unused")
    public List<String> getAttributesNames(ContentType contentType, fr.solunea.thaleia.model.Locale locale) {
        List<String> result = new ArrayList<>();

        for (ContentTypeProperty property : contentType.getProperties()) {
            // On recherche le nom de cette propriété pour la locale demandée
            for (ContentPropertyName name : property.getNames()) {
                if (locale.equals(name.getLocale())) {
                    result.add(name.getName());
                }
            }
        }

        return result;
    }

}
