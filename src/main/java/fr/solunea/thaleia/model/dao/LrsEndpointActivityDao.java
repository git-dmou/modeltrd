package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.LrsEndpointActivity;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class LrsEndpointActivityDao extends CayenneDao<LrsEndpointActivity> {

    public LrsEndpointActivityDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(LrsEndpointActivity object, Locale locale) {
        return object.getActivityId();
    }

    public LrsEndpointActivity findByActivityId(String activityId) {
        return findFirstMatchByProperty(LrsEndpointActivity.ACTIVITY_ID.getName(), activityId);
    }

    /**
     * @return les identifiants (Cayenne) des activités de ce lrsEndpoint.
     */
    public List<Integer> findIds(LrsEndpoint lrsEndpoint) {
        List<Integer> result = new ArrayList<>();

        for (LrsEndpointActivity activity : lrsEndpoint.getActivities()) {
            result.add(getPK(activity));
        }

        return result;
    }
}
