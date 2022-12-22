package fr.solunea.thaleia.model.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fr.solunea.thaleia.model.Allocation;
import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.model.ContentVersion;
import fr.solunea.thaleia.model.User;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.map.SQLResult;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.commons.lang.StringUtils;

/**
 * <p>ContentDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentDao extends CayenneDao<Content> {

	/**
	 * <p>Constructor for ContentDao.</p>
	 *
	 * @param context le service de contexte Cayenne
	 */
	public ContentDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(Content object, Locale locale) {
		String result = Integer.toString(getPK(object));

		ContentVersion lastVersion;
		lastVersion = object.getLastVersion();
		if (lastVersion != null) {
			return lastVersion.getContentIdentifier();
		} else {
			return result;
		}
	}

	/**
	 * <p>findByName.</p>
	 *
	 * @param contentId l'identifiant
	 * @return tous les contenus portant cet identifiant
	 */
	public List<Content> findByName(String contentId) {
		return findMatchByProperty(Content.ID_PK_COLUMN, contentId);
	}

	/**
	 * <p>getModuleChilds.</p>
	 *
	 * @param version la version
	 * @return si cette version est celle d'un module, alors on renvoie la liste
	 *         des contenus de ce module, dans l'ordre de leurs positions. Sinon
	 *         on renvoie une liste vide.
	 */
	public List<Content> getModuleChilds(ContentVersion version) {
		List<Content> result = new ArrayList<>();

		if (version.getContent().getIsModule()) {
			List<Allocation> allocations = version.getChilds();
			// On trie les contenus selon leur position dans le module
			Collections.sort(allocations);
			for (Allocation allocation : allocations) {
				result.add(allocation.getChild());
			}
			logger.debug("Contenus ordonnés du module : " + result);
			return result;

		} else {
			return result;
		}
	}

	public List<String> getContentsIdForDomainsPk(List<String> domainsPK) {
		String sql = "select *\n" +
				"from content\n" +
				"         right join content_version cv on content.id = cv.content_id\n" +
				"         inner join content_property_value cpv on cv.id = cpv.content_version_id\n" +
				"where domain_id in (" + StringUtils.join(domainsPK, ",") + ")\n" +
				"  AND is_module = true\n" +
				"  AND cpv.content_property_id = 1;";

		SQLTemplate query = new SQLTemplate(User.class, sql);

		// Pour la récupération des valeurs par Cayenne
		SQLResult resultDescriptor = new SQLResult();
		resultDescriptor.addColumnResult("id");
		query.setResult(resultDescriptor);

		List<String> result = getContext().performQuery(query);


		return result;
	}

}
