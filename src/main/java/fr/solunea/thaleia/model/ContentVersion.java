package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._ContentVersion;
import fr.solunea.thaleia.model.dao.ContentPropertyValueDao;
import org.apache.log4j.Logger;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ContentVersion extends _ContentVersion implements Serializable, Comparable<ContentVersion> {

    /**
     * Le numéro de la première version d'un contenu
     */
    public static final int FIRST_VERSION_NUMBER = 1;
    private static final Logger logger = Logger.getLogger(ContentVersion.class);

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(ContentVersion o) {
        if (o == null) {
            logger.debug("Impossible de comparer '" + this + "' avec nul !");
            return 0;
        }

        try {
            return this.getRevisionNumber().compareTo(o.getRevisionNumber());
        } catch (Exception e) {
            logger.debug("Impossible de comparer '" + this + "' (rev. number=" + this.revisionNumber + ") avec '" + o + "' (rev. number=" + o.revisionNumber + ") :" + e);
            return 0;
        }
    }

    /**
     * <p>getProperty.</p>
     *
     * @param localizedPropertyName le nom localisé de la propriété que l'on recherche.
     * @return La valeur de propriété (ContentPropertyValue) dont le nom
     * localisé (pour ce type de contenu) est celui demandé, ou null si
     * aucune ne porte ce nom, ou ou encore si cette propriété existe,
     * mais elle n'a pas de valeur (ContentPropertyVlaue) pour cette
     * version.
     */
    public ContentPropertyValue getProperty(String localizedPropertyName) {

        String checkedLocalizedPropertyName = localizedPropertyName;
        if (checkedLocalizedPropertyName == null) {
            checkedLocalizedPropertyName = "";
        }

        // On cherche La propriété qui porte le nom localizedPropertyName pour
        // le ContentType courant.
        ContentProperty property = null;

        // Pour le contentType, on recherche les propriétés qui peuvent être
        // définies
        for (ContentTypeProperty contentTypeProperty : this.getContentType().getProperties()) {
            // On recherche les noms existants de cette propriété de ContentType
            for (ContentPropertyName name : contentTypeProperty.getNames()) {
                if (checkedLocalizedPropertyName.equals(name.getName())) {
                    property = contentTypeProperty.getContentProperty();
                }
            }
        }

        if (property == null) {
            // On n'a pas trouvé de propriété portant ce nom
            return null;

        } else {
            // On recherche dans les ContentPropertyValue du ContentVersion
            // celle qui correspond à la ContentProperty trouvée.
            for (ContentPropertyValue value : this.getProperties()) {
                if (value.getProperty().equals(property)) {
                    return value;
                }
            }
            // On n'a pas trouvé de valeur pour cette propriété définie pour ce
            // ContentVersion.
            return null;
        }

    }

    /**
     * <p>getPropertyValue.</p>
     *
     * @param contentProperty         la propriété.
     * @param locale                  la locale
     * @param defaultValue            la valeur par défaut.
     * @param contentPropertyValueDao le DAO à utiliser.
     * @return la valeur de cette propriété, pour cette locale, associée à cette
     * version. Si cette valeur ne peut pas être retrouvée, alors on
     * renvoie la valeur par défaut.
     */
    public String getPropertyValue(ContentProperty contentProperty, Locale locale, String defaultValue,
                                   ContentPropertyValueDao contentPropertyValueDao) {

        ContentPropertyValue value = getPropertyValue(contentProperty, locale);

        if (value == null) {
            return defaultValue;
        } else {
            return contentPropertyValueDao.getValue(value);
        }

    }

    /**
     * <p>getPropertyValue.</p>
     *
     * @param contentProperty la propriété.
     * @param locale          la locale.
     * @return la valeur de cette propriété, pour cette locale, associée à cette
     * version. Si cette valeur ne peut pas être retrouvée, alors on
     * renvoie null.
     */
    public ContentPropertyValue getPropertyValue(ContentProperty contentProperty, Locale locale) {

        for (ContentPropertyValue value : getProperties()) {
            if (value.getProperty().equals(contentProperty) && value.getLocale().equals(locale)) {
                return value;
            }
        }

        return null;
    }

    /**
     * <p>hasChild.</p>
     *
     * @param child un contenu
     * @return true si cette version contient ce contenu parmi ses fils de
     * PREMIER NIVEAU (on ne cherche pas dans les fils de ses fils).
     */
    public boolean hasChild(Content child) {
        for (Allocation allocation : getChilds()) {
            if (allocation.getChild().equals(child)) {
                return true;
            }
        }
        return false;
    }

}
