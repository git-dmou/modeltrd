package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._LrsEndpoint;

import java.io.Serializable;

public class LrsEndpoint extends _LrsEndpoint implements Serializable {

    @Override
    public String toString() {
        return getName();
    }

}
