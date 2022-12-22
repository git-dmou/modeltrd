package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.DetailedException;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.map.SQLResult;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>UserDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class UserDao extends CayenneDao<User> {


    public UserDao(ObjectContext context) {
        super(context);
    }


    public List<User> findUsersByPartialLogin(String partialLogin) {

        Expression qualifier = ExpressionFactory.likeIgnoreCaseExp(
                User.LOGIN.getName(),
                "%" + partialLogin + "%");
        SelectQuery select = new SelectQuery(User.class, qualifier);
        List users = context.performQuery(select);
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(User object, Locale locale) {
        return object.getName();
    }

    /**
     * <p>findUserByLogin.</p>
     *
     * @param login a {@link java.lang.String} object.
     * @return le compte trouvé pour ce login, ou null s'il n'existe pas.
     */
    public User findUserByLogin(String login) {
        List<User> users = findMatchByProperty(User.LOGIN.getName(), login);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * <p>findUsersByLogin.</p>
     *
     * @param login a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List<User> findUsersByLogin(String login) {
        return findMatchByProperty(User.LOGIN.getName(), login);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) throws DetailedException {
        // Non admin par défaut
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        // On l'enregistre
        super.save(user);
    }

    /**
     * @param username cet utilisateur n'est pas comptabilisé dans les jetons utilisés.
     * @return le nombre d'utilisateurs différents qui ont ouvert une session durant le mois calendaire en cours,
     * sans compter cet utilisateur, ni les admins.
     */
    public int usersLoggedThisMonth(String username) throws DetailedException {
        try {
            // Le premier jour du mois calendaire en cours
            Date firstDayOfMonth = java.sql.Date.valueOf(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));

            SelectQuery query = new SelectQuery(User.class);
            query.setQualifier(ExpressionFactory.greaterExp(User.LAST_ACCESS.getName(), firstDayOfMonth).andExp(ExpressionFactory.lessExp(User.LOGIN.getName(), username)));
            @SuppressWarnings("unchecked")
            List<User> users = context.performQuery(query);

            // On parcourt la liste pour supprimer les connexions des admins
            List<User> usersNoAdmin = new ArrayList<>();
            for (User user : users) {
                if (!user.getIsAdmin()) {
                    usersNoAdmin.add(user);
                }
            }

            return usersNoAdmin.size();

        } catch (Exception e) {
            logger.debug(LogUtils.getStackTrace(e.getStackTrace()));
            throw new DetailedException(e).addMessage(
                    "Impossible d'obtenir les utilisateurs logués dans le mois " + "courant.");
        }
    }

    public User findByPasswordResetCode(String code) {
        List<User> users = findMatchByProperty(User.RESET_PASSWORD_CODE.getName(), code);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * Retourne la liste des domaines pour lequels un utilisateur a les droits.
     * @param user Utilisateur à tester.
     * @return Liste des id de domaines.
     */
    public List<String> getDomainsIdWithRightsOn(User user) {
        UserDao dao = new UserDao(this.context);
        int userId = dao.getPK(user);

        String sql = "select domain.id as id\n" +
                "from\n" +
                "    user_account\n" +
                "    join domain on user_account.domain_id = domain.id\n" +
                "where user_account.id = " + userId + "\n" +
                "union\n" +
                "select  domain_right.domain_to_id as id\n" +
                "from\n" +
                "    user_account\n" +
                "    join domain on user_account.domain_id = domain.id\n" +
                "    join domain_right on domain.id = domain_right.domain_from_id\n" +
                "where user_account.id = " + userId + ";";

        SQLTemplate query = new SQLTemplate(User.class, sql);

        // Pour la récupération des valeurs par Cayenne
        SQLResult resultDescriptor = new SQLResult();
        resultDescriptor.addColumnResult("id");
        query.setResult(resultDescriptor);

        List<String> result = getContext().performQuery(query);


        return result;
    }
}
