package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.*;
import fr.solunea.thaleia.utils.DetailedException;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.PersistenceState;
import org.apache.cayenne.query.SelectById;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p>ContentPropertyValueDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentPropertyValueDao extends CayenneDao<ContentPropertyValue> {

    protected static final Logger logger = Logger.getLogger(ContentPropertyValueDao.class.getName());

    /**
     * L'URL locale absolue du répertoire de base dans lequel sont stockés les
     * binaires dont le nom de fichier est stocké en base.
     */
    private final String binaryPropertyLocalDir;

    /**
     * Le nom de la contentProperty qui indique que la valeur de la propriété
     * est un fichier, et non une valeur pure.
     */
    private final String binaryPropertyType;

    /**
     * <p>Constructor for ContentPropertyValueDao.</p>
     *
     * @param binaryPropertyLocalDir l'URL locale absolue du répertoire de base dans lequel sont stockés les binaires
     *                               dont le nom de fichier est stocké en base
     * @param binaryPropertyType     le type de propriétés qui correspond aux propriétés décrivant un fichier binaire.
     */
    public ContentPropertyValueDao(String binaryPropertyLocalDir, String binaryPropertyType, ObjectContext context) {
        super(context);
        this.binaryPropertyLocalDir = binaryPropertyLocalDir;
        this.binaryPropertyType = binaryPropertyType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(ContentPropertyValue object, java.util.Locale locale) {
        return object.getContentVersion().getContentIdentifier() + "." + object.getContentVersion().getRevisionNumber()
                + "." + object.getProperty().getName();
    }

    /**
     * <p>find.</p>
     *
     * @param version  la version
     * @param property la propriété
     * @return tous les objets qui concernent cette version et cette propriété.
     */
    public List<ContentPropertyValue> find(ContentVersion version, ContentProperty property) {
        try {
            // On ne fait pas le code suivant, car il ne fonctionne pas avec des
            // objets "NEW" :
            // Expression qual = ExpressionFactory
            // .matchExp(ContentVersionProperty.CONTENT_VERSION_PROPERTY,
            // version)
            // .andExp(ExpressionFactory.matchExp(
            // ContentVersionProperty.PROPERTY_PROPERTY, property));
            // SelectQuery query = new SelectQuery(ContentVersionProperty.class,
            // qual);
            //
            // return CastUtils.castList(CayenneUtils.getDataContext()
            // .performQuery(query), ContentVersionProperty.class);

            List<ContentPropertyValue> result = new ArrayList<>();

            List<ContentPropertyValue> propertyValues = version.getProperties();
            for (ContentPropertyValue propertyValue : propertyValues) {
                if (propertyValue.getProperty().getName().equals(property.getName())) {
                    result.add(propertyValue);
                }
            }

            return result;

        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContentPropertyValue : " + e);
            return new ArrayList<>();
        }
    }

    public ContentPropertyValue findInDb(int id) {
        return SelectById.query(ContentPropertyValue.class, id).selectOne(context);
    }

    /**
     * <p>find.</p>
     *
     * @param version  la version
     * @param property la propriété
     * @param locale   la locale
     * @return tous les objets qui concernent cette version et cette propriété pour cette locale.
     */
    public List<ContentPropertyValue> find(ContentVersion version, ContentProperty property, Locale locale) {
        try {
            // On ne fait pas le code suivant, car il ne fonctionne pas avec des
            // objets "NEW" :
            // Expression qual = ExpressionFactory
            // .matchExp(ContentVersionProperty.CONTENT_VERSION_PROPERTY,
            // version)
            // .andExp(ExpressionFactory.matchExp(
            // ContentVersionProperty.PROPERTY_PROPERTY, property))
            // .andExp(ExpressionFactory.matchExp(
            // ContentVersionProperty.LOCALE_PROPERTY, locale));
            // SelectQuery query = new SelectQuery(ContentVersionProperty.class,
            // qual);
            //
            // return CastUtils.castList(CayenneUtils.getDataContext()
            // .performQuery(query), ContentVersionProperty.class);

            unHollowObjects(version, property, locale);

            List<ContentPropertyValue> result = new ArrayList<>();

            List<ContentPropertyValue> propertyValues = version.getProperties();
            for (ContentPropertyValue propertyValue : propertyValues) {
                //logger.debug("propertyValue=" + propertyValue);
                if (propertyValue.getProperty().getName().equals(property.getName())
                        && propertyValue.getLocale().getName().equals(locale.getName())) {
                    result.add(propertyValue);
                }
            }

            return result;

        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContentPropertyValue : " + e + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            logger.debug("Appel : " + LogUtils.getCallerInfo(3));
            logger.debug("version=" + version);
            logger.debug("property=" + property);
            logger.debug("locale=" + locale);
            return new ArrayList<>();
        }
    }

    /**
     * <p>isValueDescribesAFile.</p>
     *
     * @param property la propriété
     * @return true si la valeur de cette propriété est un fichier.
     */
    public boolean isValueDescribesAFile(ContentPropertyValue property) {
        boolean bool;
        try {
            unHollowObjects(property);

            // L'objet a été supprimé, et n'est plus associé au contexte
            bool = PersistenceState.TRANSIENT != property.getPersistenceState()
                    && binaryPropertyType.equals(property.getProperty().getValueType().getName());
        } catch (Exception e) {
            logger.debug(
                    "Impossible de déterminer si la propriété " + property + " se rapporte à un fichier binaire " + ": "
                            + e);
            bool = false;
        }
        return (bool);
    }

    /**
     * <p>getValue.</p>
     *
     * @param property la propriété
     * @return la valeur de cette propriété. Si la propriété correspond à un fichier, alors c'est le nom de ce fichier
     * qui est renvoyé (sans le nom du répertoire qui le contient).
     */
    public String getValue(ContentPropertyValue property) {
        unHollowObjects(property);

        // Si l'objet correspond à une propriété de type "fichier", alors la
        // valeur est le nom du fichier
        if (isValueDescribesAFile(property)) {

            String result = getFile(property).getName();

            // On ne présente que le nom du fichier, et pas le répertoire qui le
            // contient.
            if (result.contains("/")) {
                result = result.substring(result.lastIndexOf("/") + 1);
            }
            if (result.contains("\\")) {
                result = result.substring(result.lastIndexOf("\\") + 1);
            }
            return result;

        } else {
            return property.getValue();
        }
    }

    /**
     * <p>getFile.</p>
     *
     * @param property la propriété
     * @return le fichier correspondant à cette propriété, si cette propriété correspond à un fichier binaire. Sinon,
     * renvoie null.
     */
    public File getFile(ContentPropertyValue property) {
        unHollowObjects(property);

        if (isValueDescribesAFile(property)) {
            // logger.debug("On retourne " +
            // getLocaleFileRoot().getAbsolutePath()
            // + File.separator + property.getValue());
            return new File(getLocaleFileRoot().getAbsolutePath() + File.separator + property.getValue());
        } else {
            logger.debug("getFile() : le fichier est null.");
            return null;
        }
    }

    /**
     * Si cette propriété correspond à un type binaire, alors on copie le
     * fichier dans l'arborescence locale de stockage des fichiers binaires
     * (avec le nom de fichier demandé), et on stocke dans la valeur l'URL
     * relative d'accès à ce fichier.
     *
     * @param property la propriété
     * @param file     le binaire, dans un fichier temporaire
     * @param filename le nom du fichier
     * @throws fr.solunea.thaleia.utils.DetailedException si erreur
     */
    public void setFile(ContentPropertyValue property, File file, String filename) throws DetailedException {

        unHollowObjects(property);

        if (file.isDirectory()) {
            throw new DetailedException("On ne peut pas stocker '" + file.getAbsolutePath() + "' comme valeur de la "
                    + "propriété, car c'est un répertoire !");
        }

        if (isValueDescribesAFile(property)) {
            // On prépare le chemin relatif vers le fichier, d'après son nom

            // On s'assure que le filename est défini.
            String checkedFilename = filename;
            if ("".equals(checkedFilename)) {
                checkedFilename = file.getName();
            }

            // On prépare le nom du répertoire propre à ce fichier
            String uniqueId = Calendar.getInstance().getTimeInMillis() + "-" + RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(4) + 4);

            // On prépare le chemin relatif de ce fichier, par rapport à la
            // racine du stockage des fichiers uploadés.
            String relativeUri = uniqueId + File.separator + checkedFilename;

            // On copie le binaire
            File destination = new File(getLocaleFileRoot().getAbsolutePath() + File.separator + relativeUri);
            try {
                logger.debug("Copie du fichier '" + file + "' vers '" + destination + "'.");
                FileUtils.copyFile(file, destination);

            } catch (Exception e) {
                String message = "Impossible de copier le fichier '" + file + "' vers '" + destination + "' : " + e;
                logger.warn(message);
                throw new DetailedException(message);
            }

            // On stocke en base l'Uri relative du fichier copié
            property.setValue(relativeUri);

        } else {
            throw new DetailedException("Impossible de stocker un fichier dans une propriété de type '"
                    + property.getProperty().getValueType().getName() + "' !");
        }
    }

    /**
     * <p>getAllPropertiesValues.</p>
     *
     * @param version la version
     * @return toutes les ContentPropertyValue possibles pour cette version : celles définies en base, et celles
     * (vides)
     * qui pourraient être définie au vu de ce type de contenu.
     */
    @SuppressWarnings("unused")
    public List<ContentPropertyValue> getAllPropertiesValues(ContentVersion version) {

        unHollowObjects(version);

        // Toutes les locales du système
        List<Locale> locales = new LocaleDao(context).find();

        return getAllPropertiesValues(version, locales, false);
    }

    /**
     * <p>getAllPropertiesValues.</p>
     *
     * @param version la version
     * @param locale  la locale
     * @return toutes les ContentPropertyValue possibles pour cette version : celles définies en base, et celles
     * (vides)
     * qui pourraient être définie au vu de ce type de contenu.
     */
    @SuppressWarnings("unused")
    public List<ContentPropertyValue> getAllPropertiesValues(ContentVersion version, Locale locale) {

        unHollowObjects(version, locale);

        // Uniquement la locale demandée
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);

        return getAllPropertiesValues(version, locales, false);
    }

    /**
     * @param version               la version
     * @param locales               les locales
     * @param onlyVisibleProperties uniquement les propriétés visibles ?
     * @return toutes les ContentPropertyValue possibles pour cette version, mais uniquement dans les locales demandées
     * : celles définies en base, et celles (vides) qui pourraient être définie au vu de ce type de contenu.
     */
    private List<ContentPropertyValue> getAllPropertiesValues(ContentVersion version, List<Locale> locales, boolean
            onlyVisibleProperties) {

        unHollowObjects(version);
        unHollowObjectsList(locales);

        List<ContentPropertyValue> result = new ArrayList<>();

        // Toutes les ContentPropertyValue possibles pour ce contentType
        ContentType contentType = version.getContentType();
        List<ContentTypeProperty> contentTypeProperties = contentType.getProperties();
        for (ContentTypeProperty contentTypeProperty : contentTypeProperties) {
            // On récupère la Property
            ContentProperty property = contentTypeProperty.getContentProperty();
            if (onlyVisibleProperties) {
                if (!(contentTypeProperty.getHidden())) {
                    // Pour toutes les langues demandées
                    addContentPropertyValuesToResult(version, locales, result, property);
                }
            } else {
                // Pour toutes les langues demandées
                addContentPropertyValuesToResult(version, locales, result, property);
            }
        }

        // logger.debug("Les propriétés éditables de la version " + version
        // + " >>>> " + result);

        return result;
    }

    private void addContentPropertyValuesToResult(ContentVersion version, List<Locale> locales,
                                                  List<ContentPropertyValue> result, ContentProperty property) {
        for (Locale locale : locales) {
            // Cette valeur existe-t-elle pour cette version ?
            List<ContentPropertyValue> existingValues = find(version, property, locale);
            if (existingValues.isEmpty()) {
                // Non : on en crée une vide.
                // logger.debug("Création d'une ContentPropertyValue
                // pour property.name="
                // + property.getName()
                // + " locale="
                // + locale.getName());
                ContentPropertyValue value = get();
                value.setProperty(property);
                value.setLocale(locale);
                value.setContentVersion(version);
                result.add(value);
            } else {
                // Oui : on l'ajoute telle quelle au résultat
                result.addAll(existingValues);
                // logger.debug("Récupération de " +
                // existingValues.size()
                // + " ContentPropertyValue pour property.name="
                // + property.getName() + " locale="
                // + locale.getName() + " : " + existingValues);
            }
        }
    }

    /**
     * <p>getAllVisiblePropertiesValues.</p>
     *
     * @param version la version
     * @param locale  la locale
     * @param user    l'utilisateur
     * @return toutes les ContentPropertyValue visibles possibles pour cette version : celles définies en base, et
     * celles (vides) qui pourraient être définie au vu de ce type de contenu. Si l'utilisateur n'est pas admin, il ne
     * verra pas les propriétés cachées.
     */
    public List<ContentPropertyValue> getAllVisiblePropertiesValues(ContentVersion version, Locale locale, User user) {

        unHollowObjects(version, locale, user);

        // Uniquement la locale demandée
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);

        boolean onlyVisibleProperties = true;
        if (user.getIsAdmin()) {
            onlyVisibleProperties = false;
        }

        return getAllPropertiesValues(version, locales, onlyVisibleProperties);
    }

    /**
     * Supprime toutes les propriétés vides de cette version.
     *
     * @param version la version
     */
    public void deletePropertiesIfNoValue(ContentVersion version) {

        unHollowObjects(version);

        // On fabrique une NOUVELLE liste des objets à parcourir, sinon la liste
        // des ContentPropertyValue va être modifiée à mesure des éventuelles
        // suppression, ce qui apporte beaucoup de désagréments.
        List<ContentPropertyValue> propertyValues = version.getProperties();
        List<ContentPropertyValue> valuesToParse = new ArrayList<>(propertyValues);

        for (ContentPropertyValue propertyValue : valuesToParse) {
            if (propertyValue.getValue() == null || propertyValue.getValue().length() == 0) {
                try {
                    // logger.debug("Suppression de " + propertyValue);
                    version.removeFromProperties(propertyValue);
                    getContext().deleteObjects(propertyValue);
                } catch (Exception e) {
                    logger.warn("Impossible de supprimer " + propertyValue + " : " + e);
                }
            }
        }

    }

    /**
     * @return le répertoire dans le répertoire local de données correspondant au répertoire à partir duquel sont
     * exprimées les URL relatives stockées en base pour les fichiers binaires.
     */
    private File getLocaleFileRoot() {
        return new File(binaryPropertyLocalDir + File.separator + binaryPropertyType + File.separator);
    }

    /**
     * Supprime toutes les valeurs de cette ContentProperty pour cette
     * ContentVersion et cette Locale.
     *
     * @param version         la version
     * @param contentProperty la propriété
     * @param locale          la locale
     * @throws fr.solunea.thaleia.utils.DetailedException si erreur
     */
    public void deleteValues(ContentVersion version, ContentProperty contentProperty, Locale locale) throws
            DetailedException {

        unHollowObjects(version, contentProperty, locale);

        // On fabrique une NOUVELLE liste des objets à supprimer, sinon la liste
        // des ContentPropertyValue va être modifiée à mesure des éventuelles
        // suppression, ce qui apporte beaucoup de désagréments.

        List<ContentPropertyValue> valuesToDelete = new ArrayList<>();
        List<ContentPropertyValue> propertyValues = version.getProperties();
        for (ContentPropertyValue value : propertyValues) {
            if (locale.equals(value.getLocale()) && contentProperty.equals(value.getProperty())) {
                valuesToDelete.add(value);
            }
        }

        logger.debug(valuesToDelete.size() + " propriétés " + contentProperty.getName() + " à supprimer en "
                + locale.getName() + " dans la version " + version.toString());

        for (ContentPropertyValue value : valuesToDelete) {
            delete(value, false);
        }

    }

}
