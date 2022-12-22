package fr.solunea.thaleia.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.solunea.thaleia.model.CustomizationFile;
import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.model.Plugin;
import org.apache.cayenne.ObjectContext;

/**
 * <p>CustomizationFileDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class CustomizationFileDao extends CayenneDao<CustomizationFile> {

	public CustomizationFileDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(CustomizationFile object, Locale locale) {
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
	public List<CustomizationFile> find(String name, Plugin plugin,
			Domain domain) {
		
		List<CustomizationFile> result = new ArrayList<>();
		
		if (name == null) {
			return result;
		}

		if (plugin == null && domain == null) {
			return findMatchByProperty(CustomizationFile.NAME.getName(), name);
		}

		if (plugin == null && domain != null) {
			return findMatchByProperties(CustomizationFile.NAME.getName(), name,
					CustomizationFile.DOMAIN.getName(), getPK(domain));
		}

		if (plugin != null && domain == null) {
			return findMatchByProperties(CustomizationFile.NAME.getName(), name,
					CustomizationFile.PLUGIN.getName(), getPK(plugin));
		}

		return findMatchByProperties(CustomizationFile.NAME.getName(), name,
				CustomizationFile.PLUGIN.getName(), getPK(plugin),
				CustomizationFile.DOMAIN.getName(), getPK(domain));
	}

}
