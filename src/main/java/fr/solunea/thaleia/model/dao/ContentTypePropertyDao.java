package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.ContentProperty;
import fr.solunea.thaleia.model.ContentTypeProperty;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>ContentTypePropertyDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentTypePropertyDao extends CayenneDao<ContentTypeProperty> {

    public ContentTypePropertyDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(ContentTypeProperty object, Locale locale) {
        return "";
    }

    /**
     * <p>findContentPropertiesByContentType.</p>
     *
     * @param contentTypeId a int.
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("unused")
    public List<ContentProperty> findContentPropertiesByContentType(int contentTypeId) {
        try {

            logger.debug("Lancement de la requête...");
            SelectQuery query = new SelectQuery(ContentTypeProperty.class, ExpressionFactory.matchExp
                    (ContentTypeProperty.CONTENT_TYPE.getName(), contentTypeId));

            List<ContentTypeProperty> contentTypeProperties = CastUtils.castList(getContext().performQuery(query),
                    ContentTypeProperty.class);

            ArrayList<ContentProperty> properties = new ArrayList<>();

            logger.debug("Ajout du résultat au tableau properties");
            if (contentTypeProperties.size() > 0) {
                for (ContentTypeProperty contentTypeProperty : contentTypeProperties) {
                    properties.add(contentTypeProperty.getContentProperty());
                }
                logger.debug("On retourne " + properties);
            }

            return (properties);
        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContentProperty du ContentType '" + contentTypeId + "' : " + e);
            return null;
        }
    }

    /**
     * <p>findContentTypePropertiesByContentType.</p>
     *
     * @param contentTypeId a int.
     * @return toutes les ContentTypeProperties associées au ContentType contentType
     */
    public List<ContentTypeProperty> findContentTypePropertiesByContentType(int contentTypeId) {
        try {

            SelectQuery query = new SelectQuery(ContentTypeProperty.class, ExpressionFactory.matchExp
                    (ContentTypeProperty.CONTENT_TYPE.getName(), contentTypeId));

            return (CastUtils.castList(getContext().performQuery(query), ContentTypeProperty.class));
        } catch (Exception e) {
            logger.warn(
                    "Impossible de récupérer les ContentTypeProperties du ContentType '" + contentTypeId + "' : " + e);
            return null;
        }
    }

}
