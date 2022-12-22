package fr.solunea.thaleia.model.dao;

import java.util.List;
import java.util.Locale;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import fr.solunea.thaleia.model.Licence;
import fr.solunea.thaleia.utils.CastUtils;

/**
 * <p>LicenceDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class LicenceDao extends CayenneDao<Licence> {

	/**
	 * <p>Constructor for LicenceDao.</p>
	 *
	 */
	public LicenceDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(Licence object, Locale locale) {
		return object.getName();
	}

	/**
	 * <p>findBySku.</p>
	 *
	 * @param sku le sku
	 * @return la première licence trouvée qui porte ce sku, ou null si aucune
	 *         n'a été trouvée.
	 */
	public Licence findBySku(String sku) {
		List<Licence> result = findMatchByProperty(Licence.SKU.getName(), sku);
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * <p>findByName.</p>
	 *
	 * @param name le nom
	 * @return la première Licence trouvée qui porte ce nom, ou null si elle n'a
	 *         pas été trouvée.
	 */
	public Licence findByName(String name) {

		String checkedName = name;
		if (checkedName == null) {
			checkedName = "";
		}

		try {
			SelectQuery query = new SelectQuery(Licence.class,
					ExpressionFactory.matchExp(Licence.NAME.getName(), checkedName));

			List<Licence> licences = CastUtils.castList(getContext().performQuery(query), Licence.class);

			if (licences.size() > 0) {
				return licences.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.warn("Impossible de récupérer la Licence '" + checkedName + "' : " + e);
			return null;
		}
	}
}
