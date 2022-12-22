package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.AccountRequest;
import fr.solunea.thaleia.model.User;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.ObjectSelect;

import java.util.List;
import java.util.Locale;

/**
 * <p>AccountRequestDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class AccountRequestDao extends CayenneDao<AccountRequest> {

    public AccountRequestDao(ObjectContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(AccountRequest object, Locale locale) {
        return object.getMail() + " - " + object.getRequestDate();
    }

    /**
     * @return les demandes de création de compte associées à cet utilisateur.
     */
    public List<AccountRequest> findByUser(User user) {
        return ObjectSelect.query(AccountRequest.class)
                .where(AccountRequest.CREATED_USER.eq(user))
                .select(getContext());
    }

    public List<AccountRequest> findByEmail(String email) {
        return findMatchByProperty(AccountRequest.MAIL.getName(), email);
    }

    /**
     * L'utilisateur qui est associé à ce code de validation d'email, ou null si aucun n'est trouvé.
     */
    public AccountRequest findByEmailValidationCode(String code) {
        List<AccountRequest> accountRequests = findMatchByProperty(AccountRequest.EMAIL_VALIDATION_CODE.getName(), code);
        if (accountRequests.size() > 0) {
            return accountRequests.get(0);
        } else {
            return null;
        }
    }
}
