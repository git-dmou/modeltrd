package fr.solunea.thaleia.service.utils.classes;

/**
 * Teste si une classe hérite d'une autre.
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class ExtendsTester implements IClassesTester {

	/** {@inheritDoc} */
	@Override
	public boolean matches(Class<?> tested, Class<?> compareTo) {
		return compareTo.isAssignableFrom(tested);
	}
}
