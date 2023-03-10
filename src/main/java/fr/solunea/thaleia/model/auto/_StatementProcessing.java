package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.LrsEndpoint;
import fr.solunea.thaleia.model.User;

/**
 * Class _StatementProcessing was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _StatementProcessing extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> DESCRIPTION = Property.create("description", String.class);
    public static final Property<String> DESTINATION_LOGIN = Property.create("destinationLogin", String.class);
    public static final Property<String> DESTINATION_PASSWORD = Property.create("destinationPassword", String.class);
    public static final Property<String> DESTINATION_URL = Property.create("destinationUrl", String.class);
    public static final Property<String> FILTER = Property.create("filter", String.class);
    public static final Property<String> PROCESSING = Property.create("processing", String.class);
    public static final Property<Boolean> STORE_RAW_RESULT = Property.create("storeRawResult", Boolean.class);
    public static final Property<Boolean> STORE_RESULT = Property.create("storeResult", Boolean.class);
    public static final Property<User> OWNER = Property.create("owner", User.class);
    public static final Property<LrsEndpoint> SOURCE_LRS = Property.create("sourceLrs", LrsEndpoint.class);

    protected String description;
    protected String destinationLogin;
    protected String destinationPassword;
    protected String destinationUrl;
    protected String filter;
    protected String processing;
    protected Boolean storeRawResult;
    protected Boolean storeResult;

    protected Object owner;
    protected Object sourceLrs;

    public void setDescription(String description) {
        beforePropertyWrite("description", this.description, description);
        this.description = description;
    }

    public String getDescription() {
        beforePropertyRead("description");
        return this.description;
    }

    public void setDestinationLogin(String destinationLogin) {
        beforePropertyWrite("destinationLogin", this.destinationLogin, destinationLogin);
        this.destinationLogin = destinationLogin;
    }

    public String getDestinationLogin() {
        beforePropertyRead("destinationLogin");
        return this.destinationLogin;
    }

    public void setDestinationPassword(String destinationPassword) {
        beforePropertyWrite("destinationPassword", this.destinationPassword, destinationPassword);
        this.destinationPassword = destinationPassword;
    }

    public String getDestinationPassword() {
        beforePropertyRead("destinationPassword");
        return this.destinationPassword;
    }

    public void setDestinationUrl(String destinationUrl) {
        beforePropertyWrite("destinationUrl", this.destinationUrl, destinationUrl);
        this.destinationUrl = destinationUrl;
    }

    public String getDestinationUrl() {
        beforePropertyRead("destinationUrl");
        return this.destinationUrl;
    }

    public void setFilter(String filter) {
        beforePropertyWrite("filter", this.filter, filter);
        this.filter = filter;
    }

    public String getFilter() {
        beforePropertyRead("filter");
        return this.filter;
    }

    public void setProcessing(String processing) {
        beforePropertyWrite("processing", this.processing, processing);
        this.processing = processing;
    }

    public String getProcessing() {
        beforePropertyRead("processing");
        return this.processing;
    }

    public void setStoreRawResult(Boolean storeRawResult) {
        beforePropertyWrite("storeRawResult", this.storeRawResult, storeRawResult);
        this.storeRawResult = storeRawResult;
    }

    public Boolean getStoreRawResult() {
        beforePropertyRead("storeRawResult");
        return this.storeRawResult;
    }

    public void setStoreResult(Boolean storeResult) {
        beforePropertyWrite("storeResult", this.storeResult, storeResult);
        this.storeResult = storeResult;
    }

    public Boolean getStoreResult() {
        beforePropertyRead("storeResult");
        return this.storeResult;
    }

    public void setOwner(User owner) {
        setToOneTarget("owner", owner, true);
    }

    public User getOwner() {
        return (User)readProperty("owner");
    }

    public void setSourceLrs(LrsEndpoint sourceLrs) {
        setToOneTarget("sourceLrs", sourceLrs, true);
    }

    public LrsEndpoint getSourceLrs() {
        return (LrsEndpoint)readProperty("sourceLrs");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "description":
                return this.description;
            case "destinationLogin":
                return this.destinationLogin;
            case "destinationPassword":
                return this.destinationPassword;
            case "destinationUrl":
                return this.destinationUrl;
            case "filter":
                return this.filter;
            case "processing":
                return this.processing;
            case "storeRawResult":
                return this.storeRawResult;
            case "storeResult":
                return this.storeResult;
            case "owner":
                return this.owner;
            case "sourceLrs":
                return this.sourceLrs;
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
            case "destinationLogin":
                this.destinationLogin = (String)val;
                break;
            case "destinationPassword":
                this.destinationPassword = (String)val;
                break;
            case "destinationUrl":
                this.destinationUrl = (String)val;
                break;
            case "filter":
                this.filter = (String)val;
                break;
            case "processing":
                this.processing = (String)val;
                break;
            case "storeRawResult":
                this.storeRawResult = (Boolean)val;
                break;
            case "storeResult":
                this.storeResult = (Boolean)val;
                break;
            case "owner":
                this.owner = val;
                break;
            case "sourceLrs":
                this.sourceLrs = val;
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
        out.writeObject(this.destinationLogin);
        out.writeObject(this.destinationPassword);
        out.writeObject(this.destinationUrl);
        out.writeObject(this.filter);
        out.writeObject(this.processing);
        out.writeObject(this.storeRawResult);
        out.writeObject(this.storeResult);
        out.writeObject(this.owner);
        out.writeObject(this.sourceLrs);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.description = (String)in.readObject();
        this.destinationLogin = (String)in.readObject();
        this.destinationPassword = (String)in.readObject();
        this.destinationUrl = (String)in.readObject();
        this.filter = (String)in.readObject();
        this.processing = (String)in.readObject();
        this.storeRawResult = (Boolean)in.readObject();
        this.storeResult = (Boolean)in.readObject();
        this.owner = in.readObject();
        this.sourceLrs = in.readObject();
    }

}
