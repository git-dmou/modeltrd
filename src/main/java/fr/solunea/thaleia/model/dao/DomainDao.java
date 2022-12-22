package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Domain;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

import java.util.List;
import java.util.Locale;

/**
 * <p>DomainDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class DomainDao extends CayenneDao<Domain> {

    public DomainDao(ObjectContext context) {
        super(context);
    }

    public List<Domain> findDomainsByPartialName(String partialDomainName) {

        Expression qualifier = ExpressionFactory.likeIgnoreCaseExp(
                Domain.NAME.getName(),
                "%" + partialDomainName + "%");
        SelectQuery select = new SelectQuery(Domain.class, qualifier);
        List domains = context.performQuery(select);
        return domains;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName(Domain object, Locale locale) {
        return object.getName();
    }

    /**
     * <p>findByName.</p>
     *
     * @param name le nom
     * @return la liste des domaines portant ce nom.
     */
    public List<Domain> findByName(String name) {
        return findMatchByProperty(Domain.NAME.getName(), name);
    }

    /**
     * <p>existsWithName.</p>
     *
     * @param name   le nom
     * @param domain a {@link fr.solunea.thaleia.model.Domain} object.
     * @return true si un Domaine existe avec ce nom, à l'exclusion du domaine passé en paramètre.
     */
    public boolean existsWithName(String name, Domain domain) {
        if (name == null || domain == null) {
            return false;
        }

        // On recherche tous les domaines portant ce nom
        List<Domain> domains = findByName(name);

        // Est-ce que le domaine est dans cette liste ?
        if (domain.equals(domains)) {
            // S'il est seul, alors il n'y a pas d'autre domaine dans la liste
            return domains.size() != 1;
        } else {
            // Le domaine à exclure n'est pas dans la liste

            // Si la liste est vide, le problème est réglé
            return domains.size() != 0;
        }

        // return containsObjects(domains, domain);
    }

    /**
     * <p>find.</p>
     *
     * @param excluding le domaine qui ne doit pas faire partie de la liste renvoyée.
     * @return la liste de tous les Domaines existants, sauf celui passé en paramètre.
     */
    public List<Domain> find(Domain excluding) {
        List<Domain> result = find();
        result.remove(excluding);
        return result;
    }

}
