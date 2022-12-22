package fr.solunea.thaleia.model.dao;

import java.util.Locale;

import fr.solunea.thaleia.model.PublicationRecipient;
import org.apache.cayenne.ObjectContext;

/**
 * <p>PublicationRecipientDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class PublicationRecipientDao extends CayenneDao<PublicationRecipient> {

	public PublicationRecipientDao(ObjectContext context) {
		super(context);
	}

	/** {@inheritDoc} */
	@Override
	public String getDisplayName(PublicationRecipient object, Locale locale) {
		return object.getEmail();
	}

}
