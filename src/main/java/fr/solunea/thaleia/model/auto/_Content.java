package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.Allocation;
import fr.solunea.thaleia.model.ContentVersion;
import fr.solunea.thaleia.model.ContributeRole;
import fr.solunea.thaleia.model.Domain;

/**
 * Class _Content was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Content extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<Boolean> IS_MODULE = Property.create("isModule", Boolean.class);
    public static final Property<List<Allocation>> ALLOCATIONS = Property.create("allocations", List.class);
    public static final Property<List<ContributeRole>> CONTRIBUTE_ROLES = Property.create("contributeRoles", List.class);
    public static final Property<Domain> DOMAIN = Property.create("domain", Domain.class);
    public static final Property<List<ContentVersion>> VERSIONS = Property.create("versions", List.class);

    protected Boolean isModule;

    protected Object allocations;
    protected Object contributeRoles;
    protected Object domain;
    protected Object versions;

    public void setIsModule(Boolean isModule) {
        beforePropertyWrite("isModule", this.isModule, isModule);
        this.isModule = isModule;
    }

    public Boolean getIsModule() {
        beforePropertyRead("isModule");
        return this.isModule;
    }

    public void addToAllocations(Allocation obj) {
        addToManyTarget("allocations", obj, true);
    }

    public void removeFromAllocations(Allocation obj) {
        removeToManyTarget("allocations", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<Allocation> getAllocations() {
        return (List<Allocation>)readProperty("allocations");
    }

    public void addToContributeRoles(ContributeRole obj) {
        addToManyTarget("contributeRoles", obj, true);
    }

    public void removeFromContributeRoles(ContributeRole obj) {
        removeToManyTarget("contributeRoles", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<ContributeRole> getContributeRoles() {
        return (List<ContributeRole>)readProperty("contributeRoles");
    }

    public void setDomain(Domain domain) {
        setToOneTarget("domain", domain, true);
    }

    public Domain getDomain() {
        return (Domain)readProperty("domain");
    }

    public void addToVersions(ContentVersion obj) {
        addToManyTarget("versions", obj, true);
    }

    public void removeFromVersions(ContentVersion obj) {
        removeToManyTarget("versions", obj, true);
    }

    @SuppressWarnings("unchecked")
    public List<ContentVersion> getVersions() {
        return (List<ContentVersion>)readProperty("versions");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "isModule":
                return this.isModule;
            case "allocations":
                return this.allocations;
            case "contributeRoles":
                return this.contributeRoles;
            case "domain":
                return this.domain;
            case "versions":
                return this.versions;
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
            case "isModule":
                this.isModule = (Boolean)val;
                break;
            case "allocations":
                this.allocations = val;
                break;
            case "contributeRoles":
                this.contributeRoles = val;
                break;
            case "domain":
                this.domain = val;
                break;
            case "versions":
                this.versions = val;
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
        out.writeObject(this.isModule);
        out.writeObject(this.allocations);
        out.writeObject(this.contributeRoles);
        out.writeObject(this.domain);
        out.writeObject(this.versions);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.isModule = (Boolean)in.readObject();
        this.allocations = in.readObject();
        this.contributeRoles = in.readObject();
        this.domain = in.readObject();
        this.versions = in.readObject();
    }

}