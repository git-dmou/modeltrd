package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Locale;
import fr.solunea.thaleia.utils.CastUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.List;

/**
 * <p>LocaleDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class LocaleDao extends CayenneDao<Locale> {

    /**
     * <p>Constructor for LocaleDao.</p>
     *
     * @param service a {@link fr.solunea.thaleia.model.dao.ICayenneContextService} object.
     */
    public LocaleDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(Locale object, java.util.Locale locale) {
        return object.getName();
    }

    /**
     * <p>existsWithName.</p>
     *
     * @param name   le nom
     * @param locale la locale
     * @return true si une Locale existe avec ce nom.
     */
    public boolean existsWithName(String name, Locale locale) {
        List<Locale> locales = findMatchByProperty(Locale.NAME.getName(), name);

        return containsObjects(locales, locale);
    }

    /**
     * <p>getLocale.</p>
     *
     * @param javaLocale la locale
     * @return la locale de la base qui correspond à cette locale système. Null
     * s'il n'existe en base aucune locale correspondante à cette locale
     * système
     */
    public Locale getLocale(java.util.Locale javaLocale) {
        // On considère que le nom de la locale en base est le nom de la langue
        String lookFor = javaLocale.getLanguage();

        return findFirstMatchByProperty(Locale.NAME.getName(), lookFor);
    }

    /**
     * <p>getJavaLocale.</p>
     *
     * @param locale a {@link fr.solunea.thaleia.model.Locale} object.
     * @return la locale système qui correspond à cette locale de la base. Null
     * s'il n'existe pas de correspondance
     */
    public java.util.Locale getJavaLocale(Locale locale) {
        // On considère que le nom de la locale en base est le nom de la langue
        return new java.util.Locale(locale.getName());
    }

    /**
     * <p>findByName.</p>
     *
     * @param name le nom
     * @return la première Locale trouvée qui porte ce nom, ou null si elle n'a
     * pas été trouvée.
     */
    public Locale findByName(String name) {

        String checkedName = name;
        if (checkedName == null) {
            checkedName = "";
        }

        try {
            SelectQuery query = new SelectQuery(Locale.class, ExpressionFactory.matchExp(Locale.NAME.getName(),
					checkedName));

            List<Locale> locales = CastUtils.castList(getContext().performQuery(query), Locale.class);

            if (locales.size() > 0) {
                return locales.get(0);
            } else {
                return null;
            }

        } catch (Exception e) {
            logger.warn("Impossible de récupérer la Locale '" + checkedName + "' : " + e);
            return null;
        }
    }
}
