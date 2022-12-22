package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.MailTemplate;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>MailTemplateDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class MailTemplateDao extends CayenneDao<MailTemplate> {

    public MailTemplateDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(MailTemplate object, Locale locale) {
        return object.getName();
    }

    /**
     * <p>findByName.</p>
     *
     * @param templateName a {@link java.lang.String} object.
     * @param locale       a {@link java.util.Locale} object.
     * @return a {@link fr.solunea.thaleia.model.MailTemplate} object.
     */
    public MailTemplate findByName(String templateName, Locale locale) {
        List<MailTemplate> list;
        try {
            // On effectue deux recherches : la première avec le nom de la
            // locale, puis si on ne trouve rien on refait avec le nom des deux
            // premiers cararactères de la locale. Ainsi, si on reçoit FR_fr, et
            // que le template est sotcké à "fr", on trouve au deuxième essai.

            logger.debug("Recherche du template '" + templateName + "' pour la locale '" + locale.toString() + "'");
            list = findByName(templateName, locale.toString());

            if (list.isEmpty()) {
                logger.debug("Recherche du template '" + templateName + "' pour la locale '"
                        + locale.toString().substring(0, 2) + "'");
                list = findByName(templateName, locale.toString().substring(0, 2));
            }

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return null;
        }

        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private List<MailTemplate> findByName(String templateName, String locale) throws Exception {
        final Expression template = ExpressionFactory.exp("locale = $locale").andExp(ExpressionFactory.exp("name = $name"));
        SelectQuery query;
        Map<String, Object> params = new HashMap<>();
        params.put("locale", locale);
        params.put("name", templateName);
        query = new SelectQuery(MailTemplate.class, template.params(params));
        return CastUtils.castList(getContext().performQuery(query), dataObjectClass);
    }

}
