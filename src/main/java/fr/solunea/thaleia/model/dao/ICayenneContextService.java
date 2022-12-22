package fr.solunea.thaleia.model.dao;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.ObjectContext;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * Permet l'accès à un contexte de persistance Cayenne.
 */
public interface ICayenneContextService extends Serializable {

    /**
     * @return le contexte de persistance Cayenne, conservé dans un singleton.
     */
    ObjectContext getContextSingleton();

    /**
     * @return un nouveau contexte de persistance Cayenne.
     */
    ObjectContext getNewContext();

    void close();

    ObjectContext getChildContext(ObjectContext parentContext);

    /**
     * @param clazz la classe dont il faut instancier un nouvel objet
     * @return un nouvel objet de cette classe, associé à un contexte spécifique, pour permettre de l'éditer indépendamment des autres.
     */
    <T extends BaseDataObject> T getNewInNewContext(Class<T> clazz);

    /**
     * @param source l'objet dont on veut une autre instance, pour édition dans un contexte propre
     * @return ce nouvel objet, associé à un contexte spécifique, pour permettre de l'éditer indépendamment des autres.
     */
    <T extends BaseDataObject> T getObjectInNewContext(T source);

     /**
      * Procède à la suppression et au commit en base de cette suppression, dans un contexte Cayenne propre à l'opération.
     */
    <T extends BaseDataObject> void safeDelete(T object);
}
