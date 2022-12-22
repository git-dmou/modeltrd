package fr.solunea.thaleia.service.utils.classes;

/**
 * <p>IClassesTester interface.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public interface IClassesTester {

	/**
	 * <p>matches.</p>
	 *
	 * @param tested a {@link java.lang.Class} object.
	 * @param compareTo a {@link java.lang.Class} object.
	 * @return true si tested répond au critère recherché par rapport à
	 *         compareTo.
	 */
	boolean matches(Class<?> tested, Class<?> compareTo);

}
