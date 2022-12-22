package fr.solunea.thaleia.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.solunea.thaleia.model.auto._ContentType;

/**
 * <p>ContentType class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentType extends _ContentType implements Serializable {

	/**
	 * <p>getContentProperties.</p>
	 *
	 * @return la liste des ContentProperty auxquelles est associé ce
	 *         ContentType
	 */
	public List<ContentProperty> getContentProperties() {
		List<ContentProperty> result = new ArrayList<>();
		for (ContentTypeProperty property : this.getProperties()) {
			result.add(property.getContentProperty());
		}
		return result;
	}

	/**
	 * <p>getVisibleProperties.</p>
	 *
	 * @param user l'utilisateur concerné.
	 * @return les ContentTypeProperty visibles pour cet utilisateur : toutes si
	 *         admin, sinon toutes celles qui ne sont pas invisibles.
	 */
	public List<ContentTypeProperty> getVisibleProperties(User user) {

		// Si admin, on renvoie tout
		if (user.getIsAdmin()) {
			return super.getProperties();
		}

		List<ContentTypeProperty> result = new ArrayList<>();
		for (ContentTypeProperty contentTypeProperty : super.getProperties()) {
			// On l'ajoute au résultat si elle est visible
			if (!contentTypeProperty.getHidden()) {
				result.add(contentTypeProperty);
			}
		}
		return result;
	}

}
