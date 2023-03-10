package fr.solunea.thaleia.model.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

import fr.solunea.thaleia.model.User;

/**
 * Class _BuyProcess was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _BuyProcess extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "id";

    public static final Property<String> AMOUNT = Property.create("amount", String.class);
    public static final Property<Date> APPROVAL_DATE = Property.create("approvalDate", Date.class);
    public static final Property<String> CREATION_DATE = Property.create("creationDate", String.class);
    public static final Property<String> EXECUTE_URL = Property.create("executeUrl", String.class);
    public static final Property<Date> EXECUTION_DATE = Property.create("executionDate", Date.class);
    public static final Property<String> INVOICE = Property.create("invoice", String.class);
    public static final Property<String> PAYMENT_EXTERNAL_ID = Property.create("paymentExternalId", String.class);
    public static final Property<String> SKU_GIVEN = Property.create("skuGiven", String.class);
    public static final Property<String> SKU_REQUESTED = Property.create("skuRequested", String.class);
    public static final Property<User> PAYER = Property.create("payer", User.class);

    protected String amount;
    protected Date approvalDate;
    protected String creationDate;
    protected String executeUrl;
    protected Date executionDate;
    protected String invoice;
    protected String paymentExternalId;
    protected String skuGiven;
    protected String skuRequested;

    protected Object payer;

    public void setAmount(String amount) {
        beforePropertyWrite("amount", this.amount, amount);
        this.amount = amount;
    }

    public String getAmount() {
        beforePropertyRead("amount");
        return this.amount;
    }

    public void setApprovalDate(Date approvalDate) {
        beforePropertyWrite("approvalDate", this.approvalDate, approvalDate);
        this.approvalDate = approvalDate;
    }

    public Date getApprovalDate() {
        beforePropertyRead("approvalDate");
        return this.approvalDate;
    }

    public void setCreationDate(String creationDate) {
        beforePropertyWrite("creationDate", this.creationDate, creationDate);
        this.creationDate = creationDate;
    }

    public String getCreationDate() {
        beforePropertyRead("creationDate");
        return this.creationDate;
    }

    public void setExecuteUrl(String executeUrl) {
        beforePropertyWrite("executeUrl", this.executeUrl, executeUrl);
        this.executeUrl = executeUrl;
    }

    public String getExecuteUrl() {
        beforePropertyRead("executeUrl");
        return this.executeUrl;
    }

    public void setExecutionDate(Date executionDate) {
        beforePropertyWrite("executionDate", this.executionDate, executionDate);
        this.executionDate = executionDate;
    }

    public Date getExecutionDate() {
        beforePropertyRead("executionDate");
        return this.executionDate;
    }

    public void setInvoice(String invoice) {
        beforePropertyWrite("invoice", this.invoice, invoice);
        this.invoice = invoice;
    }

    public String getInvoice() {
        beforePropertyRead("invoice");
        return this.invoice;
    }

    public void setPaymentExternalId(String paymentExternalId) {
        beforePropertyWrite("paymentExternalId", this.paymentExternalId, paymentExternalId);
        this.paymentExternalId = paymentExternalId;
    }

    public String getPaymentExternalId() {
        beforePropertyRead("paymentExternalId");
        return this.paymentExternalId;
    }

    public void setSkuGiven(String skuGiven) {
        beforePropertyWrite("skuGiven", this.skuGiven, skuGiven);
        this.skuGiven = skuGiven;
    }

    public String getSkuGiven() {
        beforePropertyRead("skuGiven");
        return this.skuGiven;
    }

    public void setSkuRequested(String skuRequested) {
        beforePropertyWrite("skuRequested", this.skuRequested, skuRequested);
        this.skuRequested = skuRequested;
    }

    public String getSkuRequested() {
        beforePropertyRead("skuRequested");
        return this.skuRequested;
    }

    public void setPayer(User payer) {
        setToOneTarget("payer", payer, true);
    }

    public User getPayer() {
        return (User)readProperty("payer");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "amount":
                return this.amount;
            case "approvalDate":
                return this.approvalDate;
            case "creationDate":
                return this.creationDate;
            case "executeUrl":
                return this.executeUrl;
            case "executionDate":
                return this.executionDate;
            case "invoice":
                return this.invoice;
            case "paymentExternalId":
                return this.paymentExternalId;
            case "skuGiven":
                return this.skuGiven;
            case "skuRequested":
                return this.skuRequested;
            case "payer":
                return this.payer;
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
            case "amount":
                this.amount = (String)val;
                break;
            case "approvalDate":
                this.approvalDate = (Date)val;
                break;
            case "creationDate":
                this.creationDate = (String)val;
                break;
            case "executeUrl":
                this.executeUrl = (String)val;
                break;
            case "executionDate":
                this.executionDate = (Date)val;
                break;
            case "invoice":
                this.invoice = (String)val;
                break;
            case "paymentExternalId":
                this.paymentExternalId = (String)val;
                break;
            case "skuGiven":
                this.skuGiven = (String)val;
                break;
            case "skuRequested":
                this.skuRequested = (String)val;
                break;
            case "payer":
                this.payer = val;
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
        out.writeObject(this.amount);
        out.writeObject(this.approvalDate);
        out.writeObject(this.creationDate);
        out.writeObject(this.executeUrl);
        out.writeObject(this.executionDate);
        out.writeObject(this.invoice);
        out.writeObject(this.paymentExternalId);
        out.writeObject(this.skuGiven);
        out.writeObject(this.skuRequested);
        out.writeObject(this.payer);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.amount = (String)in.readObject();
        this.approvalDate = (Date)in.readObject();
        this.creationDate = (String)in.readObject();
        this.executeUrl = (String)in.readObject();
        this.executionDate = (Date)in.readObject();
        this.invoice = (String)in.readObject();
        this.paymentExternalId = (String)in.readObject();
        this.skuGiven = (String)in.readObject();
        this.skuRequested = (String)in.readObject();
        this.payer = in.readObject();
    }

}
