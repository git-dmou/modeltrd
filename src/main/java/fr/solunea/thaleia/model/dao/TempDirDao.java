package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.TempDir;
import fr.solunea.thaleia.model.User;
import org.apache.cayenne.ObjectContext;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
public class TempDirDao extends CayenneDao<TempDir> {

    public TempDirDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(TempDir object, Locale locale) {
        return object.getPublicId();
    }

    public TempDir findByPublicId(String publicId) {
        return findFirstMatchByProperty(TempDir.PUBLIC_ID.getName(), publicId);
    }

    public List<TempDir> findByOwner(User user) {
        UserDao userDao = new UserDao(context);
        return findMatchByProperty(TempDir.OWNER.getName(), userDao.getPK(user));
    }
}
