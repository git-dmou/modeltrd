package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.model.ContributeRole;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>ContributeRoleDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContributeRoleDao extends CayenneDao<ContributeRole> implements Serializable {

    public ContributeRoleDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(ContributeRole object, Locale locale) {
        return object.getUser().getLogin() + " -> " + object.getContent().getObjectId().toString() + " : " + object
                .getDescription();
    }

    public List<ContributeRole> findByUser(User user) {
        List<ContributeRole> result = new ArrayList<>();
        if (user == null) {
            return result;
        }

        try {
            SelectQuery query = new SelectQuery(ContributeRole.class, ExpressionFactory.matchExp(ContributeRole
                    .USER.getName(), user.getObjectId()));

            result = CastUtils.castList(getContext().performQuery(query), ContributeRole.class);
            return result;

        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContributeRoles de " + user + " : " + e);
        }

        return result;
    }

    public List<ContributeRole> findByContent(Content content) {
        List<ContributeRole> result = new ArrayList<>();
        if (content == null) {
            return result;
        }

        try {
            SelectQuery query = new SelectQuery(ContributeRole.class, ExpressionFactory.matchExp(ContributeRole
                    .CONTENT.getName(), content.getObjectId()));

            result = CastUtils.castList(getContext().performQuery(query), ContributeRole.class);
            return result;

        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContributeRoles pour " + content + " : " + e);
        }

        return result;
    }

    public List<ContributeRole> findByUserAndContent(User user, Content content) {
        List<ContributeRole> result = new ArrayList<>();
        if (content == null) {
            return result;
        }
        if (user == null) {
            return result;
        }

        try {
            SelectQuery query = new SelectQuery(ContributeRole.class, ExpressionFactory.matchExp(ContributeRole
                    .CONTENT.getName(), content.getObjectId()).andExp(ExpressionFactory.matchExp(ContributeRole
                    .USER.getName(), user.getObjectId())));

            result = CastUtils.castList(getContext().performQuery(query), ContributeRole.class);
            return result;

        } catch (Exception e) {
            logger.warn("Impossible de récupérer les ContributeRoles de " + user + " pour " + content + " : " + e);
        }

        return result;
    }

}
