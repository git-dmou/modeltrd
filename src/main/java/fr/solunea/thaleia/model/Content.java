package fr.solunea.thaleia.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import fr.solunea.thaleia.model.auto._Content;

/**
 * <p>Content class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Content extends _Content implements Serializable {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Content.class);

	/**
	 * <p>getLastVersion.</p>
	 *
	 * @return la dernière version de ce contenu, ou null si elle n'existe pas.
	 */
	public ContentVersion getLastVersion() {

		// Si pas encore de version, on renvoie nul
		if (getVersions() == null || getVersions().size() == 0) {
			return null;
		}

		// On ne passe pas par le code suivant, car si l'objet est "NEW", ce
		// code déclenche une exception.

		// // On cherche la révision la plus grande pour ce contenu
		// try {
		// final Expression expression = ExpressionFactory.matchExp(
		// ContentVersion.CONTENT_PROPERTY, this);
		// SelectQuery query;
		// query = new SelectQuery(ContentVersion.class, expression);
		// query.addOrdering(ContentVersion.REVISION_NUMBER_PROPERTY,
		// SortOrder.DESCENDING);
		// List<ContentVersion> result = CastUtils
		// .castList(
		// CayenneUtils.getDataContext().performQuery(query),
		// ContentVersion.class);
		// return result.get(0);
		//
		// } catch (Exception e) {
		// String message =
		// "Impossible de retrouver la dernière version d'un contenu : "
		// + e;
		// logger.warn(message);
		// throw new DetailedException(message);
		// }

		List<ContentVersion> versions = getVersions();

		// logger.debug("Les versions sont les suivantes : ");
		// for(int i=0 ; i<versions.size(); i++) {
		// logger.debug("version : " + versions.get(i).getContentIdentifier());
		// }

		Collections.sort(versions);
		// La dernière des versions triées est :
		return versions.get(versions.size() - 1);
	}

	/**
	 * <p>getVersion.</p>
	 *
	 * @param revisionNumber le numéro de révision.
	 * @return la version correspondant à ce numéro, ou null si aucune n'existe
	 *         pour ce nuémro.
	 */
	public ContentVersion getVersion(Integer revisionNumber) {

		// Logs
		// String log = "";
		// for (ContentVersion contentVersion : getVersions()) {
		// log = log + "/" + contentVersion.getRevisionNumber() + "="
		// + contentVersion.getContentIdentifier();
		// }
		// logger.debug("Recherche de la version " + revisionNumber + " dans "
		// + log);

		// On parcourt toutes les versions du contenu
		for (ContentVersion contentVersion : getVersions()) {
			if (revisionNumber.equals(contentVersion.getRevisionNumber())) {
				return contentVersion;
			}
		}

		// Par défaut, null
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getIsModule() {
		if (super.getIsModule() == null) {
			return false;
		} else {
			return super.getIsModule();
		}
	}
}
