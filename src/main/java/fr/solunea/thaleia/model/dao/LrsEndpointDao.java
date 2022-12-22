package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LrsEndpoint;
import org.apache.cayenne.ObjectContext;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class LrsEndpointDao extends CayenneDao<LrsEndpoint> implements Serializable {

    public LrsEndpointDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(LrsEndpoint object, Locale locale) {
        return object.getName();
    }

    public List<LrsEndpoint> findByUser(String login, String password) {
        return findMatchByProperties(LrsEndpoint.USERNAME.getName(), login, LrsEndpoint.PASSWORD.getName(), password);
    }
}
