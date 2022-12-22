package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Domain;
import fr.solunea.thaleia.model.DomainRight;
import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.ObjectContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>DomainRightDao class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class DomainRightDao extends CayenneDao<DomainRight> {

    public DomainRightDao(ObjectContext context) {
        super(context);
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName(DomainRight object, Locale locale) {
        return object.getRightOwner().getName() + " -> " + object.getRightOn().getName();
    }

    /**
     * <p>getAccessibleDomains.</p>
     *
     * @param domain a {@link fr.solunea.thaleia.model.Domain} object.
     * @return la liste de tous les domaines sur lesquel ce domaine a les droits
     * d'accès. Cette liste NE CONTIENT PAS les domaines FILS du domaine
     * courant, mais que les domaines associés par un DomainRight. Cette
     * liste ne contient pas non plus les fils des domaines sur lesquels
     * ce domaine a les droits d'accès.
     */
    public List<Domain> getAccessibleDomains(Domain domain) {
        List<Domain> result = new ArrayList<>();

        if (domain != null) {

            try {
                List<DomainRight> domainRights = domain.getRightOn();
                for (DomainRight right : domainRights) {
                result.add(right.getRightOn());
                }
            } catch (Exception e) {
                // S'il est impossible de préparer la liste, on la renvoie en
                // l'état.
                // Cela peut arriver si l'objet domaine est transient, ou
                // hollow.
            }
        }

        return result;
    }

    /**
     * Ajoute cette liste des domaines accessibles pour ce domaine. Si ces
     * droits existent déjà, ils ne sont pas modifiés.
     *
     * @param domain            a {@link fr.solunea.thaleia.model.Domain} object.
     * @param accessibleDomains a {@link java.util.List} object.
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    public void addAccessible(Domain domain, List<Domain> accessibleDomains) throws DetailedException {

        // Pour chacun des domaines à ajouter
        for (Domain accessibleDomain : accessibleDomains) {
            // Si pas déjà de droit, on l'ajoute
            if (!getAccessibleDomains(domain).contains(accessibleDomain)) {
                DomainRight domainRight = get();
                domainRight.setRightOn(accessibleDomain);
                domainRight.setRightOwner(domain);
                save(domainRight);
            }
        }

    }

    /**
     * Remplace la liste des domaines accessibles existantes pour ce domaine,
     * par cette nouvelle liste. Les modifications ne sont pas commitées dans le contexte.
     *
     */
    public void setAccessible(Domain domain, List<Domain> accessibleDomains) throws DetailedException {
        try {
            // Suppression des domaines existants
            deleteAccessibleDomains(domain);

            // Assocation des nouveaux domaines
            // On commence par le premier numéro d'allocation (1, ou 0), et on
            // parcourt toute la lsite en incrémentant cet indice
            for (Domain accessibleDomain : accessibleDomains) {
                DomainRight right = get();
                right.setRightOn(accessibleDomain);
                right.setRightOwner(domain);
            }

        } catch (DetailedException e) {
            throw new DetailedException(e).addMessage(
                    "Impossible de remplacer les accessibilités de " + domain + " par " + accessibleDomains);
        }

    }

    /**
     * Supprime les droits d'accès à tous les domaines pour ce domaine. Ces
     * domaines ne sont pas supprimés. Les modifications ne sont pas commitées dans le contexte.
     */
    private void deleteAccessibleDomains(Domain domain) throws DetailedException {
        List<DomainRight> toDelete = new ArrayList<>();

        try {
            List<DomainRight> rigths = domain.getRightOn();
            toDelete.addAll(rigths);

        } catch (Exception e) {
            // Une erreur se lève si l'objet est transient. Il n'y a dans ce cas
            // pas d'objets dans la liste : on peut ignorer l'erreur.
        }

        try {
            for (DomainRight right : toDelete) {
                domain.removeFromRightGivenTo(right);
                delete(right, false);
            }

        } catch (DetailedException e) {
            throw new DetailedException(e).addMessage(
                    "Impossible de supprimer les droits d'accès aux domaines de " + domain);
        }

    }
}
