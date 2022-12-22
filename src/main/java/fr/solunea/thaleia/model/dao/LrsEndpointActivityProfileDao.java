package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.LrsEndpointActivity;
import fr.solunea.thaleia.model.LrsEndpointActivityProfile;
import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class LrsEndpointActivityProfileDao extends CayenneDao<LrsEndpointActivityProfile> {

    public LrsEndpointActivityProfileDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(LrsEndpointActivityProfile object, Locale locale) {
        return object.getProfileId();
    }

    /**
     * @return les identifiants (Cayenne) des profils d'activité de toutes les activités de ce lrsEndpoint.
     */
    public List<Integer> findIds(LrsEndpoint lrsEndpoint) {
        List<Integer> result = new ArrayList<>();

        for (LrsEndpointActivity activity : lrsEndpoint.getActivities()) {
            for (LrsEndpointActivityProfile profile : activity.getProfiles()) {
                result.add(getPK(profile));
            }
        }

        return result;
    }

}
