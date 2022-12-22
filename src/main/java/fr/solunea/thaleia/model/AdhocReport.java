package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._AdhocReport;

import java.io.Serializable;

public class AdhocReport extends _AdhocReport implements Serializable {

    public static DeliveryMode getCheckedDeliveryMode(String deliveryMode) {
        // Pour assurer la rétrocompatilibilé des données, on considère que l'absence de valeur ou une valeur vide
        // équivaut à "email".

        if ("sftp".equalsIgnoreCase(deliveryMode)) {
            return DeliveryMode.SFTP;
        } else {
            return DeliveryMode.Email;
        }
    }

    public static DeliveryFormat getCheckedDeliveryFormat(String deliveryFormat) {
        // Pour assurer la rétrocompatilibilé des données, on considère que l'absence de valeur ou une valeur vide
        // équivaut à "excel".

        if ("csv".equalsIgnoreCase(deliveryFormat)) {
            return DeliveryFormat.CSV;
        } else {
            return DeliveryFormat.Excel;
        }
    }

    public DeliveryMode getCheckedDeliveryMode() {
        return getCheckedDeliveryMode(getDeliveryMode());
    }

    public DeliveryFormat getCheckedDeliveryFormat() {
        return getCheckedDeliveryFormat(getDeliveryFormat());
    }

    public enum DeliveryMode {
        Email,
        SFTP
    }

    public enum DeliveryFormat {
        Excel,
        CSV
    }

}
