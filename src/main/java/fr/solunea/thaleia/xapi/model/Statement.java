package fr.solunea.thaleia.xapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe est nécessaire pour la conversion d'un objet JSON au format
 * StatData pris en charge par Cayenne. Cette classe prends en charge tous les
 * types de statements (SCORM ou non)
 */
@Generated("org.jsonschema2pojo")
public class Statement {

    @Expose
    private Actor actor;
    @Expose
    private Verb verb;
    @Expose
    private Object object;
    @Expose
    private Context context;
    @Expose
    private Result result;
    @Expose
    private String timestamp;
    @Expose
    private Authority authority;

    /**
     * <p>Getter for the field <code>actor</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Actor} object.
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * <p>Setter for the field <code>actor</code>.</p>
     *
     * @param actor a {@link fr.solunea.thaleia.xapi.model.Statement.Actor} object.
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * <p>Getter for the field <code>verb</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Verb} object.
     */
    public Verb getVerb() {
        return verb;
    }

    /**
     * <p>Setter for the field <code>verb</code>.</p>
     *
     * @param verb a {@link fr.solunea.thaleia.xapi.model.Statement.Verb} object.
     */
    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    /**
     * <p>Getter for the field <code>object</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Object} object.
     */
    public Object getObject() {
        return object;
    }

    /**
     * <p>Setter for the field <code>object</code>.</p>
     *
     * @param object a {@link fr.solunea.thaleia.xapi.model.Statement.Object} object.
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * <p>Getter for the field <code>context</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Context} object.
     */
    public Context getContext() {
        return context;
    }

    /**
     * <p>Setter for the field <code>context</code>.</p>
     *
     * @param context a {@link fr.solunea.thaleia.xapi.model.Statement.Context} object.
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * <p>Getter for the field <code>result</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Result} object.
     */
    public Result getResult() {
        return result;
    }

    /**
     * <p>Setter for the field <code>result</code>.</p>
     *
     * @param result a {@link fr.solunea.thaleia.xapi.model.Statement.Result} object.
     */
    public void setResult(Result result) {
        this.result = result;
    }

    /**
     * <p>Getter for the field <code>timestamp</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * <p>Setter for the field <code>timestamp</code>.</p>
     *
     * @param timestamp a {@link java.lang.String} object.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * <p>Getter for the field <code>authority</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.xapi.model.Statement.Authority} object.
     */
    public Authority getAuthority() {
        return authority;
    }

    /**
     * <p>Setter for the field <code>authority</code>.</p>
     *
     * @param authority a {@link fr.solunea.thaleia.xapi.model.Statement.Authority} object.
     */
    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Generated("org.jsonschema2pojo")
    public class Actor {

        @Expose
        private String objectType;
        @Expose
        private String mbox;
        @Expose
        private String name;

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getMbox() {
            return mbox;
        }

