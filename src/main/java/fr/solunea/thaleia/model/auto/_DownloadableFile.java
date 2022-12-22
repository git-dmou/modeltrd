package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.User;

/**
 * Class _DownloadableFile was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _DownloadableFile extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> DESCRIPTION = Property.create("description", String.class);
    public static final Property<String> FILENAME = Property.create("filename", String.class);
    public static final Property<Date> GENERATION_DATE = Property.create("generationDate", Date.class);
    public static final Property<Date> LAST_DOWNLOAD = Property.create("lastDownload", Date.class);
    public static final Property<Boolean> NOTIFY_AUTHOR = Property.create("notifyAuthor", Boolean.class);
    public static final Property<String> REFERENCE = Property.create("reference", String.class);
    public static final Property<User> AUTHOR = Property.create("author", User.class);
    public static final Property<User> DOWNLOADABLE_BY = Property.create("downloadableBy", User.class);

    protected String description;
    protected String filename;
    protected Date generationDate;
    protected Date lastDownload;
    protected Boolean notifyAuthor;
    protected String reference;

    protected Object author;
    protected Object downloadableBy;

    public void setDescription(String description) {
        beforePropertyWrite("description", this.description, description);
        this.description = description;
    }

    public String getDescription() {
        beforePropertyRead("description");
        return this.description;
    }

    public void setFilename(String filename) {
        beforePropertyWrite("filename", this.filename, filename);
        this.filename = filename;
    }

    public String getFilename() {
        beforePropertyRead("filename");
        return this.filename;
    }

    public void setGenerationDate(Date generationDate) {
        beforePropertyWrite("generationDate", this.generationDate, generationDate);
        this.generationDate = generationDate;
    }

    public Date getGenerationDate() {
        beforePropertyRead("generationDate");
        return this.generationDate;
    }

    public void setLastDownload(Date lastDownload) {
        beforePropertyWrite("lastDownload", this.lastDownload, lastDownload);
        this.lastDownload = lastDownload;
    }

    public Date getLastDownload() {
        beforePropertyRead("lastDownload");
        return this.lastDownload;
    }

    public void setNotifyAuthor(Boolean notifyAuthor) {
        beforePropertyWrite("notifyAuthor", this.notifyAuthor, notifyAuthor);
        this.notifyAuthor = notifyAuthor;
    }

    public Boolean getNotifyAuthor() {
        beforePropertyRead("notifyAuthor");
        return this.notifyAuthor;
    }

    public void setReference(String reference) {
        beforePropertyWrite("reference", this.reference, reference);
        this.reference = reference;
    }

    public String getReference() {
        beforePropertyRead("reference");
        return this.reference;
    }

    public void setAuthor(User author) {
        setToOneTarget("author", author, true);
    }

    public User getAuthor() {
        return (User)readProperty("author");
    }

    public void setDownloadableBy(User downloadableBy) {
        setToOneTarget("downloadableBy", downloadableBy, true);
    }

    public User getDownloadableBy() {
        return (User)readProperty("downloadableBy");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "description":
                return this.description;
            case "filename":
                return this.filename;
            case "generationDate":
                return this.generationDate;
            case "lastDownload":
                return this.lastDownload;
            case "notifyAuthor":
                return this.notifyAuthor;
            case "reference":
                return this.reference;
            case "author":
                return this.author;
            case "downloadableBy":
                return this.downloadableBy;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "description":
                this.description = (String)val;
                break;
            case "filename":
                this.filename = (String)val;
                break;
            case "generationDate":
                this.generationDate = (Date)val;
                break;
            case "lastDownload":
                this.lastDownload = (Date)val;
                break;
            case "notifyAuthor":
                this.notifyAuthor = (Boolean)val;
                break;
            case "reference":
                this.reference = (String)val;
                break;
            case "author":
                this.author = val;
                break;
            case "downloadableBy":
                this.downloadableBy = val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.description);
        out.writeObject(this.filename);
        out.writeObject(this.generationDate);
        out.writeObject(this.lastDownload);
        out.writeObject(this.notifyAuthor);
        out.writeObject(this.reference);
        out.writeObject(this.author);
        out.writeObject(this.downloadableBy);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.description = (String)in.readObject();
        this.filename = (String)in.readObject();
        this.generationDate = (Date)in.readObject();
        this.lastDownload = (Date)in.readObject();
        this.notifyAuthor = (Boolean)in.readObject();
        this.reference = (String)in.readObject();
        this.author = in.readObject();
        this.downloadableBy = in.readObject();
    }

}