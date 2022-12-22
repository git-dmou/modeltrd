package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.TempDir;

/**
 * Class _TempDirFile was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _TempDirFile extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";
    public static final String TEMP_DIR_ID_PK_COLUMN = "temp_dir_id";

    public static final Property<String> FILE_NAME = Property.create("fileName", String.class);
    public static final Property<String> PUBLIC_ID = Property.create("publicId", String.class);
    public static final Property<Long> SIZE = Property.create("size", Long.class);
    public static final Property<TempDir> TEMP_DIR = Property.create("tempDir", TempDir.class);

    protected String fileName;
    protected String publicId;
    protected Long size;

    protected Object tempDir;

    public void setFileName(String fileName) {
        beforePropertyWrite("fileName", this.fileName, fileName);
        this.fileName = fileName;
    }

    public String getFileName() {
        beforePropertyRead("fileName");
        return this.fileName;
    }

    public void setPublicId(String publicId) {
        beforePropertyWrite("publicId", this.publicId, publicId);
        this.publicId = publicId;
    }

    public String getPublicId() {
        beforePropertyRead("publicId");
        return this.publicId;
    }

    public void setSize(Long size) {
        beforePropertyWrite("size", this.size, size);
        this.size = size;
    }

    public Long getSize() {
        beforePropertyRead("size");
        return this.size;
    }

    public void setTempDir(TempDir tempDir) {
        setToOneTarget("tempDir", tempDir, true);
    }

    public TempDir getTempDir() {
        return (TempDir)readProperty("tempDir");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "fileName":
                return this.fileName;
            case "publicId":
                return this.publicId;
            case "size":
                return this.size;
            case "tempDir":
                return this.tempDir;
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
            case "fileName":
                this.fileName = (String)val;
                break;
            case "publicId":
                this.publicId = (String)val;
                break;
            case "size":
                this.size = (Long)val;
                break;
            case "tempDir":
                this.tempDir = val;
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
        out.writeObject(this.fileName);
        out.writeObject(this.publicId);
        out.writeObject(this.size);
        out.writeObject(this.tempDir);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.fileName = (String)in.readObject();
        this.publicId = (String)in.readObject();
        this.size = (Long)in.readObject();
        this.tempDir = in.readObject();
    }

}