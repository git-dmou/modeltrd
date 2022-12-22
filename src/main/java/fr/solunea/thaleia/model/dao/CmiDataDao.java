package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.CmiData;
import org.apache.cayenne.ObjectContext;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class CmiDataDao extends CayenneDao<CmiData> {

    public CmiDataDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(CmiData object, Locale locale) {
        return object.getPublication().getReference() + " - " + object.getEmail();
    }

    /**
     * Les suspend_data pour cette id de publication et cet email.
     */
    public List<CmiData> find(String publicationId, String email) {
        return findMatchByProperties(CmiData.PUBLICATION.getName(), publicationId, CmiData.EMAIL.getName(), email);
    }

}
