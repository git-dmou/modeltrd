package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._Publication;

import java.io.Serializable;

/**
 * <p>Publication class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Publication extends _Publication implements Serializable {

    /**
     * <p>hasForRecipient.</p>
     *
     * @param email l'email
     * @return true si la publication a cet email parmi ses inscriptions
     */
    public boolean hasForRecipient(String email) {
        for (PublicationRecipient recipient : getRecipients()) {
            if (recipient != null) {
                if (recipient.getEmail().equals(email)) {
                    return true;
                }
            }
        }
        return false;
    }

}
