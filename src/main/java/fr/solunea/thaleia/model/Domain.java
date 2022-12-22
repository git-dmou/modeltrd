package fr.solunea.thaleia.model;

import fr.solunea.thaleia.model.auto._Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Domain class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public class Domain extends _Domain implements Serializable {

    /**
     * <p>getAllChilds.</p>
     *
     * @return la liste de tous les fils de ce domaine, de manière récursive.
     */
    public Collection<Domain> getAllChilds() {
        List<Domain> result = new ArrayList<>();

        for (Domain child : this.getChilds()) {
            result.add(child);
            result.addAll(child.getAllChilds());
        }

        return result;
    }

    /**
     * On compare les "name" des domains afin de s'assurer qu'ils soient les mêmes.
     * On ne compare pas les objets à cause des contextes Cayenne différents.
     * @param domains Liste des domaines à tester.
     * @return Boolean
     */
    public boolean equals(List<Domain> domains) {
        Boolean result = false;
        for (Domain temp : domains) {
            if (this.getName().equals(temp.getName())) {
                result = true;
            }
        }
        return result;
    }

}
