package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.LrsEndpointPersona;
import org.apache.cayenne.ObjectContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LrsEndPointPersonaDao extends CayenneDao<LrsEndpointPersona> implements Serializable {

    public LrsEndPointPersonaDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(LrsEndpointPersona object, Locale locale) {
        return object.getPersona();
    }

    public List<Integer> findIds(LrsEndpoint lrsEndpoint) {
        List<Integer> result = new ArrayList<>();

        for (LrsEndpointPersona persona : lrsEndpoint.getPersonas()) {
            result.add(getPK(persona));
        }

        return result;
    }
}
