/**
 * Le code source, le matériel préparatoire et la documentation de ce
 * logiciel sont la propriété exclusive de la société Solunea, au titre
 * du droit de propriété intellectuelle. Ces éléments ont fait l'objet
 * de dépôts probatoires.
 * <p>
 * À défaut d'accord préalable écrit de Solunea, vous ne devez pas
 * utiliser, copier, modifier, traduire, créer une œuvre dérivée,
 * transmettre, vendre ou distribuer, de manière directe ou indirecte,
 * inverser la conception ou l'assemblage ou tenter de trouver le code
 * source (sauf cas prévus par la loi), ou transférer tout droit relatif
 * audit logiciel.
 * <p>
 * Solunea
 * SARL - N° SIRET 48795234300027
 */
package fr.solunea.thaleia.utils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DetailedException extends Exception {

    private List<InfoItem> infoItems = new ArrayList<>();

    protected class InfoItem {
        final String errorClass;
        final String errorFile;
        final String errorMethod;
        int lineNumber;
        public String message;

        InfoItem(String errorFile, String errorClass, String errorMethod, int lineNumber, String message) {

            this.errorFile = errorFile;
            this.errorClass = errorClass;
            this.errorMethod = errorMethod;
            this.lineNumber = lineNumber;
            this.message = message;
        }
    }

    /**
     * <p>Constructor for DetailedException.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public DetailedException(String message) {
        super(message);
        doAddMessage(message);
    }

    /**
     * <p>Constructor for DetailedException.</p>
     *
     * @param e a {@link java.lang.Exception} object.
     */
    public DetailedException(Exception e) {
        super(e.getMessage());
        if (e instanceof DetailedException) {
            this.infoItems = ((DetailedException) e).infoItems;

        } else {
            doAddMessage(e.toString());
            String stackTrace = LogUtils.getStackTrace(e.getStackTrace());
            doAddMessage(stackTrace);
        }
    }

    /**
     * Ajoute le message à la pile des messages de l'exception, et se renvoie.
     *
     * @param message a {@link java.lang.String} object.
     * @return elle-même
     */
    public DetailedException addMessage(String message) {
        doAddMessage(message);
        return this;
    }

    private void doAddMessage(String message) {
        // cette méthode va remonter de 3 niveaux pour trouver le bon lieu de
        // l'appel.
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        String fileName = Thread.currentThread().getStackTrace()[3].getFileName();
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        this.infoItems.add(new InfoItem(fileName, className, methodName, lineNumber, message));
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nDétail de l'exception :\n");
        // On présente la pile d'appel des infos
        for (InfoItem item : infoItems) {
            sb.append("     ");
            sb.append(item.errorClass);
            sb.append('.');
            sb.append(item.errorMethod);
            sb.append('(');
            sb.append(item.errorFile);
            sb.append(':');
            sb.append(item.lineNumber);
            sb.append(") : ");
            sb.append(item.message);
            sb.append('\n');
        }

        return sb.toString();
    }
}
