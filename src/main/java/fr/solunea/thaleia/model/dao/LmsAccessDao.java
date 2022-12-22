package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.LmsAccess;
import org.apache.cayenne.ObjectContext;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class LmsAccessDao extends CayenneDao<LmsAccess> {

    public LmsAccessDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(LmsAccess object, Locale locale) {
        return object.getLmsServiceImplementation() + " - " + object.getUser().getLogin();
    }

    public List<LmsAccess> findByUserId(int userId) {
        return super.findMatchByProperty(LmsAccess.USER.getName(), userId);
    }

}
