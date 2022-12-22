package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.model.ContentVersion;
import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.utils.CastUtils;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>ContentVersionDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentVersionDao extends CayenneDao<ContentVersion> {

    public ContentVersionDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(ContentVersion object, Locale locale) {
        // Le numéro de version, ou sinon une espace.
        return object.getRevisionNote() != null ? object.getRevisionNote() : " ";
    }

    /**
     * <p>findLastVersionByName.</p>
     *
     * @param name   a {@link java.lang.String} object.
     * @param domain a {@link fr.solunea.thaleia.model.Domain} object.
     * @return toute les dernières versions de contenus dont le nom est name, et dont le domaine de sécurité est
     * celui-ci
     */
    public List<ContentVersion> findLastVersionByName(String name, Domain domain) {

        String checkedName = name;
        if (checkedName == null) {
            checkedName = "";
        }

        try {
            String sql = "SELECT *\n" + "FROM content_version x\n" + "       INNER JOIN (SELECT p.content_id,\n"
                    + "                          MAX(revision_number) AS max_rev\n"
                    + "                   FROM content_version p\n"
                    + "                   GROUP BY p.content_id) y ON y.content_id = x.content_id\n"
                    + "       INNER JOIN content c on x.content_id = c.id\n" + "  AND y.max_rev = x.revision_number\n"
                    + "WHERE x.content_identifier = '$name'\n" + "  AND c.domain_id = $domainId;";

            SQLTemplate query = new SQLTemplate(ContentVersion.class, sql);

            // On fixe la valeur des paramètres dans la requête
            Map<String, String> params = new HashMap<>();
            params.put("name", checkedName);
            params.put("domainId", Integer.toString(getPK(domain)));
            query.setParameters(params);

            return CastUtils.castList(domain.getObjectContext().performQuery(query), ContentVersion.class);

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    /**
     * <p>findVersion.</p>
     *
     * @param content        le contenu
     * @param revisionNumber le numéro de révision
     * @return la version de contenu qui porte ce numéro de version, ou null si aucune n'a été trouvée.
     */
    public ContentVersion findVersion(Content content, int revisionNumber) {

        if (content == null) {
            return null;
        }

        if (revisionNumber < ContentVersion.FIRST_VERSION_NUMBER) {
            return null;
        }

        try {
            SelectQuery query = new SelectQuery(ContentVersion.class,
                    ExpressionFactory.matchExp(ContentVersion.CONTENT.getName(), content.getObjectId()).andExp(ExpressionFactory.matchExp(ContentVersion.REVISION_NUMBER.getName(), revisionNumber)));

            List<ContentVersion> results = CastUtils.castList(content.getObjectContext().performQuery(query), ContentVersion.class);
            if (results.size() > 0) {
                return results.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.warn("Impossible de récupérer la contentVersion " + revisionNumber + " de " + content + " : " + e);
            return null;
        }
    }

}
