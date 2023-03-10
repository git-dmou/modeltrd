package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.TempDirFile;
import fr.solunea.thaleia.model.User;

/**
 * Class _TempDir was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _TempDir extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Date> CREATION_DATE = Property.create("creationDate", Date.class);
    public static final Property<String> PATH = Property.create("path", String.class);
    public static final Property<String> PUBLIC_ID = Property.create("publicId", String.class);
    public static final Property<List<TempDirFile>> FILES = Property.create("files", List.class);
    public static final Property<User> OWNER = Property.create("owner", User.class);

    protected Date creationDate;
    protected String path;
    protected String publicId;

    protected Object files;
    protected Object owner;

    public void setCreationDate(Date creationDate) {
        beforePropertyWrite("creationDate", this.creationDate, creationDate);
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        beforePropertyRead("creationDate");
        return this.creationDate;
    }

    public void setPath(String path) {
        beforePropertyWrite("path", this.path, path);
        this.path = path;
    }

    public String getPath() {
        beforePropertyRead("path");
        return this.path;
    }

    public void setPublicId(String publicId) {
        beforePropertyWrite("publicId", this.publicId, publicId);
        this.publicId = publicId;
    }

    public String getPublicId() {
        beforePropertyRead("publicId");
        return this.publicId;
    }

    public void addToFiles(TempDirFile obj) {
        addToManyTarget("files", obj, true);
    }

    public void removeFromFiles(TempDirFile obj) {
        removeToManyTarget("files", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<TempDirFile> getFiles() {
        return (List<TempDirFile>)readProperty("files");
    }

    public void setOwner(User owner) {
        setToOneTarget("owner", owner, true);
    }

    public User getOwner() {
        return (User)readProperty("owner");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "creationDate":
                return this.creationDate;
            case "path":
                return this.path;
            case "publicId":
                return this.publicId;
            case "files":
                return this.files;
            case "owner":
                return this.owner;
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
            case "creationDate":
                this.creationDate = (Date)val;
                break;
            case "path":
                this.path = (String)val;
                break;
            case "publicId":
                this.publicId = (String)val;
                break;
            case "files":
                this.files = val;
                break;
            case "owner":
                this.owner = val;
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
        out.writeObject(this.creationDate);
        out.writeObject(this.path);
        out.writeObject(this.publicId);
        out.writeObject(this.files);
        out.writeObject(this.owner);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.creationDate = (Date)in.readObject();
        this.path = (String)in.readObject();
        this.publicId = (String)in.readObject();
        this.files = in.readObject();
        this.owner = in.readObject();
    }

}
