package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._LrsEndpointStatement;

import java.io.Serializable;

public class LrsEndpointStatement extends _LrsEndpointStatement implements Serializable {

    public String getName() {
        // TODO récupérer l'id dans la chaîne JSON
        return this.getObjectId().toString();
    }

}
