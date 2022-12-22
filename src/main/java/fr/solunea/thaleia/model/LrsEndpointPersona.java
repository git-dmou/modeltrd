package fr.solunea.thaleia.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import fr.solunea.thaleia.model.auto._LrsEndpointPersona;
import fr.solunea.thaleia.utils.DetailedException;

import java.io.Serializable;

public class LrsEndpointPersona extends _LrsEndpointPersona implements Serializable {

    /**
     * @return la valeur de l'attribut "_id" déclaré dans ce persona.
     */
    public String getPersonaId() throws DetailedException {

        if (getPersona() == null || getPersona().isEmpty()) {
            throw new DetailedException("La persona est vide.");

        } else {
            // On parse le persona en tant que JSON
            JsonParser parser = new JsonParser();
            JsonObject persona;
            try {
                persona = parser.parse(getPersona()).getAsJsonObject();
            } catch (JsonSyntaxException e) {
                throw new DetailedException(e).addMessage("La persona ne peut pas être interprétée en tant que JSON.");
            }

            // On recherche l'attribut _id
            JsonPrimitive id = persona.getAsJsonPrimitive("_id");
            if (id == null) {
                throw new DetailedException("Pas d'attribut '_id' dans le persona.");
            } else {
                return id.getAsString();
            }
        }

    }
}