        public void setMbox(String mbox) {
            this.mbox = mbox;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    @Generated("org.jsonschema2pojo")
    public class Verb {

        @Expose
        private String id;
        @Expose
        private Display display;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Display getDisplay() {
            return display;
        }

        public void setDisplay(Display display) {
            this.display = display;
        }

        @Generated("org.jsonschema2pojo")
        public class Display {

            @SerializedName("en-US")
            @Expose
            private String enUS;

            public String getEnUS() {
                return enUS;
            }

            public void setEnUS(String enUS) {
                this.enUS = enUS;
            }

            @SerializedName("und")
            @Expose
            private String und;

            public String getUnd() {
                return und;
            }

            public void setUnd(String und) {
                this.und = und;
            }

        }

    }

    @Generated("org.jsonschema2pojo")
    public class Object {

        @Expose
        private String id;
        @Expose
        private Definition definition;
        @Expose
        private String objectType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Definition getDefinition() {
            return definition;
        }

        public void setDefinition(Definition definition) {
            this.definition = definition;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        @Generated("org.jsonschema2pojo")
        public class Definition {

            @Expose
            private Name name;
            @Expose
            private String type;
            @Expose
            private String interactionType;

            public String getInteractionType() {
                return interactionType;
            }

            public void setInteractionType(String interactionType) {
                this.interactionType = interactionType;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Name getName() {
                return name;
            }

            public void setName(Name name) {
                this.name = name;
            }

            @Generated("org.jsonschema2pojo")
            public class Name {

                @SerializedName("en-US")
                @Expose
                private String enUS;

                public String getEnUS() {
                    return enUS;
                }

                public void setEnUS(String enUS) {
                    this.enUS = enUS;
                }

                @SerializedName("fr-FR")
                @Expose
                private String frFR;

                public String getFrFR() {
                    return frFR;
                }

                public void setFrFR(String frFR) {
                    this.frFR = frFR;
                }

            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public class Context {

        @Expose
        private ContextActivities contextActivities;

        public ContextActivities getContextActivities() {
            return contextActivities;
        }

        public void setContextActivities(ContextActivities contextActivities) {
            this.contextActivities = contextActivities;
        }

        @Generated("org.jsonschema2pojo")
        public class ContextActivities {

            @Expose
            private List<Parent> parent = new ArrayList<Parent>();
            @Expose
            private List<Grouping> grouping = new ArrayList<Grouping>();
            @Expose
            private List<Other> other = new ArrayList<Other>();

            public List<Parent> getParent() {
                return parent;
            }

            public void setParent(List<Parent> parent) {
                this.parent = parent;
            }

            public List<Grouping> getGrouping() {
                return grouping;
            }

            public void setGrouping(List<Grouping> grouping) {
                this.grouping = grouping;
            }

            public List<Other> getOther() {
                return other;
            }

            public void setOther(List<Other> other) {
                this.other = other;
            }

            @Generated("org.jsonschema2pojo")
            public class Parent {

                @Expose
                private String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

            }

            @Generated("org.jsonschema2pojo")
            public class Grouping {

                @Expose
                private String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

            }

            @Generated("org.jsonschema2pojo")
            public class Other {

                @Expose
                private String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

            }

        }

    }

    @Generated("org.jsonschema2pojo")
    public class Result {

        @Expose
        private Boolean completion;
        @Expose
        private Boolean success;
        @Expose
        private String duration;
        @Expose
        private String response;
        @Expose
        private Extensions extensions;
        @Expose
        private Score score;


        public Boolean getCompletion() {
            return completion;
        }

        public void setCompletion(Boolean completion) {
            this.completion = completion;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public Extensions getExtensions() {
            return extensions;
        }

        public void setExtensions(Extensions extensions) {
            this.extensions = extensions;
        }

        public void setScore(Score score) {
            this.score = score;
        }

        public Score getScore() {
            return score;
        }

        public class Score {

            @Expose
            private float min;
            @Expose
            private float max;
            @Expose
            private float raw;
            @Expose
            private float scaled;

            public float getMin() {
                return min;
            }

            public void setMin(float min) {
                this.min = min;
            }

            public float getMax() {
                return max;
            }

            public void setMax(float max) {
                this.max = max;
            }

            public float getRaw() {
                return raw;
            }

            public void setRaw(float raw) {
                this.raw = raw;
            }

            public float getScaled() {
                return scaled;
            }

            public void setScaled(float scaled) {
                this.scaled = scaled;
            }
        }

        @Generated("org.jsonschema2pojo")
        public class Extensions {

            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/completion_status/")
            @Expose
            private String completionStatus;
            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/progress_measure/")
            @Expose
            private String progressMeasure;
            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/success_status/")
            @Expose
            private String successStatus;
            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/location/")
            @Expose
            private String location;
            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/suspend_data/")
            @Expose
            private String suspendData;
            @SerializedName("http://aero.gocreate-solutions.com/scormXapi/201312/result/extensions/scorm_version/")
            @Expose
            private String scormVersion;

            public String getCompletionStatus() {
                return completionStatus;
            }

            public void setCompletionStatus(String completionStatus) {
                this.completionStatus = completionStatus;
            }

            public String getProgressMeasure() {
                return progressMeasure;
            }

            public void setsProgressMeasure(String progressMeasure) {
                this.progressMeasure = progressMeasure;
            }

            public String getSuccessStatus() {
                return successStatus;
            }

            public void setSuccessStatus(String successStatus) {
                this.successStatus = successStatus;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getSuspendData() {
                return suspendData;
            }

            public void setSuspendData(String suspendData) {
                this.suspendData = suspendData;
            }

            public String getScormVersion() {
                return scormVersion;
            }

            public void setScormVersion(String scormVersion) {
                this.scormVersion = scormVersion;
            }

        }

    }

    @Generated("org.jsonschema2pojo")
    public class Authority {

        @Expose
        private String objectType;
        @Expose
        private String mbox;
        @Expose
        private String name;
        @Expose
        private Account account;

        @Generated("org.jsonschema2pojo")
        public class Account {

            @Expose
            private String homePage;
            @Expose
            private String name;

            public String getHomePage() {
                return homePage;
            }

            public void setHomePage(String homePage) {
                this.homePage = homePage;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getMbox() {
            return mbox;
        }

        public void setMbox(String mbox) {
            this.mbox = mbox;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
