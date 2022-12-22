package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Content;
import fr.solunea.thaleia.model.ContentPropertyValue;
import fr.solunea.thaleia.model.EditedContent;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.ObjectContext;

import java.util.List;
import java.util.Locale;

/**
 * <p>EditedContentDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class EditedContentDao extends CayenneDao<EditedContent> {

    public EditedContentDao(ObjectContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(EditedContent object, Locale locale) {
        return Integer.toString(getPK(object));
    }

    /**
     * <p>deleteAll.</p>
     *
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    public void deleteAll() throws DetailedException {
        try {
            List<EditedContent> existing = find();
            for (EditedContent anExisting : existing) {
                delete(anExisting);
            }

        } catch (DetailedException e) {
            throw new DetailedException(e).addMessage("Impossible de supprimer les EditedContent.");
        }

    }

    /**
     * <p>findByContent.</p>
     *
     * @param content a {@link fr.solunea.thaleia.model.Content} object.
     * @return le EditedContent de ce contenu, ou null si aucun n'existe.
     */
    public EditedContent findByContent(Content content) {
        List<EditedContent> editedContents = find();
        for (EditedContent editedContent : editedContents) {
            if (editedContent.getContentPropertyValue().getContentVersion().getContent().equals(content)) {
                return editedContent;
            }
        }
        return null;
    }

    /**
     * @return le premier EditedContent trouvé pour ce contenu ET cet auteur, ou null si aucun n'est trouvé.
     */
    public EditedContent findByContentAndUser(Content content, User user) {

        if (content == null || user == null) {
            return null;
        }

        List<EditedContent> editedContents = find();
        for (EditedContent editedContent : editedContents) {
            if (editedContent.getContentPropertyValue().getContentVersion().getContent().equals(content)
                    && editedContent.getAuthor().equals(user)) {
                return editedContent;
            }
        }
        return null;
    }

    /**
     * <p>contentIsEdited.</p>
     *
     * @param content a {@link fr.solunea.thaleia.model.Content} object.
     * @return true si le contenu est actuellement associé à un EditedContent
     */
    public boolean contentIsEdited(Content content) {
        EditedContent editedContent = findByContent(content);
        return editedContent != null;
    }

    /**
     * <p>findByContentPropertyValue.</p>
     *
     * @param contentPropertyValue a {@link fr.solunea.thaleia.model.ContentPropertyValue} object.
     * @return le EditedContent qui est actuellement ouvert pour l'édition de cette valeur de ContentPorperty. Null si
     * aucune trouvée.
     */
    public EditedContent findByContentPropertyValue(ContentPropertyValue contentPropertyValue) {
        List<EditedContent> editedContents = find();
        for (EditedContent editedContent : editedContents) {
            if (editedContent.getContentPropertyValue().equals(contentPropertyValue)) {
                return editedContent;
            }
        }
        return null;
    }

}
