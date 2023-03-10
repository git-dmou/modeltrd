package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.model.Plugin;

/**
 * Class _CustomizationProperty was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _CustomizationProperty extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<String> VALUE = Property.create("value", String.class);
    public static final Property<Domain> DOMAIN = Property.create("domain", Domain.class);
    public static final Property<Plugin> PLUGIN = Property.create("plugin", Plugin.class);

    protected String name;
    protected String value;

    protected Object domain;
    protected Object plugin;

    public void setName(String name) {
        beforePropertyWrite("name", this.name, name);
        this.name = name;
    }

    public String getName() {
        beforePropertyRead("name");
        return this.name;
    }

    public void setValue(String value) {
        beforePropertyWrite("value", this.value, value);
        this.value = value;
    }

    public String getValue() {
        beforePropertyRead("value");
        return this.value;
    }

    public void setDomain(Domain domain) {
        setToOneTarget("domain", domain, true);
    }

    public Domain getDomain() {
        return (Domain)readProperty("domain");
    }

    public void setPlugin(Plugin plugin) {
        setToOneTarget("plugin", plugin, true);
    }

    public Plugin getPlugin() {
        return (Plugin)readProperty("plugin");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "name":
                return this.name;
            case "value":
                return this.value;
            case "domain":
                return this.domain;
            case "plugin":
                return this.plugin;
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
            case "value":
                this.value = (String)val;
                break;
            case "domain":
                this.domain = val;
                break;
            case "plugin":
                this.plugin = val;
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
        out.writeObject(this.value);
        out.writeObject(this.domain);
        out.writeObject(this.plugin);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.name = (String)in.readObject();
        this.value = (String)in.readObject();
        this.domain = in.readObject();
        this.plugin = in.readObject();
    }

}
