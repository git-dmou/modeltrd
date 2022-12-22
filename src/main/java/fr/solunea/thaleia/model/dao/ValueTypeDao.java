package fr.solunea.thaleia.model.dao;

import java.util.Locale;

import fr.solunea.thaleia.model.ValueType;
import org.apache.cayenne.ObjectContext;

/**
 * <p>ValueTypeDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ValueTypeDao extends CayenneDao<ValueType> {

	public ValueTypeDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(ValueType object, Locale locale) {
		return object.getName();
	}

	/**
	 * <p>findByName.</p>
	 *
	 * @param valueTypeName a {@link java.lang.String} object.
	 * @return a {@link fr.solunea.thaleia.model.ValueType} object.
	 */
	public ValueType findByName(String valueTypeName) {
		return findFirstMatchByProperty(ValueType.NAME.getName(), valueTypeName);
	}

}
