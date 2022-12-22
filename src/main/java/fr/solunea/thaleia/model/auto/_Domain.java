package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.model.CustomizationFile;
import fr.solunea.thaleia.model.CustomizationProperty;
import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.model.DomainRight;
import fr.solunea.thaleia.model.Plugin;
import fr.solunea.thaleia.model.User;

/**
 * Class _Domain was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Domain extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<List<Domain>> CHILDS = Property.create("childs", List.class);
    public static final Property<List<Content>> CONTENTS = Property.create("contents", List.class);
    public static final Property<List<CustomizationFile>> CUSTOMIZATION_FILES = Property.create("customizationFiles", List.class);
    public static final Property<List<CustomizationProperty>> CUSTOMIZATION_PROPERTIES = Property.create("customizationProperties", List.class);
    public static final Property<Domain> PARENT = Property.create("parent", Domain.class);
    public static final Property<List<Plugin>> PLUGINS = Property.create("plugins", List.class);
    public static final Property<List<DomainRight>> RIGHT_GIVEN_TO = Property.create("rightGivenTo", List.class);
    public static final Property<List<DomainRight>> RIGHT_ON = Property.create("rightOn", List.class);
    public static final Property<List<User>> USERS = Property.create("users", List.class);

    protected String name;

    protected Object childs;
    protected Object contents;
    protected Object customizationFiles;
    protected Object customizationProperties;
    protected Object parent;
    protected Object plugins;
    protected Object rightGivenTo;
    protected Object rightOn;
    protected Object users;

    public void setName(String name) {
        beforePropertyWrite("name", this.name, name);
        this.name = name;
    }

    public String getName() {
        beforePropertyRead("name");
        return this.name;
    }

    public void addToChilds(Domain obj) {
        addToManyTarget("childs", obj, true);
    }

    public void removeFromChilds(Domain obj) {
        removeToManyTarget("childs", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Domain> getChilds() {
        return (List<Domain>)readProperty("childs");
    }

    public void addToContents(Content obj) {
        addToManyTarget("contents", obj, true);
    }

    public void removeFromContents(Content obj) {
        removeToManyTarget("contents", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Content> getContents() {
        return (List<Content>)readProperty("contents");
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

    public void setParent(Domain parent) {
        setToOneTarget("parent", parent, true);
    }

    public Domain getParent() {
        return (Domain)readProperty("parent");
    }

    public void addToPlugins(Plugin obj) {
        addToManyTarget("plugins", obj, true);
    }

    public void removeFromPlugins(Plugin obj) {
        removeToManyTarget("plugins", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Plugin> getPlugins() {
        return (List<Plugin>)readProperty("plugins");
    }

    public void addToRightGivenTo(DomainRight obj) {
        addToManyTarget("rightGivenTo", obj, true);
    }

    public void removeFromRightGivenTo(DomainRight obj) {
        removeToManyTarget("rightGivenTo", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<DomainRight> getRightGivenTo() {
        return (List<DomainRight>)readProperty("rightGivenTo");
    }

    public void addToRightOn(DomainRight obj) {
        addToManyTarget("rightOn", obj, true);
    }

    public void removeFromRightOn(DomainRight obj) {
        removeToManyTarget("rightOn", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<DomainRight> getRightOn() {
        return (List<DomainRight>)readProperty("rightOn");
    }

    public void addToUsers(User obj) {
        addToManyTarget("users", obj, true);
    }

    public void removeFromUsers(User obj) {
        removeToManyTarget("users", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return (List<User>)readProperty("users");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "name":
                return this.name;
            case "childs":
                return this.childs;
            case "contents":
                return this.contents;
            case "customizationFiles":
                return this.customizationFiles;
            case "customizationProperties":
                return this.customizationProperties;
            case "parent":
                return this.parent;
            case "plugins":
                return this.plugins;
            case "rightGivenTo":
                return this.rightGivenTo;
            case "rightOn":
                return this.rightOn;
            case "users":
                return this.users;
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
            case "name":
                this.name = (String)val;
                break;
            case "childs":
                this.childs = val;
                break;
            case "contents":
                this.contents = val;
                break;
            case "customizationFiles":
                this.customizationFiles = val;
                break;
            case "customizationProperties":
                this.customizationProperties = val;
                break;
            case "parent":
                this.parent = val;
                break;
            case "plugins":
                this.plugins = val;
                break;
            case "rightGivenTo":
                this.rightGivenTo = val;
                break;
            case "rightOn":
                this.rightOn = val;
                break;
            case "users":
                this.users = val;
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
        out.writeObject(this.name);
        out.writeObject(this.childs);
        out.writeObject(this.contents);
        out.writeObject(this.customizationFiles);
        out.writeObject(this.customizationProperties);
        out.writeObject(this.parent);
        out.writeObject(this.plugins);
        out.writeObject(this.rightGivenTo);
        out.writeObject(this.rightOn);
        out.writeObject(this.users);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.name = (String)in.readObject();
        this.childs = in.readObject();
        this.contents = in.readObject();
        this.customizationFiles = in.readObject();
        this.customizationProperties = in.readObject();
        this.parent = in.readObject();
        this.plugins = in.readObject();
        this.rightGivenTo = in.readObject();
        this.rightOn = in.readObject();
        this.users = in.readObject();
    }

}