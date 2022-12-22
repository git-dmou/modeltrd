package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.TempDir;
import fr.solunea.thaleia.model.TempDirFile;
import org.apache.cayenne.ObjectContext;

import java.util.Locale;

@SuppressWarnings("serial")
public class TempDirFileDao extends CayenneDao<TempDirFile> {

    public TempDirFileDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(TempDirFile object, Locale locale) {
        return object.getPublicId();
    }

    public TempDirFile findByPublicId(String publicId) {
        return findFirstMatchByProperty(TempDirFile.PUBLIC_ID.getName(), publicId);
    }
}
