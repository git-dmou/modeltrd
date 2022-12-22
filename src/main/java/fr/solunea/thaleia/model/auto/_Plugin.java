package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.CustomizationFile;
import fr.solunea.thaleia.model.CustomizationProperty;
import fr.solunea.thaleia.model.Domain;

/**
 * Class _Plugin was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Plugin extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> FILENAME = Property.create("filename", String.class);
    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<List<CustomizationFile>> CUSTOMIZATION_FILES = Property.create("customizationFiles", List.class);
    public static final Property<List<CustomizationProperty>> CUSTOMIZATION_PROPERTIES = Property.create("customizationProperties", List.class);
    public static final Property<Domain> DOMAIN = Property.create("domain", Domain.class);

    protected String filename;
    protected String name;

    protected Object customizationFiles;
    protected Object customizationProperties;
    protected Object domain;

    public void setFilename(String filename) {
        beforePropertyWrite("filename", this.filename, filename);
        this.filename = filename;
    }

    public String getFilename() {
        beforePropertyRead("filename");
        return this.filename;
    }

    public void setName(String name) {
        beforePropertyWrite("name", this.name, name);
        this.name = name;
    }

    public String getName() {
        beforePropertyRead("name");
        return this.name;
    }

    public void addToCustomizationFiles(CustomizationFile obj) {
        addToManyTarget("customizationFiles", obj, true);
    }

    public void removeFromCustomizationFiles(CustomizationFile obj) {
        removeToManyTarget("customizationFiles", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<CustomizationFile> getCustomizationFiles() {
        return (List<CustomizationFile>)readProperty("customizationFiles");
    }

    public void addToCustomizationProperties(CustomizationProperty obj) {
        addToManyTarget("customizationProperties", obj, true);
    }

    public void removeFromCustomizationProperties(CustomizationProperty obj) {
        removeToManyTarget("customizationProperties", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<CustomizationProperty> getCustomizationProperties() {
        return (List<CustomizationProperty>)readProperty("customizationProperties");
    }

    public void setDomain(Domain domain) {
        setToOneTarget("domain", domain, true);
    }

    public Domain getDomain() {
        return (Domain)readProperty("domain");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "filename":
                return this.filename;
            case "name":
                return this.name;
            case "customizationFiles":
                return this.customizationFiles;
            case "customizationProperties":
                return this.customizationProperties;
            case "domain":
                return this.domain;
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
            case "filename":
                this.filename = (String)val;
                break;
            case "name":
                this.name = (String)val;
                break;
            case "customizationFiles":
                this.customizationFiles = val;
                break;
            case "customizationProperties":
                this.customizationProperties = val;
                break;
            case "domain":
                this.domain = val;
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
        out.writeObject(this.filename);
        out.writeObject(this.name);
        out.writeObject(this.customizationFiles);
        out.writeObject(this.customizationProperties);
        out.writeObject(this.domain);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.filename = (String)in.readObject();
        this.name = (String)in.readObject();
        this.customizationFiles = in.readObject();
        this.customizationProperties = in.readObject();
        this.domain = in.readObject();
    }

}