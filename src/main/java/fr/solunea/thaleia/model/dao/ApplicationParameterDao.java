package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.ApplicationParameter;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * <p>ApplicationParameterDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class ApplicationParameterDao extends CayenneDao<ApplicationParameter> {

    /**
     * Le séparateur utilisé pour stocker plusieurs valeurs dans un champ.
     */
    private static final String VALUES_SEPARATOR = ";";

    public ApplicationParameterDao(ObjectContext context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(ApplicationParameter object, Locale locale) {
        return object.getName();
    }

    /**
     * <p>getValue.</p>
     *
     * @param parameterName le nom du paramètre
     * @param defaultValue  la valeur par défaut
     * @return la valeur de ce paramètre, ou la valeur par défaut si ce
     * paramètre n'a pas été fixé.
     */
    public String getValue(String parameterName, String defaultValue) {

        ApplicationParameter parameter = this.findFirstMatchByProperty(ApplicationParameter.NAME.getName(), parameterName);

        if (parameter == null) {
            return defaultValue;
        } else {
            return parameter.getValue();
        }
    }

    /**
     * <p>getValues.</p>
     *
     * @param parameterName le nom du paramètre
     * @param defaultValue  la valeur par défaut du paramètre recherché
     * @return la liste des valeurs contenus dans ce paramètre, considérant le
     * caractère VALUES_SEPARATOR comme le séparateur de valeurs.
     */
    @SuppressWarnings("unused")
    public List<String> getValues(String parameterName, String defaultValue) {

        List<String> result = new ArrayList<>();

        // On recherche la valeur du paramètre
        String allValues = getValue(parameterName, defaultValue);

        // On tokenize cette valeur
        StringTokenizer tokenizer = new StringTokenizer(allValues, VALUES_SEPARATOR);
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }

        return result;

    }

    /**
     * <p>findByNameStartingWith.</p>
     *
     * @param name le début du nom du paramètre
     * @return Tous les paramètres d'application trouvés dont le nom commence
     * par la chaîne demandée, ou une liste vide si aucun n'est trouvé.
     */
    public List<ApplicationParameter> findByNameStartingWith(String name) {

        String checkedString = name;
        if (checkedString == null) {
            checkedString = "";
        }

        try {
            SelectQuery query = new SelectQuery(ApplicationParameter.class, ExpressionFactory.likeExp
                    (ApplicationParameter.NAME.getName(),
                            checkedString + "%"));

            return CastUtils.castList(getContext().performQuery(query), ApplicationParameter.class);

        } catch (Exception e) {
            logger.warn(
                    "Impossible de récupére les ApplicationParameters commençant par '" + checkedString + "' : " + e);
            return new ArrayList<>();
        }
    }

    /**
     * <p>findByName.</p>
     *
     * @param name le nom du paramètre
     * @return Le premier paramètre d'application trouvé qui porte ce nom, ou
     * null s'il n'a pas été trouvée.
     */
    public ApplicationParameter findByName(String name) {

        String checkedString = name;
        if (checkedString == null) {
            checkedString = "";
        }

        try {
            SelectQuery query = new SelectQuery(ApplicationParameter.class, ExpressionFactory.matchExp
                    (ApplicationParameter.NAME.getName(), checkedString));

            List<ApplicationParameter> properties = CastUtils.castList(getContext().performQuery(query),
                    ApplicationParameter.class);

            if (properties.size() > 0) {
                return properties.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.warn("Impossible de récupérer le ApplicationParameter '" + checkedString + "' : " + e);
            return null;
        }
    }

}
