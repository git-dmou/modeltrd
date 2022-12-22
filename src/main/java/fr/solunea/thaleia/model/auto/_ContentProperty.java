package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.ContentPropertyValue;
import fr.solunea.thaleia.model.ContentTypeProperty;
import fr.solunea.thaleia.model.ValueType;

/**
 * Class _ContentProperty was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _ContentProperty extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> NAME = Property.create("name", String.class);
    public static final Property<List<ContentTypeProperty>> CONTENT_TYPES = Property.create("contentTypes", List.class);
    public static final Property<ValueType> VALUE_TYPE = Property.create("valueType", ValueType.class);
    public static final Property<List<ContentPropertyValue>> VALUES = Property.create("values", List.class);

    protected String name;

    protected Object contentTypes;
    protected Object valueType;
    protected Object values;

    public void setName(String name) {
        beforePropertyWrite("name", this.name, name);
        this.name = name;
    }

    public String getName() {
        beforePropertyRead("name");
        return this.name;
    }

    public void addToContentTypes(ContentTypeProperty obj) {
        addToManyTarget("contentTypes", obj, true);
    }

    public void removeFromContentTypes(ContentTypeProperty obj) {
        removeToManyTarget("contentTypes", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<ContentTypeProperty> getContentTypes() {
        return (List<ContentTypeProperty>)readProperty("contentTypes");
    }

    public void setValueType(ValueType valueType) {
        setToOneTarget("valueType", valueType, true);
    }

    public ValueType getValueType() {
        return (ValueType)readProperty("valueType");
    }

    public void addToValues(ContentPropertyValue obj) {
        addToManyTarget("values", obj, true);
    }

    public void removeFromValues(ContentPropertyValue obj) {
        removeToManyTarget("values", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<ContentPropertyValue> getValues() {
        return (List<ContentPropertyValue>)readProperty("values");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "name":
                return this.name;
            case "contentTypes":
                return this.contentTypes;
            case "valueType":
                return this.valueType;
            case "values":
                return this.values;
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
            case "contentTypes":
                this.contentTypes = val;
                break;
            case "valueType":
                this.valueType = val;
                break;
            case "values":
                this.values = val;
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
        out.writeObject(this.contentTypes);
        out.writeObject(this.valueType);
        out.writeObject(this.values);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.name = (String)in.readObject();
        this.contentTypes = in.readObject();
        this.valueType = in.readObject();
        this.values = in.readObject();
    }

}
