package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.ContentProperty;
import fr.solunea.thaleia.model.ContentPropertyName;
import fr.solunea.thaleia.model.ContentTypeProperty;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>ContentPropertyDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentPropertyDao extends CayenneDao<ContentProperty> {

    public ContentPropertyDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(ContentProperty object, Locale locale) {
        return object.getName();
    }

    /**
     * <p>findByName.</p>
     *
     * @param name le nom
     * @return la première ContentProperty trouvée qui porte ce nom, ou null si elle n'a pas été trouvée.
     */
    public ContentProperty findByName(String name) {

        String checkedString = name;
        if (checkedString == null) {
            checkedString = "";
        }

        try {
            SelectQuery query = new SelectQuery(ContentProperty.class, ExpressionFactory.matchExp(ContentProperty
                    .NAME.getName(), checkedString));

            List<ContentProperty> properties = CastUtils.castList(getContext().performQuery(query), ContentProperty
                    .class);

            if (properties.size() > 0) {
                return properties.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.warn("Impossible de récupére la ContentProperty '" + checkedString + "' : " + e);
            return null;
        }
    }

    /**
     * <p>getContentPropertyNames.</p>
     *
     * @param contentProperty la propriété
     * @return pour tous les ContentType existants pour lesquels la ContentProperty est associée, et pour toutes les
     * locales existantes, récupère le ContentPropertyName correspondant. Si ce ContentPropertyName n'existe pas, il est
     * créé, avec par défaut la valeur ContentProperty.name
     */
    public List<ContentPropertyName> getContentPropertyNames(ContentProperty contentProperty) {
        List<ContentPropertyName> result = new ArrayList<>();

        ContentPropertyNameDao contentPropertyNameDao = new ContentPropertyNameDao(context);

        for (ContentTypeProperty contentTypeProperty : new ContentTypePropertyDao(context).find()) {
            if (contentTypeProperty.getContentProperty().equals(contentProperty)) {
                // On a un ContentType pour lequel il faut récupérer la
                // ContentPropertyName
                // Pour toutes les locales du système
                for (fr.solunea.thaleia.model.Locale locale : new LocaleDao(context).find()) {
                    // On recherche la ContentPropertyName
                    List<ContentPropertyName> contentPropertyNames = contentPropertyNameDao.get(contentTypeProperty,
                            locale);
                    if (contentPropertyNames.size() > 0) {
                        // Au moins une existe : on prend la première
                        result.add(contentPropertyNames.get(0));
                    } else {
                        // Elle n'existe pas : on la crée
                        ContentPropertyName name = contentPropertyNameDao.get();
                        name.setLocale(locale);
                        name.setContentTypeProperty(contentTypeProperty);
                        name.setName(contentProperty.getName());
                        result.add(name);
                    }
                }
            }
        }

        return result;
    }
}
