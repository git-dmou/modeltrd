package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.Domain;

/**
 * Class _DomainRight was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _DomainRight extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Domain> RIGHT_ON = Property.create("rightOn", Domain.class);
    public static final Property<Domain> RIGHT_OWNER = Property.create("rightOwner", Domain.class);


    protected Object rightOn;
    protected Object rightOwner;

    public void setRightOn(Domain rightOn) {
        setToOneTarget("rightOn", rightOn, true);
    }

    public Domain getRightOn() {
        return (Domain)readProperty("rightOn");
    }

    public void setRightOwner(Domain rightOwner) {
        setToOneTarget("rightOwner", rightOwner, true);
    }

    public Domain getRightOwner() {
        return (Domain)readProperty("rightOwner");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "rightOn":
                return this.rightOn;
            case "rightOwner":
                return this.rightOwner;
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
            case "rightOn":
                this.rightOn = val;
                break;
            case "rightOwner":
                this.rightOwner = val;
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
        out.writeObject(this.rightOn);
        out.writeObject(this.rightOwner);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.rightOn = in.readObject();
        this.rightOwner = in.readObject();
    }

}
