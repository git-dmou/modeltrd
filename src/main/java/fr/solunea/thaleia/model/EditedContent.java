package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._EditedContent;

import java.io.File;
import java.io.Serializable;

/**
 * <p>EditedContent class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class EditedContent extends _EditedContent implements Serializable {

    /**
     * Fixe le répertoire qui contient un répertoire de prévisualisation associé
     * à ce EditedContent.
     * <p>setPreviewDirectory.</p>
     *
     * @param previewDirectory le répertoire.
     */
    public void setPreviewDirectory(File previewDirectory) {
        super.setPreviewDirectory(previewDirectory.getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Fixe le chemin absolu du répertoire de prévisualisation associé à ce
     * EditedContent
     */
    @Override
    public void setPreviewDirectory(String previewDirectory) {
        writeProperty(PREVIEW_DIRECTORY.getName(), previewDirectory);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Le chemin absolu du répertoire de prévisualisation associé à ce
     * EditedContent
     */
    @Override
    public String getPreviewDirectory() {
        return (String) readProperty(PREVIEW_DIRECTORY.getName());
    }

}
