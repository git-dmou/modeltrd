package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._PublicationSession;

import java.io.Serializable;
import java.util.Calendar;

public class PublicationSession extends _PublicationSession implements Serializable {

    public boolean isValid() {
        if (getValidUntil().before(Calendar.getInstance().getTime())) {
            return false;
        }

        return true;
    }

}
