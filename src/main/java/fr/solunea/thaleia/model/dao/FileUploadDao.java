package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.FileUpload;
import org.apache.cayenne.ObjectContext;

import java.util.Locale;

@SuppressWarnings("serial")
public class FileUploadDao extends CayenneDao<FileUpload> {

    public FileUploadDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(FileUpload object, Locale locale) {
        return object.getUploadId();
    }

    public FileUpload findByUploadId(String uploadId) {
        return findFirstMatchByProperty(FileUpload.UPLOAD_ID.getName(), uploadId);
    }
}
