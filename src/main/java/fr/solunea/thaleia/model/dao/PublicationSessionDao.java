package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.PublicationSession;
import org.apache.cayenne.ObjectContext;

import java.util.Calendar;
import java.util.Locale;

/**
 * <p>BuyProcessDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class PublicationSessionDao extends CayenneDao<PublicationSession> {

    public PublicationSessionDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(PublicationSession object, Locale locale) {
        return object.getUsername() + " -> " + object.getPublication().getReference();
    }

    public PublicationSession findByToken(String token) {
        return findFirstMatchByProperty(PublicationSession.TOKEN.getName(), token);
    }
}
