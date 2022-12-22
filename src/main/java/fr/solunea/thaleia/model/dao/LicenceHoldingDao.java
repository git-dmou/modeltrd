package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Licence;
import fr.solunea.thaleia.model.LicenceHolding;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.DetailedException;
import fr.solunea.thaleia.utils.Hash;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.*;

/**
 * <p>LicenceHoldingDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class LicenceHoldingDao extends CayenneDao<LicenceHolding> {

    public LicenceHoldingDao(ObjectContext context) {
        super(context);
    }

    public static String hashEmail(String email) {
        return Hash.getHash(email);
    }

    @Override
    public String getDisplayName(LicenceHolding object, Locale locale) {
        return object.getUser().getLogin() + " - "
                + object.getLicence().getName()
                + object.getStartDate().toString();
    }

    /**
     * Toutes les licencesHolding qui existent associées à cet email. On ne recherche pas dans les user.getLogin(), mais
     * dans les user_hashed_email.
     *
     * @param clearEmail   l'email en clair que l'on va hasher et rechercher dans les licences_holding.
     * @param onlyNullUser si true, alors on ne recherche les licence_holding que pour lesquelles le user est null, et non un user existant en base.
     */
    public List<LicenceHolding> getHoldedByHashedEmail(String clearEmail, boolean onlyNullUser) {
        if (clearEmail == null) {
            return new ArrayList<>();
        }

        Expression expression = ExpressionFactory.matchExp(LicenceHolding.USER_HASHED_EMAIL.getName(), hashEmail(clearEmail));
        if (onlyNullUser) {
            // Seulement celles pour user=null
            expression = expression.andExp(ExpressionFactory.matchExp(LicenceHolding.USER.getName(), null));
        }

        return performQuery(expression);
    }

    /**
     * @param user       a {@link fr.solunea.thaleia.model.User} object.
     * @param onlyActive si true, alors on ne renvoie pas celles qui ont été annulées.
     * @return Toutes les détentions de licence de cet utilisateur. ATTENTION : renvoie également les licences qui sont périmées, tant qu'elles n'ont pas été annulées.
     */
    public List<LicenceHolding> getHoldedBy(User user, boolean onlyActive) {

        if (user == null) {
            return new ArrayList<>();
        }

        Expression expression = ExpressionFactory.matchExp(
                LicenceHolding.USER.getName(), getPK(user));

        if (onlyActive) {
            // Seulement les valides
            expression = expression.andExp(ExpressionFactory.matchExp(
                    LicenceHolding.CANCELED.getName(), "0"));
        }
        return performQuery(expression);
    }

    @SuppressWarnings("unchecked")
    private List<LicenceHolding> performQuery(Expression expression) {
        SelectQuery query = new SelectQuery(LicenceHolding.class, expression);

        List<LicenceHolding> result = new ArrayList<>();
        try {
            result = getContext().performQuery(query);
        } catch (Exception e) {
            logger.warn("Erreur d'exécution de la requête : " + e
                    + LogUtils.getStackTrace(e.getStackTrace()));
        }
        return result;
    }

    /**
     * <p>getValidHoldings.</p>
     *
     * @param user a {@link fr.solunea.thaleia.model.User} object.
     * @return la liste des licences actuellement valides : vérification que la
     * date courante soit entre le début et la fin de validité de la
     * licence + attribution de licence non annulée.
     * Si le compte utilisateur a expiré (et n'est pas administrateur), alors toutes les licences sont invalidées.
     */
    public List<LicenceHolding> getValidHoldings(User user) {

        if (user == null) {
            return new ArrayList<>();
        }

        if (!user.getIsAdmin() && user.getExpiration() != null) {
            if (user.getExpiration().before(Calendar.getInstance().getTime())) {
                return new ArrayList<>();
            }
        }

        Expression expression = ExpressionFactory.matchExp(
                LicenceHolding.USER.getName(), getPK(user));

        // Seulement les valides
        expression = expression.andExp(ExpressionFactory.matchExp(
                LicenceHolding.CANCELED.getName(), "0"));

        Date now = Calendar.getInstance().getTime();
        // La date courante est dans l'intervalle start - end
        expression = expression.andExp(ExpressionFactory.lessOrEqualExp(
                LicenceHolding.START_DATE.getName(), now));
        expression = expression.andExp(ExpressionFactory.greaterOrEqualExp(
                LicenceHolding.END_DATE.getName(), now));

        return performQuery(expression);
    }

    /**
     * Procède à l'attribution de la licence portant ce SKU à l'utilisateur.
     * Les modifications sont faites dans le contexte de l'objet passé en paramètre, qui n'est pas commité.
     *
     * @param sku    le SKU de la licence à attribuer
     * @param user   la personne à qui attribuer la licence
     * @param origin l'origine de cette attribution (site vitrine, commerce, admin...)
     * @return la licence attribuée
     */
    public LicenceHolding attributeLicence(User user, String sku, String origin) throws DetailedException {
        if (user == null) {
            throw new DetailedException("User nul !");
        }

        try {
            // On récupère la licence qui correspond au SKU demandé, dans le contexte du user
            LicenceDao licenceDao = new LicenceDao(user.getObjectContext());
            Licence licence = licenceDao.findBySku(sku);
            if (licence == null) {
                throw new DetailedException("Aucune licence portant le SKU '" + sku + "' n'est disponible !");
            }

            LicenceHoldingDao licenceHoldingDao = new LicenceHoldingDao(user.getObjectContext());
            LicenceHolding licenceHolding = licenceHoldingDao.get();
            Date now = Calendar.getInstance().getTime();
            licenceHolding.setAttributionDate(now);
            licenceHolding.setStartDate(now);
            licenceHolding.setCanceled(false);
            licenceHolding.setLicence(licence);
            licenceHolding.setUser(user);
            licenceHolding.setUserHashedEmail(LicenceHoldingDao.hashEmail(user.getLogin()));
            licenceHolding.setOrigin(origin);
            long durationMillis = (long) licence.getLicenceDurationDays() * (long) 3600000 * (long) 24;
            Date endDate = new Date(now.getTime() + durationMillis);
            licenceHolding.setEndDate(endDate);
            // Les autres champs restent vides, afin qu'on utilise ceux de la licence.
            return licenceHolding;

        } catch (Exception e) {
            throw new DetailedException(e).addMessage("Impossible d'attribuer la licence " + sku);
        }
    }

}
