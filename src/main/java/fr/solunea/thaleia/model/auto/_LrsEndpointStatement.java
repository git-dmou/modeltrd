package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.LrsEndpoint;

/**
 * Class _LrsEndpointStatement was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _LrsEndpointStatement extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Date> DATE_RETRIEVED = Property.create("dateRetrieved", Date.class);
    public static final Property<String> JSONDATA = Property.create("jsondata", String.class);
    public static final Property<String> STATEMENT_ID = Property.create("statementId", String.class);
    public static final Property<LrsEndpoint> LRS_ENDPOINT = Property.create("lrsEndpoint", LrsEndpoint.class);

    protected Date dateRetrieved;
    protected String jsondata;
    protected String statementId;

    protected Object lrsEndpoint;

    public void setDateRetrieved(Date dateRetrieved) {
        beforePropertyWrite("dateRetrieved", this.dateRetrieved, dateRetrieved);
        this.dateRetrieved = dateRetrieved;
    }

    public Date getDateRetrieved() {
        beforePropertyRead("dateRetrieved");
        return this.dateRetrieved;
    }

    public void setJsondata(String jsondata) {
        beforePropertyWrite("jsondata", this.jsondata, jsondata);
        this.jsondata = jsondata;
    }

    public String getJsondata() {
        beforePropertyRead("jsondata");
        return this.jsondata;
    }

    public void setStatementId(String statementId) {
        beforePropertyWrite("statementId", this.statementId, statementId);
        this.statementId = statementId;
    }

    public String getStatementId() {
        beforePropertyRead("statementId");
        return this.statementId;
    }

    public void setLrsEndpoint(LrsEndpoint lrsEndpoint) {
        setToOneTarget("lrsEndpoint", lrsEndpoint, true);
    }

    public LrsEndpoint getLrsEndpoint() {
        return (LrsEndpoint)readProperty("lrsEndpoint");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "dateRetrieved":
                return this.dateRetrieved;
            case "jsondata":
                return this.jsondata;
            case "statementId":
                return this.statementId;
            case "lrsEndpoint":
                return this.lrsEndpoint;
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
            case "dateRetrieved":
                this.dateRetrieved = (Date)val;
                break;
            case "jsondata":
                this.jsondata = (String)val;
                break;
            case "statementId":
                this.statementId = (String)val;
                break;
            case "lrsEndpoint":
                this.lrsEndpoint = val;
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
        out.writeObject(this.dateRetrieved);
        out.writeObject(this.jsondata);
        out.writeObject(this.statementId);
        out.writeObject(this.lrsEndpoint);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.dateRetrieved = (Date)in.readObject();
        this.jsondata = (String)in.readObject();
        this.statementId = (String)in.readObject();
        this.lrsEndpoint = in.readObject();
    }

}