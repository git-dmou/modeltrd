package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

/**
 * Class _StatData was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _StatData extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> ACTOR_MBOX = Property.create("actorMbox", String.class);
    public static final Property<String> ACTOR_NAME = Property.create("actorName", String.class);
    public static final Property<String> ACTOR_OBJECTTYPE = Property.create("actorObjecttype", String.class);
    public static final Property<String> AUTHORITY_ACCOUNT_HOMEPAGE = Property.create("authorityAccountHomepage", String.class);
    public static final Property<String> AUTHORITY_ACCOUNT_NAME = Property.create("authorityAccountName", String.class);
    public static final Property<String> AUTHORITY_MBOX = Property.create("authorityMbox", String.class);
    public static final Property<String> AUTHORITY_NAME = Property.create("authorityName", String.class);
    public static final Property<String> AUTHORITY_OBJECTTYPE = Property.create("authorityObjecttype", String.class);
    public static final Property<String> CONTEXT_CONTEXTACTIVITIES_GROUPING = Property.create("contextContextactivitiesGrouping", String.class);
    public static final Property<String> CONTEXT_CONTEXTACTIVITIES_OTHER = Property.create("contextContextactivitiesOther", String.class);
    public static final Property<String> CONTEXT_CONTEXTACTIVITIES_PARENT = Property.create("contextContextactivitiesParent", String.class);
    public static final Property<String> JSONOBJECT = Property.create("jsonobject", String.class);
    public static final Property<String> OBJECT_DEFINITION_DESCRIPTION = Property.create("objectDefinitionDescription", String.class);
    public static final Property<String> OBJECT_DEFINITION_INTERACTIONTYPE = Property.create("objectDefinitionInteractiontype", String.class);
    public static final Property<String> OBJECT_DEFINITION_NAME_ENUS = Property.create("objectDefinitionNameEnus", String.class);
    public static final Property<String> OBJECT_DEFINITION_TYPE = Property.create("objectDefinitionType", String.class);
    public static final Property<String> OBJECT_ID_URL = Property.create("objectIdUrl", String.class);
    public static final Property<String> OBJECT_OBJECTTYPE = Property.create("objectObjecttype", String.class);
    public static final Property<String> PUBLICATION_REFERENCE = Property.create("publicationReference", String.class);
    public static final Property<Date> RECEIVED = Property.create("received", Date.class);
    public static final Property<Boolean> RESULT_COMPLETION = Property.create("resultCompletion", Boolean.class);
    public static final Property<String> RESULT_DURATION = Property.create("resultDuration", String.class);
    public static final Property<String> RESULT_EXTENSIONS_COMPLETIONSTATUS = Property.create("resultExtensionsCompletionstatus", String.class);
    public static final Property<String> RESULT_EXTENSIONS_LOCATION = Property.create("resultExtensionsLocation", String.class);
    public static final Property<String> RESULT_EXTENSIONS_PROGRESSMEASURE = Property.create("resultExtensionsProgressmeasure", String.class);
    public static final Property<String> RESULT_EXTENSIONS_SCORMVERSION = Property.create("resultExtensionsScormversion", String.class);
    public static final Property<String> RESULT_EXTENSIONS_SUCCESSSTATUS = Property.create("resultExtensionsSuccessstatus", String.class);
    public static final Property<String> RESULT_RESPONSE = Property.create("resultResponse", String.class);
    public static final Property<Float> RESULT_SCORE_MAX = Property.create("resultScoreMax", Float.class);
    public static final Property<Float> RESULT_SCORE_MIN = Property.create("resultScoreMin", Float.class);
    public static final Property<Float> RESULT_SCORE_RAW = Property.create("resultScoreRaw", Float.class);
    public static final Property<Float> RESULT_SCORE_SCALED = Property.create("resultScoreScaled", Float.class);
    public static final Property<Boolean> RESULT_SUCCESS = Property.create("resultSuccess", Boolean.class);
    public static final Property<String> TIMESTAMP = Property.create("timestamp", String.class);
    public static final Property<String> VERB_DISPLAY_ENUS = Property.create("verbDisplayEnus", String.class);
    public static final Property<String> VERB_ID = Property.create("verbId", String.class);

    protected String actorMbox;
    protected String actorName;
    protected String actorObjecttype;
    protected String authorityAccountHomepage;
    protected String authorityAccountName;
    protected String authorityMbox;
    protected String authorityName;
    protected String authorityObjecttype;
    protected String contextContextactivitiesGrouping;
    protected String contextContextactivitiesOther;
    protected String contextContextactivitiesParent;
    protected String jsonobject;
    protected String objectDefinitionDescription;
    protected String objectDefinitionInteractiontype;
    protected String objectDefinitionNameEnus;
    protected String objectDefinitionType;
    protected String objectIdUrl;
    protected String objectObjecttype;
    protected String publicationReference;
    protected Date received;
    protected Boolean resultCompletion;
    protected String resultDuration;
    protected String resultExtensionsCompletionstatus;
    protected String resultExtensionsLocation;
    protected String resultExtensionsProgressmeasure;
    protected String resultExtensionsScormversion;
    protected String resultExtensionsSuccessstatus;
    protected String resultResponse;
    protected Float resultScoreMax;
    protected Float resultScoreMin;
    protected Float resultScoreRaw;
    protected Float resultScoreScaled;
    protected Boolean resultSuccess;
    protected String timestamp;
    protected String verbDisplayEnus;
    protected String verbId;


    public void setActorMbox(String actorMbox) {
        beforePropertyWrite("actorMbox", this.actorMbox, actorMbox);
        this.actorMbox = actorMbox;
    }

    public String getActorMbox() {
        beforePropertyRead("actorMbox");
        return this.actorMbox;
    }

    public void setActorName(String actorName) {
        beforePropertyWrite("actorName", this.actorName, actorName);
        this.actorName = actorName;
    }

    public String getActorName() {
        beforePropertyRead("actorName");
        return this.actorName;
    }

    public void setActorObjecttype(String actorObjecttype) {
        beforePropertyWrite("actorObjecttype", this.actorObjecttype, actorObjecttype);
        this.actorObjecttype = actorObjecttype;
    }

    public String getActorObjecttype() {
        beforePropertyRead("actorObjecttype");
        return this.actorObjecttype;
    }

    public void setAuthorityAccountHomepage(String authorityAccountHomepage) {
        beforePropertyWrite("authorityAccountHomepage", this.authorityAccountHomepage, authorityAccountHomepage);
        this.authorityAccountHomepage = authorityAccountHomepage;
    }

    public String getAuthorityAccountHomepage() {
        beforePropertyRead("authorityAccountHomepage");
        return this.authorityAccountHomepage;
    }

    public void setAuthorityAccountName(String authorityAccountName) {
        beforePropertyWrite("authorityAccountName", this.authorityAccountName, authorityAccountName);
        this.authorityAccountName = authorityAccountName;
    }

    public String getAuthorityAccountName() {
        beforePropertyRead("authorityAccountName");
        return this.authorityAccountName;
    }

    public void setAuthorityMbox(String authorityMbox) {
        beforePropertyWrite("authorityMbox", this.authorityMbox, authorityMbox);
        this.authorityMbox = authorityMbox;
    }

    public String getAuthorityMbox() {
        beforePropertyRead("authorityMbox");
        return this.authorityMbox;
    }

    public void setAuthorityName(String authorityName) {
        beforePropertyWrite("authorityName", this.authorityName, authorityName);
        this.authorityName = authorityName;
    }

    public String getAuthorityName() {
        beforePropertyRead("authorityName");
        return this.authorityName;
    }

    public void setAuthorityObjecttype(String authorityObjecttype) {
        beforePropertyWrite("authorityObjecttype", this.authorityObjecttype, authorityObjecttype);
        this.authorityObjecttype = authorityObjecttype;
    }

    public String getAuthorityObjecttype() {
        beforePropertyRead("authorityObjecttype");
        return this.authorityObjecttype;
    }

    public void setContextContextactivitiesGrouping(String contextContextactivitiesGrouping) {
        beforePropertyWrite("contextContextactivitiesGrouping", this.contextContextactivitiesGrouping, contextContextactivitiesGrouping);
        this.contextContextactivitiesGrouping = contextContextactivitiesGrouping;
    }

    public String getContextContextactivitiesGrouping() {
        beforePropertyRead("contextContextactivitiesGrouping");
        return this.contextContextactivitiesGrouping;
    }

    public void setContextContextactivitiesOther(String contextContextactivitiesOther) {
        beforePropertyWrite("contextContextactivitiesOther", this.contextContextactivitiesOther, contextContextactivitiesOther);
        this.contextContextactivitiesOther = contextContextactivitiesOther;
    }

    public String getContextContextactivitiesOther() {
        beforePropertyRead("contextContextactivitiesOther");
        return this.contextContextactivitiesOther;
    }

    public void setContextContextactivitiesParent(String contextContextactivitiesParent) {
        beforePropertyWrite("contextContextactivitiesParent", this.contextContextactivitiesParent, contextContextactivitiesParent);
        this.contextContextactivitiesParent = contextContextactivitiesParent;
    }

    public String getContextContextactivitiesParent() {
        beforePropertyRead("contextContextactivitiesParent");
        return this.contextContextactivitiesParent;
    }

    public void setJsonobject(String jsonobject) {
        beforePropertyWrite("jsonobject", this.jsonobject, jsonobject);
        this.jsonobject = jsonobject;
    }

    public String getJsonobject() {
        beforePropertyRead("jsonobject");
        return this.jsonobject;
    }

    public void setObjectDefinitionDescription(String objectDefinitionDescription) {
        beforePropertyWrite("objectDefinitionDescription", this.objectDefinitionDescription, objectDefinitionDescription);
        this.objectDefinitionDescription = objectDefinitionDescription;
    }

    public String getObjectDefinitionDescription() {
        beforePropertyRead("objectDefinitionDescription");
        return this.objectDefinitionDescription;
    }

    public void setObjectDefinitionInteractiontype(String objectDefinitionInteractiontype) {
        beforePropertyWrite("objectDefinitionInteractiontype", this.objectDefinitionInteractiontype, objectDefinitionInteractiontype);
        this.objectDefinitionInteractiontype = objectDefinitionInteractiontype;
    }

    public String getObjectDefinitionInteractiontype() {
        beforePropertyRead("objectDefinitionInteractiontype");
        return this.objectDefinitionInteractiontype;
    }

    public void setObjectDefinitionNameEnus(String objectDefinitionNameEnus) {
        beforePropertyWrite("objectDefinitionNameEnus", this.objectDefinitionNameEnus, objectDefinitionNameEnus);
        this.objectDefinitionNameEnus = objectDefinitionNameEnus;
    }

    public String getObjectDefinitionNameEnus() {
        beforePropertyRead("objectDefinitionNameEnus");
        return this.objectDefinitionNameEnus;
    }

    public void setObjectDefinitionType(String objectDefinitionType) {
        beforePropertyWrite("objectDefinitionType", this.objectDefinitionType, objectDefinitionType);
        this.objectDefinitionType = objectDefinitionType;
    }

    public String getObjectDefinitionType() {
        beforePropertyRead("objectDefinitionType");
        return this.objectDefinitionType;
    }

    public void setObjectIdUrl(String objectIdUrl) {
        beforePropertyWrite("objectIdUrl", this.objectIdUrl, objectIdUrl);
        this.objectIdUrl = objectIdUrl;
    }

    public String getObjectIdUrl() {
        beforePropertyRead("objectIdUrl");
        return this.objectIdUrl;
    }

    public void setObjectObjecttype(String objectObjecttype) {
        beforePropertyWrite("objectObjecttype", this.objectObjecttype, objectObjecttype);
        this.objectObjecttype = objectObjecttype;
    }

    public String getObjectObjecttype() {
        beforePropertyRead("objectObjecttype");
        return this.objectObjecttype;
    }

    public void setPublicationReference(String publicationReference) {
        beforePropertyWrite("publicationReference", this.publicationReference, publicationReference);
        this.publicationReference = publicationReference;
    }

    public String getPublicationReference() {
        beforePropertyRead("publicationReference");
        return this.publicationReference;
    }

    public void setReceived(Date received) {
        beforePropertyWrite("received", this.received, received);
        this.received = received;
    }

    public Date getReceived() {
        beforePropertyRead("received");
        return this.received;
    }

    public void setResultCompletion(Boolean resultCompletion) {
        beforePropertyWrite("resultCompletion", this.resultCompletion, resultCompletion);
        this.resultCompletion = resultCompletion;
    }

    public Boolean getResultCompletion() {
        beforePropertyRead("resultCompletion");
        return this.resultCompletion;
    }

    public void setResultDuration(String resultDuration) {
        beforePropertyWrite("resultDuration", this.resultDuration, resultDuration);
        this.resultDuration = resultDuration;
    }

    public String getResultDuration() {
        beforePropertyRead("resultDuration");
        return this.resultDuration;
    }

    public void setResultExtensionsCompletionstatus(String resultExtensionsCompletionstatus) {
        beforePropertyWrite("resultExtensionsCompletionstatus", this.resultExtensionsCompletionstatus, resultExtensionsCompletionstatus);
        this.resultExtensionsCompletionstatus = resultExtensionsCompletionstatus;
    }

    public String getResultExtensionsCompletionstatus() {
        beforePropertyRead("resultExtensionsCompletionstatus");
        return this.resultExtensionsCompletionstatus;
    }

    public void setResultExtensionsLocation(String resultExtensionsLocation) {
        beforePropertyWrite("resultExtensionsLocation", this.resultExtensionsLocation, resultExtensionsLocation);
        this.resultExtensionsLocation = resultExtensionsLocation;
    }

    public String getResultExtensionsLocation() {
        beforePropertyRead("resultExtensionsLocation");
        return this.resultExtensionsLocation;
    }

    public void setResultExtensionsProgressmeasure(String resultExtensionsProgressmeasure) {
        beforePropertyWrite("resultExtensionsProgressmeasure", this.resultExtensionsProgressmeasure, resultExtensionsProgressmeasure);
        this.resultExtensionsProgressmeasure = resultExtensionsProgressmeasure;
    }

    public String getResultExtensionsProgressmeasure() {
        beforePropertyRead("resultExtensionsProgressmeasure");
        return this.resultExtensionsProgressmeasure;
    }

    public void setResultExtensionsScormversion(String resultExtensionsScormversion) {
        beforePropertyWrite("resultExtensionsScormversion", this.resultExtensionsScormversion, resultExtensionsScormversion);
        this.resultExtensionsScormversion = resultExtensionsScormversion;
    }

    public String getResultExtensionsScormversion() {
        beforePropertyRead("resultExtensionsScormversion");
        return this.resultExtensionsScormversion;
    }

    public void setResultExtensionsSuccessstatus(String resultExtensionsSuccessstatus) {
        beforePropertyWrite("resultExtensionsSuccessstatus", this.resultExtensionsSuccessstatus, resultExtensionsSuccessstatus);
        this.resultExtensionsSuccessstatus = resultExtensionsSuccessstatus;
    }

    public String getResultExtensionsSuccessstatus() {
        beforePropertyRead("resultExtensionsSuccessstatus");
        return this.resultExtensionsSuccessstatus;
    }

    public void setResultResponse(String resultResponse) {
        beforePropertyWrite("resultResponse", this.resultResponse, resultResponse);
        this.resultResponse = resultResponse;
    }

    public String getResultResponse() {
        beforePropertyRead("resultResponse");
        return this.resultResponse;
    }

    public void setResultScoreMax(Float resultScoreMax) {
        beforePropertyWrite("resultScoreMax", this.resultScoreMax, resultScoreMax);
        this.resultScoreMax = resultScoreMax;
    }

    public Float getResultScoreMax() {
        beforePropertyRead("resultScoreMax");
        return this.resultScoreMax;
    }

    public void setResultScoreMin(Float resultScoreMin) {
        beforePropertyWrite("resultScoreMin", this.resultScoreMin, resultScoreMin);
        this.resultScoreMin = resultScoreMin;
    }

    public Float getResultScoreMin() {
        beforePropertyRead("resultScoreMin");
        return this.resultScoreMin;
    }

    public void setResultScoreRaw(Float resultScoreRaw) {
        beforePropertyWrite("resultScoreRaw", this.resultScoreRaw, resultScoreRaw);
        this.resultScoreRaw = resultScoreRaw;
    }

    public Float getResultScoreRaw() {
        beforePropertyRead("resultScoreRaw");
        return this.resultScoreRaw;
    }

    public void setResultScoreScaled(Float resultScoreScaled) {
        beforePropertyWrite("resultScoreScaled", this.resultScoreScaled, resultScoreScaled);
        this.resultScoreScaled = resultScoreScaled;
    }

    public Float getResultScoreScaled() {
        beforePropertyRead("resultScoreScaled");
        return this.resultScoreScaled;
    }

    public void setResultSuccess(Boolean resultSuccess) {
        beforePropertyWrite("resultSuccess", this.resultSuccess, resultSuccess);
        this.resultSuccess = resultSuccess;
    }

    public Boolean getResultSuccess() {
        beforePropertyRead("resultSuccess");
        return this.resultSuccess;
    }

    public void setTimestamp(String timestamp) {
        beforePropertyWrite("timestamp", this.timestamp, timestamp);
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        beforePropertyRead("timestamp");
        return this.timestamp;
    }

    public void setVerbDisplayEnus(String verbDisplayEnus) {
        beforePropertyWrite("verbDisplayEnus", this.verbDisplayEnus, verbDisplayEnus);
        this.verbDisplayEnus = verbDisplayEnus;
    }

    public String getVerbDisplayEnus() {
        beforePropertyRead("verbDisplayEnus");
        return this.verbDisplayEnus;
    }

    public void setVerbId(String verbId) {
        beforePropertyWrite("verbId", this.verbId, verbId);
        this.verbId = verbId;
    }

    public String getVerbId() {
        beforePropertyRead("verbId");
        return this.verbId;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "actorMbox":
                return this.actorMbox;
            case "actorName":
                return this.actorName;
            case "actorObjecttype":
                return this.actorObjecttype;
            case "authorityAccountHomepage":
                return this.authorityAccountHomepage;
            case "authorityAccountName":
                return this.authorityAccountName;
            case "authorityMbox":
                return this.authorityMbox;
            case "authorityName":
                return this.authorityName;
            case "authorityObjecttype":
                return this.authorityObjecttype;
            case "contextContextactivitiesGrouping":
                return this.contextContextactivitiesGrouping;
            case "contextContextactivitiesOther":
                return this.contextContextactivitiesOther;
            case "contextContextactivitiesParent":
                return this.contextContextactivitiesParent;
            case "jsonobject":
                return this.jsonobject;
            case "objectDefinitionDescription":
                return this.objectDefinitionDescription;
            case "objectDefinitionInteractiontype":
                return this.objectDefinitionInteractiontype;
            case "objectDefinitionNameEnus":
                return this.objectDefinitionNameEnus;
            case "objectDefinitionType":
                return this.objectDefinitionType;
            case "objectIdUrl":
                return this.objectIdUrl;
            case "objectObjecttype":
                return this.objectObjecttype;
            case "publicationReference":
                return this.publicationReference;
            case "received":
                return this.received;
            case "resultCompletion":
                return this.resultCompletion;
            case "resultDuration":
                return this.resultDuration;
            case "resultExtensionsCompletionstatus":
                return this.resultExtensionsCompletionstatus;
            case "resultExtensionsLocation":
                return this.resultExtensionsLocation;
            case "resultExtensionsProgressmeasure":
                return this.resultExtensionsProgressmeasure;
            case "resultExtensionsScormversion":
                return this.resultExtensionsScormversion;
            case "resultExtensionsSuccessstatus":
                return this.resultExtensionsSuccessstatus;
            case "resultResponse":
                return this.resultResponse;
            case "resultScoreMax":
                return this.resultScoreMax;
            case "resultScoreMin":
                return this.resultScoreMin;
            case "resultScoreRaw":
                return this.resultScoreRaw;
            case "resultScoreScaled":
                return this.resultScoreScaled;
            case "resultSuccess":
                return this.resultSuccess;
            case "timestamp":
                return this.timestamp;
            case "verbDisplayEnus":
                return this.verbDisplayEnus;
            case "verbId":
                return this.verbId;
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
            case "actorMbox":
                this.actorMbox = (String)val;
                break;
            case "actorName":
                this.actorName = (String)val;
                break;
            case "actorObjecttype":
                this.actorObjecttype = (String)val;
                break;
            case "authorityAccountHomepage":
                this.authorityAccountHomepage = (String)val;
                break;
            case "authorityAccountName":
                this.authorityAccountName = (String)val;
                break;
            case "authorityMbox":
                this.authorityMbox = (String)val;
                break;
            case "authorityName":
                this.authorityName = (String)val;
                break;
            case "authorityObjecttype":
                this.authorityObjecttype = (String)val;
                break;
            case "contextContextactivitiesGrouping":
                this.contextContextactivitiesGrouping = (String)val;
                break;
            case "contextContextactivitiesOther":
                this.contextContextactivitiesOther = (String)val;
                break;
            case "contextContextactivitiesParent":
                this.contextContextactivitiesParent = (String)val;
                break;
            case "jsonobject":
                this.jsonobject = (String)val;
                break;
            case "objectDefinitionDescription":
                this.objectDefinitionDescription = (String)val;
                break;
            case "objectDefinitionInteractiontype":
                this.objectDefinitionInteractiontype = (String)val;
                break;
            case "objectDefinitionNameEnus":
                this.objectDefinitionNameEnus = (String)val;
                break;
            case "objectDefinitionType":
                this.objectDefinitionType = (String)val;
                break;
            case "objectIdUrl":
                this.objectIdUrl = (String)val;
                break;
            case "objectObjecttype":
                this.objectObjecttype = (String)val;
                break;
            case "publicationReference":
                this.publicationReference = (String)val;
                break;
            case "received":
                this.received = (Date)val;
                break;
            case "resultCompletion":
                this.resultCompletion = (Boolean)val;
                break;
            case "resultDuration":
                this.resultDuration = (String)val;
                break;
            case "resultExtensionsCompletionstatus":
                this.resultExtensionsCompletionstatus = (String)val;
                break;
            case "resultExtensionsLocation":
                this.resultExtensionsLocation = (String)val;
                break;
            case "resultExtensionsProgressmeasure":
                this.resultExtensionsProgressmeasure = (String)val;
                break;
            case "resultExtensionsScormversion":
                this.resultExtensionsScormversion = (String)val;
                break;
            case "resultExtensionsSuccessstatus":
                this.resultExtensionsSuccessstatus = (String)val;
                break;
            case "resultResponse":
                this.resultResponse = (String)val;
                break;
            case "resultScoreMax":
                this.resultScoreMax = (Float)val;
                break;
            case "resultScoreMin":
                this.resultScoreMin = (Float)val;
                break;
            case "resultScoreRaw":
                this.resultScoreRaw = (Float)val;
                break;
            case "resultScoreScaled":
                this.resultScoreScaled = (Float)val;
                break;
            case "resultSuccess":
                this.resultSuccess = (Boolean)val;
                break;
            case "timestamp":
                this.timestamp = (String)val;
                break;
            case "verbDisplayEnus":
                this.verbDisplayEnus = (String)val;
                break;
            case "verbId":
                this.verbId = (String)val;
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
        out.writeObject(this.actorMbox);
        out.writeObject(this.actorName);
        out.writeObject(this.actorObjecttype);
        out.writeObject(this.authorityAccountHomepage);
        out.writeObject(this.authorityAccountName);
        out.writeObject(this.authorityMbox);
        out.writeObject(this.authorityName);
        out.writeObject(this.authorityObjecttype);
        out.writeObject(this.contextContextactivitiesGrouping);
        out.writeObject(this.contextContextactivitiesOther);
        out.writeObject(this.contextContextactivitiesParent);
        out.writeObject(this.jsonobject);
        out.writeObject(this.objectDefinitionDescription);
        out.writeObject(this.objectDefinitionInteractiontype);
        out.writeObject(this.objectDefinitionNameEnus);
        out.writeObject(this.objectDefinitionType);
        out.writeObject(this.objectIdUrl);
        out.writeObject(this.objectObjecttype);
        out.writeObject(this.publicationReference);
        out.writeObject(this.received);
        out.writeObject(this.resultCompletion);
        out.writeObject(this.resultDuration);
        out.writeObject(this.resultExtensionsCompletionstatus);
        out.writeObject(this.resultExtensionsLocation);
        out.writeObject(this.resultExtensionsProgressmeasure);
        out.writeObject(this.resultExtensionsScormversion);
        out.writeObject(this.resultExtensionsSuccessstatus);
        out.writeObject(this.resultResponse);
        out.writeObject(this.resultScoreMax);
        out.writeObject(this.resultScoreMin);
        out.writeObject(this.resultScoreRaw);
        out.writeObject(this.resultScoreScaled);
        out.writeObject(this.resultSuccess);
        out.writeObject(this.timestamp);
        out.writeObject(this.verbDisplayEnus);
        out.writeObject(this.verbId);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.actorMbox = (String)in.readObject();
        this.actorName = (String)in.readObject();
        this.actorObjecttype = (String)in.readObject();
        this.authorityAccountHomepage = (String)in.readObject();
        this.authorityAccountName = (String)in.readObject();
        this.authorityMbox = (String)in.readObject();
        this.authorityName = (String)in.readObject();
        this.authorityObjecttype = (String)in.readObject();
        this.contextContextactivitiesGrouping = (String)in.readObject();
        this.contextContextactivitiesOther = (String)in.readObject();
        this.contextContextactivitiesParent = (String)in.readObject();
        this.jsonobject = (String)in.readObject();
        this.objectDefinitionDescription = (String)in.readObject();
        this.objectDefinitionInteractiontype = (String)in.readObject();
        this.objectDefinitionNameEnus = (String)in.readObject();
        this.objectDefinitionType = (String)in.readObject();
        this.objectIdUrl = (String)in.readObject();
        this.objectObjecttype = (String)in.readObject();
        this.publicationReference = (String)in.readObject();
        this.received = (Date)in.readObject();
        this.resultCompletion = (Boolean)in.readObject();
        this.resultDuration = (String)in.readObject();
        this.resultExtensionsCompletionstatus = (String)in.readObject();
        this.resultExtensionsLocation = (String)in.readObject();
        this.resultExtensionsProgressmeasure = (String)in.readObject();
        this.resultExtensionsScormversion = (String)in.readObject();
        this.resultExtensionsSuccessstatus = (String)in.readObject();
        this.resultResponse = (String)in.readObject();
        this.resultScoreMax = (Float)in.readObject();
        this.resultScoreMin = (Float)in.readObject();
        this.resultScoreRaw = (Float)in.readObject();
        this.resultScoreScaled = (Float)in.readObject();
        this.resultSuccess = (Boolean)in.readObject();
        this.timestamp = (String)in.readObject();
        this.verbDisplayEnus = (String)in.readObject();
        this.verbId = (String)in.readObject();
    }

}
