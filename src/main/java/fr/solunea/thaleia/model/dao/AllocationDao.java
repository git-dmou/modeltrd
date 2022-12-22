package fr.solunea.thaleia.model.dao;

import java.util.Locale;

import fr.solunea.thaleia.model.Allocation;
import org.apache.cayenne.ObjectContext;

/**
 * <p>AllocationDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class AllocationDao extends CayenneDao<Allocation> {

	/**
	 * <p>Constructor for AllocationDao.</p>
	 *
	 * @param contextService a {@link fr.solunea.thaleia.model.dao.ICayenneContextService} object.
	 */
	public AllocationDao(ObjectContext context) {
		super(context);
	}

	/**
	 * Le numéro de la première allocation
	 */
	public static final int FIRST_ALLOCATION_NUMBER = 1;

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(Allocation object, Locale locale) {
		return object.toString();
	}

}
