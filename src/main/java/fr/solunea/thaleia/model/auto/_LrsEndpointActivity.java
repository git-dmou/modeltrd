package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.LrsEndpointActivityProfile;

/**
 * Class _LrsEndpointActivity was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _LrsEndpointActivity extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> ACTIVITY_ID = Property.create("activityId", String.class);
    public static final Property<String> JSON_DATA = Property.create("jsonData", String.class);
    public static final Property<LrsEndpoint> LRS_ENDPOINT = Property.create("lrsEndpoint", LrsEndpoint.class);
    public static final Property<List<LrsEndpointActivityProfile>> PROFILES = Property.create("profiles", List.class);

    protected String activityId;
    protected String jsonData;

    protected Object lrsEndpoint;
    protected Object profiles;

    public void setActivityId(String activityId) {
        beforePropertyWrite("activityId", this.activityId, activityId);
        this.activityId = activityId;
    }

    public String getActivityId() {
        beforePropertyRead("activityId");
        return this.activityId;
    }

    public void setJsonData(String jsonData) {
        beforePropertyWrite("jsonData", this.jsonData, jsonData);
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        beforePropertyRead("jsonData");
        return this.jsonData;
    }

    public void setLrsEndpoint(LrsEndpoint lrsEndpoint) {
        setToOneTarget("lrsEndpoint", lrsEndpoint, true);
    }

    public LrsEndpoint getLrsEndpoint() {
        return (LrsEndpoint)readProperty("lrsEndpoint");
    }

    public void addToProfiles(LrsEndpointActivityProfile obj) {
        addToManyTarget("profiles", obj, true);
    }

    public void removeFromProfiles(LrsEndpointActivityProfile obj) {
        removeToManyTarget("profiles", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<LrsEndpointActivityProfile> getProfiles() {
        return (List<LrsEndpointActivityProfile>)readProperty("profiles");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "activityId":
                return this.activityId;
            case "jsonData":
                return this.jsonData;
            case "lrsEndpoint":
                return this.lrsEndpoint;
            case "profiles":
                return this.profiles;
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
            case "activityId":
                this.activityId = (String)val;
                break;
            case "jsonData":
                this.jsonData = (String)val;
                break;
            case "lrsEndpoint":
                this.lrsEndpoint = val;
                break;
            case "profiles":
                this.profiles = val;
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
        out.writeObject(this.activityId);
        out.writeObject(this.jsonData);
        out.writeObject(this.lrsEndpoint);
        out.writeObject(this.profiles);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.activityId = (String)in.readObject();
        this.jsonData = (String)in.readObject();
        this.lrsEndpoint = in.readObject();
        this.profiles = in.readObject();
    }

}
