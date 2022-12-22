package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.utils.DetailedException;
import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.CayenneDataObject;

import java.util.List;
import java.util.Locale;

/**
 * <p>ICayenneDao interface.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public interface ICayenneDao<T extends BaseDataObject> {

    /**
     * <p>getDisplayName.</p>
     *
     * @param object l'objet
     * @param locale la locale qui sera utlisée pour présenter cet
     *               objet
     * @return le nom qui représente cet objet. getName() si l'objet a un
     * attribut name, ou l'équivalent si l'objet n'a pas d'attribut
     * name.
     */
    String getDisplayName(T object, Locale locale);

    /**
     * <p>getPK.</p>
     *
     * @param object a T object.
     * @return a int.
     */
    int getPK(T object);

    /**
     * <p>find.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<T> find();

    /**
     * @return les objets dont les id correspondent à la liste passée en paramètre.
     */
    List<T> find(List<Integer> activityIds);

    /**
     * <p>get.</p>
     *
     * @param id a int.
     * @return a T object.
     */
    T get(int id);

    /**
     * <p>get.</p>
     *
     * @return a T object.
     */
    T get();

    /**
     * <p>get.</p>
     *
     * @param attachedToContext doit-on l'attacher au contexte ?
     * @return un nouvel objet attaché au contexte. Attention : en cas
     * d'annulation, il faudra le détacher du contexte.
     */
    T get(boolean attachedToContext);

    /**
     * <p>save.</p>
     *
     * @param object a T object.
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    void save(T object) throws DetailedException;

    void save(T object, boolean commit) throws DetailedException;

    /**
     * <p>delete.</p>
     *
     * @param id a int.
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    void delete(int id) throws DetailedException;

    /**
     * <p>delete.</p>
     *
     * @param object a T object.
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    void delete(T object) throws DetailedException;

    /**
     * <p>delete.</p>
     *
     * @param object a T object.
     * @param commit a boolean.
     * @throws fr.solunea.thaleia.utils.DetailedException if any.
     */
    void delete(T object, boolean commit) throws DetailedException;

    /**
     * <p>isNewObject.</p>
     *
     * @param object l'objet
     * @return true si cet objet n'a pas encore été stocké en base.
     */
    boolean isNewObject(T object);

}
