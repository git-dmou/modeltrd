package fr.solunea.thaleia.model.dao;

import java.util.List;
import java.util.Locale;

import fr.solunea.thaleia.model.Plugin;
import org.apache.cayenne.ObjectContext;

/**
 * <p>PluginDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class PluginDao extends CayenneDao<Plugin> {

	/**
	 * <p>Constructor for PluginDao.</p>
	 *
	 * @param service a {@link fr.solunea.thaleia.model.dao.ICayenneContextService} object.
	 */
	public PluginDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(Plugin object, Locale locale) {
		return object.getName();
	}

	/**
	 * <p>findByName.</p>
	 *
	 * @param pluginClassName a {@link java.lang.String} object.
	 * @return tous les plugins portant ce nom.
	 */
	public List<Plugin> findByName(String pluginClassName) {
		return findMatchByProperty(Plugin.NAME.getName(), pluginClassName);
	}

}
