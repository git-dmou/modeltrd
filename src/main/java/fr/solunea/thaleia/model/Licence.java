package fr.solunea.thaleia.model;

import java.io.Serializable;

import fr.solunea.thaleia.model.auto._Licence;

/**
 * <p>Licence class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Licence extends _Licence implements Serializable {

	/**
	 * <p>getMaxSizeBytes.</p>
	 *
	 * @return la taille totale maximale des fichiers, exprimée en octets.
	 */
	public long getMaxSizeBytes() {
		// Conversion de Mo en o
		return Long.valueOf(getMaxSizeMo()) * 1048576L;
	}

}
