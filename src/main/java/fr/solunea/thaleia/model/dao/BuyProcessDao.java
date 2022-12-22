package fr.solunea.thaleia.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import fr.solunea.thaleia.model.BuyProcess;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.CastUtils;

/**
 * <p>BuyProcessDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class BuyProcessDao extends CayenneDao<BuyProcess> {

	/**
	 * <p>Constructor for BuyProcessDao.</p>
	 *
	 * @param service a {@link fr.solunea.thaleia.model.dao.ICayenneContextService} object.
	 */
	public BuyProcessDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(BuyProcess object, Locale locale) {
		return object.getPaymentExternalId();
	}

	/**
	 * <p>listByPayer.</p>
	 *
	 * @param user l'utilisateur
	 * @return les paiements qui concernent ce payeur.
	 */
	private List<BuyProcess> listByPayer(User user) {
		return findMatchByProperty(BuyProcess.PAYER.getName(), user.getObjectId());
	}

	/**
	 * <p>listByPayer.</p>
	 *
	 * @param user l'utilisateur
	 * @param onlyExecuted
	 *            si true, alors on ne renvoie que les processus d'achat qui ont
	 *            une date d'éxecution.
	 * @return les paiements qui concernent ce payeur.
	 */
	public List<BuyProcess> listByPayer(User user, boolean onlyExecuted) {
		if (onlyExecuted) {
			try {
				SelectQuery query = new SelectQuery(BuyProcess.class,
						ExpressionFactory.matchExp(BuyProcess.PAYER.getName(), user.getObjectId())
								.andExp(ExpressionFactory.noMatchExp(BuyProcess.EXECUTION_DATE.getName(), null)));

				return CastUtils.castList(getContext().performQuery(query), BuyProcess.class);

			} catch (Exception e) {
				logger.warn("Impossible de récupérer les BuyProcess de " + user + " : " + e);
				return new ArrayList<>();
			}
		} else {
			return listByPayer(user);
		}
	}

	/**
	 * <p>findByPaymentExternalId.</p>
	 *
	 * @param id l'identifiant de paiement
	 * @return le premier objet qui porte cet identifiant de paiement externe
	 *         (par exemple : id de paiement PayPal), ou null si aucun n'a été
	 *         trouvé.
	 */
	public BuyProcess findByPaymentExternalId(String id) {

		String checkedId = id;
		if (checkedId == null) {
			checkedId = "";
		}

		try {
			SelectQuery query = new SelectQuery(BuyProcess.class,
					ExpressionFactory.matchExp(BuyProcess.PAYMENT_EXTERNAL_ID.getName(), checkedId));

			List<BuyProcess> results = CastUtils.castList(getContext().performQuery(query), BuyProcess.class);

			if (results.size() > 0) {
				return results.get(0);
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.warn("Impossible de récupérer le BuyProcess '" + checkedId + "' : " + e);
			return null;
		}
	}

	/**
	 * <p>getExecutionDateComparator.</p>
	 *
	 * @return un comparateur par date de création de la publication.
	 */
	public Comparator<BuyProcess> getExecutionDateComparator() {
		return new ExecutionDateComparator();
	}

	class ExecutionDateComparator implements Comparator<BuyProcess>, Serializable {

		public int compare(final BuyProcess o1, final BuyProcess o2) {

			if (o1 == null || o2 == null) {
				logger.debug("Comparaison impossible d'objets nuls.");
				return 0;
			}

			// Récupération des dates de publication
			Date date1 = o1.getExecutionDate();
			Date date2 = o2.getExecutionDate();

			// Comparaison n'est pas raison
			int result;
			try {
				result = date1.compareTo(date2);
			} catch (Exception e) {
				// Peut arriver : NPE
				logger.debug("Problème de comparaison ? " + e);
				result = 0;
			}

			return result;
		}
	}
}
