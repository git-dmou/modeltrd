package fr.solunea.thaleia.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.solunea.thaleia.model.CustomizationProperty;
import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.model.Plugin;
import org.apache.cayenne.ObjectContext;

/**
 * <p>CustomizationPropertyDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class CustomizationPropertyDao extends CayenneDao<CustomizationProperty> {

	public CustomizationPropertyDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(CustomizationProperty object, Locale locale) {
		return object.getName();
	}

	/**
	 * <p>find.</p>
	 *
	 * @param name
	 *            ne doit pas être null
	 * @param plugin
	 *            le plugin associé cette personnalisation. Si null, alors on ne
	 *            fixe pas de critères de sélection sur cette valeur.
	 * @param domain
	 *            le domaine de sécurité associé à cette personnalisation. Si
	 *            null, alors on ne fixe pas de critères de sélection sur cette
	 *            valeur.
	 * @return toutes les personnalisations qui correspondent à ces critères.
	 */
	public List<CustomizationProperty> find(String name, Plugin plugin,
			Domain domain) {
		
		List<CustomizationProperty> result = new ArrayList<>();
		
		if (name == null) {
			return result;
		}

		if (plugin == null && domain == null) {
			return findMatchByProperty(CustomizationProperty.NAME.getName(), name);
		}

		if (plugin == null && domain != null) {
			return findMatchByProperties(CustomizationProperty.NAME.getName(), name,
					CustomizationProperty.DOMAIN.getName(),
					getPK(domain));
		}

		if (plugin != null && domain == null) {
			return findMatchByProperties(CustomizationProperty.NAME.getName(), name,
					CustomizationProperty.PLUGIN.getName(),
					getPK(plugin));
		}

		return findMatchByProperties(CustomizationProperty.NAME.getName(), name, CustomizationProperty.PLUGIN.getName(),
				getPK(plugin), CustomizationProperty.DOMAIN.getName(), getPK(domain));
	}

}
