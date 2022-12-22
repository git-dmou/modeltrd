package fr.solunea.thaleia.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

import fr.solunea.thaleia.model.auto._Allocation;

/**
 * <p>Allocation class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Allocation extends _Allocation implements Serializable,
		Comparable<Allocation> {

	private static final Logger logger = Logger.getLogger(Allocation.class);

	/** {@inheritDoc} */
	@Override
	public int compareTo(Allocation o) {
		if (o == null) {
			logger.debug("Impossible de comparer '" + this + "' avec nul !");
			return 0;
		}

		try {
			if (this.getPosition() > o.getPosition()) {
				return 1;
			} else if (this.getPosition() < o.getPosition()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.debug("Impossible de comparer '" + this + "' avec '" + o
					+ "' :" + e);
			return 0;
		}
	}

}
