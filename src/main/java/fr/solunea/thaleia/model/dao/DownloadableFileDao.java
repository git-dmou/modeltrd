package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.DownloadableFile;
import fr.solunea.thaleia.model.User;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DownloadableFileDao extends CayenneDao<DownloadableFile> {

    public DownloadableFileDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(DownloadableFile object, Locale locale) {
        return "DownloadableFile reference = " + object.getReference();
    }

    public DownloadableFile findByReference(String reference) {
        return super.findFirstMatchByProperty(DownloadableFile.REFERENCE.getName(), reference);
    }

    public List<DownloadableFile> findByDownloadableBy(User user) {
        //        return findMatchByProperty(DownloadableFile.DOWNLOADABLE_BY.getName(), user.getObjectId().toString());
        List<DownloadableFile> result = new ArrayList<>();
        for (DownloadableFile downloadableFile : find()) {
            if (downloadableFile.getDownloadableBy().getObjectId().toString().equals(user.getObjectId().toString())) {
                result.add(downloadableFile);
            }
        }
        return result;
    }
}
