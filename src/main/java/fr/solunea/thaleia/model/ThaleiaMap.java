package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._ThaleiaMap;

import java.io.Serializable;

/**
 * <p>ThaleiaMap class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public final class ThaleiaMap extends _ThaleiaMap implements Serializable {

    private final static ThaleiaMap instance = new ThaleiaMap();

    private ThaleiaMap() {
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link fr.solunea.thaleia.model.ThaleiaMap} object.
     */
    public static ThaleiaMap getInstance() {
        return instance;
    }
}
