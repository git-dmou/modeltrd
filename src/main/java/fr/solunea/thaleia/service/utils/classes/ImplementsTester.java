package fr.solunea.thaleia.service.utils.classes;

import org.apache.log4j.Logger;

/**
 * Teste si une classe implémente une interface.
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class ImplementsTester implements IClassesTester {

    /** Constant <code>logger</code> */
    protected static final Logger logger = Logger.getLogger(ImplementsTester.class.getName());

    /** {@inheritDoc} */
    @Override
    public boolean matches(Class<?> tested, Class<?> compareTo) {
        Class<?>[] interfaces = tested.getInterfaces();
        // logger.debug("Interfaces de la classe '" + tested.getName() + "' :"
        // + interfaces);
        for (Class<?> anInterface : interfaces)
            if (anInterface.equals(compareTo)) {
                return true;
            }
        return false;
    }

}
