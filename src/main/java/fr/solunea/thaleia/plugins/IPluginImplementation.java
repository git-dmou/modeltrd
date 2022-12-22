package fr.solunea.thaleia.plugins;

import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.utils.DetailedException;

import java.util.Locale;

/**
 * <p>IPluginImplementation interface.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public interface IPluginImplementation {

    /**
     * <p>getName.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     * @return le nom du plugin
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    String getName(Locale locale) throws DetailedException;

    /**
     * <p>getDescription.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     * @return la description du plugin
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    String getDescription(Locale locale) throws DetailedException;

    /**
     * <p>getVersion.</p>
     *
     * @param locale a {@link java.util.Locale} object.
     * @return la description du plugin
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    String getVersion(Locale locale) throws DetailedException;

    /**
     * <p>getPage.</p>
     *
     * @return la classe de la page d'accueil à présenter au lancement du plugin.
     */
    Class<?> getPage();

    /**
     * <p>getDetailsPage.</p>
     *
     * @return la classe de la page d'accueil à présenter pour détailler le fonctionnement du plugin.
     */
    Class<?> getDetailsPage();

    /**
     * <p>getImageAsPng.</p>
     *
     * @return une image, un format PNG, qui illustre le plugin.
     */
    byte[] getImageAsPng();

    /**
     * @return true si ce contenu peut être édité par ce plugin.
     */
    boolean canEdit(Content content);

    void onInstalled() throws DetailedException;
}
