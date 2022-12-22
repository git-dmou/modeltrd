package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._User;
import fr.solunea.thaleia.model.dao.LicenceHoldingDao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>User class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class User extends _User implements Serializable {

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        Date now = Calendar.getInstance().getTime();
        setPasswordCreationDate(now);
    }

    /**
     * @return true si cet utilisateur est identifié en interne, plutôt qu'avec un tiers (Google...)
     */
    public boolean isInternalSignin() {
        return getExternalSignin() == null || getExternalSignin().isEmpty();
    }

    public void onPreRemove() {
        // On veut conserver tous les licenceHoldings, mais sans les données personnelles (= le lien vers ce user)
        for (LicenceHolding licenceHolding : getLicences()) {
            // On fabrique une copie de cet objet, associé à un utilisateur anonyme, pour conserver le hash de l'email
            LicenceHolding licenceHoldingCopy = new LicenceHoldingDao(getObjectContext()).copyObject(licenceHolding);
            // On affecte la licenceHolding à personne
            licenceHoldingCopy.setUser(null);
            licenceHoldingCopy.setLicence(licenceHolding.getLicence());
        }
    }

    public String getThirdPartyServiceKey(String serviceName, String dataName) {
        return "3bab90c2-a11d-422f-3357-a628955c1ffb:fx";
    }

}
