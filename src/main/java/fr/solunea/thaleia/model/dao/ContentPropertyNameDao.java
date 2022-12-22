package fr.solunea.thaleia.model.dao;

import java.util.List;
import java.util.Locale;

import fr.solunea.thaleia.model.ContentPropertyName;
import fr.solunea.thaleia.model.ContentTypeProperty;
import org.apache.cayenne.ObjectContext;

/**
 * <p>ContentPropertyNameDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentPropertyNameDao extends CayenneDao<ContentPropertyName> {

	public ContentPropertyNameDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(ContentPropertyName object, Locale locale) {
		return object.getName();
	}

	/**
	 * <p>get.</p>
	 *
	 * @param contentTypeProperty
	 *            le type de propriété
	 * @param locale
	 *            la locale
	 * @return les ContentPropertyName pour cette ContentTypeProperty et cette
	 *         locale
	 */
	public List<ContentPropertyName> get(ContentTypeProperty contentTypeProperty,
			fr.solunea.thaleia.model.Locale locale) {
		return findMatchByProperties(ContentPropertyName.CONTENT_TYPE_PROPERTY.getName(), contentTypeProperty,
				ContentPropertyName.LOCALE.getName(), locale);
	}

	/**
	 * <p>get.</p>
	 *
	 * @param contentTypeProperty
	 *            le type de propriété
	 * @return les ContentPropertyName pour cette ContentTypeProperty (quelle
	 *         que soit la locale)
	 */
	public List<ContentPropertyName> get(ContentTypeProperty contentTypeProperty) {
		return findMatchByProperty(ContentPropertyName.CONTENT_TYPE_PROPERTY.getName(), contentTypeProperty);
	}

}
