package fr.solunea.thaleia.utils;

/**
 * <p>LogUtils class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class LogUtils {

    /**
     * <p>getCallerInfo.</p>
     *
     * @param n pour n=2, on obtient le point d'appel à getCallerInfo(). Pour
     *          n=3, on obtient le point d'appel du bout de code qui contient
     *          l'appel à getCallerInfo().
     * @return une chaîne de caractère qui présente le point d'appel en
     * remontant n niveaux dans la pile d'appels.
     */
    public static String getCallerInfo(int n) {
        int level = n;
        if (level < 0) {
            level = 0;
        }
        if (level > Thread.currentThread().getStackTrace().length) {
            level = Thread.currentThread().getStackTrace().length;
        }

        // cette méthode va remonter de 3 niveaux pour trouver le bon lieu de
        // l'appel.
        int lineNumber = Thread.currentThread().getStackTrace()[level].getLineNumber();
        String fileName = Thread.currentThread().getStackTrace()[level].getFileName();
        String className = Thread.currentThread().getStackTrace()[level].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[level].getMethodName();

        return fileName + "|" + className + "|" + methodName + "|" + lineNumber;
    }

    /**
     * <p>getStackTrace.</p>
     *
     * @return la String qui détaille la pile d'appel du Thread.
     */
    public static String getStackTrace() {
        return getStackTrace(Thread.currentThread().getStackTrace());

    }

    /**
     * <p>getStackTrace.</p>
     *
     * @param stackTrace an array of {@link java.lang.StackTraceElement} objects.
     * @return la String qui détaille la pile d'appel
     */
    public static String getStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder stackString = new StringBuilder();
        for (StackTraceElement aStackTrace : stackTrace) {
            stackString.append(aStackTrace.toString()).append("\n");
        }
        return stackString.toString();
    }

}
