package fr.solunea.thaleia.model;

import java.io.Serializable;

import fr.solunea.thaleia.model.auto._ContentProperty;

/**
 * <p>ContentProperty class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class ContentProperty extends _ContentProperty implements Serializable {

	/**
	 * <p>getId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		return this.objectId.toString();
	}

}
