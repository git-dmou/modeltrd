package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.utils.CastUtils;
import fr.solunea.thaleia.utils.DetailedException;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.*;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.map.DeleteRule;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.cayenne.reflect.*;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DAO générique pour tous les objets peristants Cayenne. <br> Pour l'utiliser : créer une classe en héritant, en
 * indiquant quel classe d'objet persistant est à manipumler.<br> <br> Par exemple : <br> {@code public class
 * ExpandedFilesDao extends CayenneDao<Expandedfile> }
 *
 * @param <T> la classe traitée par le DAO.
 * @author RMAR
 * @version $Id: $Id
 */
@SuppressWarnings("serial")
public abstract class CayenneDao<T extends BaseDataObject> implements Serializable, ICayenneDao<T> {

    /**
     * Constant <code>logger</code>
     */
    protected static final Logger logger = Logger.getLogger(CayenneDao.class.getName());
    protected final ObjectContext context;
    private final Cache<T> cache = new Cache<>(this);
    Class<T> dataObjectClass;

    @SuppressWarnings("unchecked")
    public CayenneDao(ObjectContext context) {
        this.context = context;

        // pour une classe héritant de CayenneDao, on récupère la classe des
        // objets persistants demandés
        try {
            this.dataObjectClass =
                    (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (Exception e) {
            logger.debug(
                    "Impossible de récupérer la classe de paramètre pour le dao '" + this.getClass().getName() + "' : "
                            + e.toString());
            // On la laisse nulle. La sous-classe de Dao doit surdéfinir cette
            // récupération.
        }
    }

    public Cache<T> getCache() {
        return cache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNewObject(T object) {

        unHollowObjects(object);

        return object != null && !isCommitedInDatabase(object);

    }

    /**
     * <p>getContext.</p>
     *
     * @return a {@link org.apache.cayenne.ObjectContext} object.
     */
    protected ObjectContext getContext() {
        if (context != null) {
            return context;
        }
        logger.error("Pas de service d'accès au contexte disponible !");
        return null;
    }

    /**
     * <p>containsObjects.</p>
     *
     * @param objects             la liste d'objets à étudier
     * @param excludingThisObject l'objet à exclure
     * @return true si la liste contient au moins un object qui n'est PAS l'objet passé en paramètre.
     */
    boolean containsObjects(List<T> objects, T excludingThisObject) {

        unHollowObjectsList(objects);
        unHollowObjects(excludingThisObject);

        // Est-ce que l'objet est dans cette liste ?
        if (objects.contains(excludingThisObject)) {
            // S'il est seul, alors il n'y a pas d'autre objet dans la liste
            return objects.size() != 1;

        } else {
            // L'objet à exclure n'est pas dans la liste

            // Si la liste est vide, le problème est réglé
            return objects.size() != 0;
        }
    }

    /**
     * <p>isCommitedInDatabase.</p>
     *
     * @param object l'objet.
     * @return true si cet objet a déjà été enregistré en base, c'est à dire n'est pas un objet temporaire en cours de
     * création.
     */
    public boolean isCommitedInDatabase(T object) {

        unHollowObjects(object);

        return object.getPersistenceState() != PersistenceState.TRANSIENT
                && object.getPersistenceState() != PersistenceState.NEW;
    }

    /**
     * <p>getTableName.</p>
     *
     * @return le nom de la table qui stocke les objets de la classe de ce DAO.
     * @throws fr.solunea.thaleia.utils.DetailedException si erreur
     */
    @SuppressWarnings("unused")
    protected String getTableName() throws DetailedException {
        try {
            return getContext().getEntityResolver().getObjEntity(dataObjectClass.getSimpleName()).getDbEntityName();

        } catch (Exception e) {
            throw new DetailedException(
                    "Impossible de retrouver le nom de la table des objets '" + dataObjectClass.getName() + "' : "
                            + e.toString());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Supprime cet objet, et commite les modifications en base.
     */
    @Override
    public void delete(T object) throws DetailedException {
        if (object != null) {
            unHollowObjects(object);
            delete(object, true);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Marque cet objet pour être supprimé au prochain enregistrement. Si commit = true, alors déclenche un
     * enregsitrement en base de la modification, sinon uniquement dans le contexte du DAO.
     */
    @Override
    public void delete(T object, boolean commit) throws DetailedException {

        unHollowObjects(object);

        if (object != null) {
            try {
                getContext().deleteObjects(object);

            } catch (Exception e) {
                logger.warn("Erreur de suppression :" + e.toString());
                rollback();
                throw new DetailedException(e.toString());
            }

            if (commit) {
                commit();
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Supprime l'objet dont la PK est id.
     */
    @Override
    public void delete(int id) throws DetailedException {
        Persistent object = get(id);

        if (object != null) {
            try {
                getContext().deleteObjects(object);
            } catch (Exception e) {
                logger.warn("Erreur de suppression :" + e.toString());
                rollback();
                throw new DetailedException(e.toString());
            }
            commit();
        }
    }

    /**
     * Supprime tous les objets dont l'identifiant est dans la liste.
     *
     * @param commit si false, alors les modifications ne sont pas stockées en base.
     */
    public void delete(List<Integer> objectIds, boolean commit) throws DetailedException {
        try {
            for (Integer id : objectIds) {
                try {
                    delete(get(id), commit);
                } catch (DetailedException e) {
                    throw e.addMessage("Impossible de supprimer l'objet id = " + id);
                }
            }
        } catch (DetailedException e) {
            throw e.addMessage("Impossible de supprimer les objets.");
        }
    }

    /**
     * Supprime tous les objets dont l'identifiant est dans la liste.
     */
    public void delete(List<Integer> objectIds) throws DetailedException {
        delete(objectIds, true);
    }

    /**
     * Supprime toutes les modifications non commitées du contexte.
     */
    public void rollback() {
        // On journalise les objets à commiter
        Collection<?> toCommit = getContext().uncommittedObjects();
        logger.debug(toCommit.size() + " objets non commités.");
        for (Object object : toCommit) {
            logger.debug(object.toString());
        }

        logger.info("Les données modifiées du contexte sont supprimées des données à commiter !");
        getContext().rollbackChanges();
        logger.debug(getContext().uncommittedObjects().size() + " objets non commités.");
    }

    /**
     * Tente de commiter tous les objets modifiés du contexte.
     *
     * @throws fr.solunea.thaleia.utils.DetailedException si erreur
     */
    protected void commit() throws DetailedException {
        try {
            // On journalise la pile d'appel
            // logger.debug(LogUtils.getStackTrace(Thread.currentThread()
            // .getStackTrace()));

            // On journalise les objets à commiter
            Collection<?> toCommit = getContext().uncommittedObjects();
            logger.debug(toCommit.size() + " objets à commiter : " + toCommit.toString());

            getContext().commitChanges();

        } catch (CayenneRuntimeException c) {
            // pour remonter les messages envoyés par Postgresql
            // ce qui simplifie l'analyse des problèmes
            //
            // par exemple :
            // Commit Exception
            // ERREUR: la valeur d'une clé dupliquée rompt la contrainte unique « stat_data_pkey »
            //  Détail : La clé « (id)=(280) » existe déjà.
            //
            logger.debug("Erreur de commit :" + c.getMessage());
            logger.debug(c.getUnlabeledMessage());
            logger.debug(c.getCause().getMessage());
            throw new DetailedException(c);
        } catch (Exception e) {
            logger.debug("Erreur de commit :" + e.getMessage());
            logger.debug(e.getCause().getMessage());
            logger.warn(e.toString());
            logger.debug(LogUtils.getStackTrace(e.getStackTrace()));
            rollback();
            throw new DetailedException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(T object) throws DetailedException {
        save(object, true);
    }

    @Override
    public void save(T object, boolean commit) throws DetailedException {
        registerObject(object);
        if (commit) {
            commit();
        }
    }

    public void registerObject(T object) throws DetailedException {
        unHollowObjects(object);
        try {
            // logger.debug("Enregistrement de l'objet comme persistant...");
            if (object.getPersistenceState() == PersistenceState.TRANSIENT) {
                getContext().registerNewObject(object);
            }

        } catch (Exception e) {
            logger.warn("Impossible d'enregistrer le nouvel objet :" + e.toString());
            rollback();
            throw new DetailedException(e.toString());
        }
    }

    public void commitRegisteredObjects() throws DetailedException {
        commit();
    }

    public int getUncommitedObjectsCount() {
        return getContext().uncommittedObjects().size();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.solunea.ts.model.dao.ICayenneDao#get()
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        try {
            // On créé un objet qui n'est pas attaché au contexte.
            // Il sera attaché lors de l'appel de save().
            return dataObjectClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            logger.error("Problème lors de l'instanciation d'un objet pour DAO Cayenne : " + e.toString());
            return null;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see fr.solunea.thaleia.model.dao.ICayenneDao#get(boolean)
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(boolean attachedToContext) {
        if (attachedToContext) {
            // On renvoie un objet attaché à ce contexte
            // En cas d'annulation de l'édition de cet objet, il faudra penser à
            // le détacher
            return getContext().newObject(dataObjectClass);
        } else {
            return get();
        }
    }

    /**
     * <p>get.</p>
     *
     * @param id l'id de l'objet.
     * @return l'objet correspondant à cet ObjectId, ou null si l'objet n'a pas été trouvé.
     */
    @SuppressWarnings("unchecked")
    public T get(ObjectId id) {
        try {
            // logger.debug("Recherche de l'objet id='" + id + "'...");
            DataObject result = (T) Cayenne.objectForPK(getContext(), id);

            if (result == null) {
                logger.debug(
                        "Recherche de l'objet de type '" + dataObjectClass.getName() + "' pour id='" + id + "' " + ":"
                                + " trouvé null !");
                logger.debug(LogUtils.getStackTrace(Thread.currentThread().getStackTrace()));
                return null;
            }


            // On vérifie que l'objet est de la bonne classe avant de le caster
            if (dataObjectClass.isAssignableFrom(result.getClass())) {
                return (T) result;
            } else {
                throw new Exception(
                        "L'objet '' renvoyé par le contexte n'est pas de la classe '" + dataObjectClass.getName()
                                + "' !");
            }

        } catch (Exception e) {
            logger.error("Impossible d'effectuer la recherche de l'id " + id + " : " + e.toString());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T get(int id) {
        try {
            // logger.debug("Recherche de l'objet id='" + id + "'...");
            DataObject result = Cayenne.objectForPK(getContext(), dataObjectClass, id);

            if (result == null) {
                logger.debug(
                        "Recherche de l'objet de type '" + dataObjectClass.getName() + "' pour id='" + id + "' " + ":"
                                + " trouvé null !");
                return null;
            }


            // On vérifie que l'objet est de la bonne classe avant de le caster
            if (dataObjectClass != null) {
                if (dataObjectClass.isAssignableFrom(result.getClass())) {
                    return (T) result;
                } else {
                    throw new Exception(
                            "L'objet '' renvoyé par le contexte n'est pas de la classe '" + dataObjectClass.getName()
                                    + "' !");
                }
            } else {
                throw new DetailedException("Le dataObjectClass de ce DAO n'est pas affecté !");
            }

        } catch (Exception e) {
            logger.debug(
                    "Impossible d'effectuer la recherche de l'id " + id + " : " + e + "\n" + LogUtils.getCallerInfo(3));
            return null;
        }
    }

    /**
     * <p>likeByProperty.</p>
     *
     * @param property la propriété
     * @param value    la valeur
     * @return les objets qui répondent à "like" sur cette propriété.
     */
    @SuppressWarnings("unused")
    protected List<T> likeByProperty(String property, Object value) {

        List<T> result;
        try {
            Expression qual = ExpressionFactory.likeIgnoreCaseExp(property, value);
            SelectQuery query = new SelectQuery(dataObjectClass, qual);

            result = CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }

    /**
     * <p>findMatchByProperty.</p>
     * <p>
     * Attention : le requestCache ne fonctionne que si la valeur est de type String
     *
     * @param property la propriété
     * @param value    la valeur
     * @return les objets qui répondent à "match" sur cette propriété.
     */
    protected List<T> findMatchByProperty(String property, Object value, long cacheDurationInSeconds) {

        if (value.getClass().isAssignableFrom(String.class)) {
            return getCache().getObjects(property, (String) value, cacheDurationInSeconds);
        } else {
            return findMatchByProperty(property, value);
        }
    }

    /**
     * <p>findMatchByProperty.</p>
     *
     * @param property la propriété
     * @param value    la valeur
     * @return les objets qui répondent à "match" sur cette propriété.
     */
    protected List<T> findMatchByProperty(String property, Object value) {
        try {
            Expression qual = ExpressionFactory.matchExp(property, value);
            SelectQuery query = new SelectQuery(dataObjectClass, qual);
            return CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e + "\n"
                    + LogUtils.getStackTrace(e.getStackTrace()));
            return new ArrayList<>();
        }
    }

    /**
     * <p>findMatchByProperties.</p>
     *
     * @param property1 la propriété 1
     * @param value1    la valeur 1
     * @param property2 la propriété 2
     * @param value2    la valeur 2
     * @return les objets qui répondent à "match" sur les deux conditions : property1 = value1 ET property2 = value2.
     */
    List<T> findMatchByProperties(String property1, Object value1, String property2, Object value2) {

        try {
            Expression qual =
                    ExpressionFactory.matchExp(property1, value1).andExp(ExpressionFactory.matchExp(property2, value2));
            SelectQuery<?> query = new SelectQuery<>(dataObjectClass, qual);
            return CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e + "\n" + e.getCause().getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * <p>findMatchByProperties.</p>
     *
     * @param property1 la propriété 1
     * @param value1    la valeur 1
     * @param property2 la propriété 2
     * @param value2    la valeur 2
     * @param property3 la propriété 3
     * @param value3    la valeur 3
     * @return les objets qui répondent à "match" sur les trois conditions : property1 = value1 ET property2 = value2 ET
     * property3 = value3.
     */
    List<T> findMatchByProperties(String property1, Object value1, String property2, Object value2, String property3,
                                  Object value3) {
        try {
            Expression qual =
                    ExpressionFactory.matchExp(property1, value1).andExp(ExpressionFactory.matchExp(property2,
                            value2).andExp(ExpressionFactory.matchExp(property3, value3)));
            SelectQuery query = new SelectQuery(dataObjectClass, qual);
            return CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return new ArrayList<>();
        }
    }

    /**
     * <p>findFirstMatchByProperty.</p>
     *
     * @param property   la propriété
     * @param value      la valeur
     * @return le premier objet retouvé qui répond à "match" sur cette propriété. Si aucun trouvé, renvoie null.
     */
    T findFirstMatchByProperty(String property, Object value) {

        List<T> list;
        try {
            Expression qual = ExpressionFactory.matchExp(property, value);
            SelectQuery<T> query = new SelectQuery<>(dataObjectClass, qual);
            list = CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return null;
        }

        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> find() {
        SelectQuery query = new SelectQuery(dataObjectClass);
        List<T> result;
        try {
            try {
                result = CastUtils.castList(getContext().performQuery(query), dataObjectClass);

            } catch (CayenneRuntimeException cre) {
                logger.debug(LogUtils.getStackTrace(cre.getStackTrace()));
                throw cre;
            }
        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }


    @Override
    public List<T> find(List<Integer> objectsId) {
        if (objectsId == null || objectsId.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<T> result = new ArrayList<>();
        for (Integer id : objectsId) {
            result.add(get(id));
        }
        return result;
    }

    /**
     * <p>findOrderBy.</p>
     *
     * @param property  la propriété
     * @param sortOrder l'ordre pour le tri
     * @return tous les objets, triés sur cette propriété selon cet ordre.
     */
    @SuppressWarnings("unused")
    protected List<T> findOrderBy(String property, SortOrder sortOrder) {
        SelectQuery query = new SelectQuery(dataObjectClass);
        query.addOrdering(new Ordering(property, sortOrder));
        List<T> result;
        try {
            result = CastUtils.castList(getContext().performQuery(query), dataObjectClass);

        } catch (Exception e) {
            logger.warn("Impossible d'effectuer la recherche de tous les objets : " + e.toString());
            return new ArrayList<>();
        }
        return new ArrayList<>(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPK(BaseDataObject object) {
        unHollowObjects(object);
        try {
            return Cayenne.intPKForObject(object);
        } catch (Exception e) {
            logger.debug("Impossible d'obtenir la PK de " + this.toString() + " : " + e.toString());
            // logger.debug(LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
    }

    /**
     * <p>canBeDeleted.</p>
     *
     * @param object l'objet
     * @return true si aucune relation (et les objets sur lesquels pointent ces relations) n'empêchent une suppression.
     * false si au moins une relation de cet objet pointe sur un objet (ou une liste d'au moins un objet) existant, et
     * que la relation indique que la règle de suppression est DENY. Dans ce cas, la suppression de l'objet object
     * echouera si on la tente.
     */
    public boolean canBeDeleted(T object) {
        unHollowObjects(object);

        if (object == null) {
            return false;
        }

        // On recherche les relations sur ce type d'objet.
        ClassDescriptor classDescriptor =
                getContext().getEntityResolver().getClassDescriptor(object.getObjectId().getEntityName());

        // On recherche toutes les relations
        final List<ArcProperty> properties = new ArrayList<>();
        classDescriptor.visitProperties(new PropertyVisitor() {

            public boolean visitAttribute(final AttributeProperty property) {
                // On ignore les attributs
                return true;
            }

            public boolean visitToOne(final ToOneProperty property) {
                properties.add(property);
                return true;
            }

            public boolean visitToMany(final ToManyProperty property) {
                properties.add(property);
                return true;
            }
        });

        // On parcourt les relations
        for (ArcProperty property : properties) {
            if (property.getRelationship().getDeleteRule() == DeleteRule.DENY) {
                // Si la règle de suppression indique DENY, alors on recherche
                // s'il existe un objet destination pour cette relation
                Object destination = object.readProperty(property.getName());
                if (destination != null) {
                    if (destination instanceof List<?>) {
                        if (((List<?>) destination).size() > 0) {
                            // logger.debug("Objet '"
                            // + object.getObjectId().getEntityName()
                            // + "' propriété '"
                            // + property.getName()
                            // + "' deleteRule="
                            // + property.getRelationship()
                            // .getDeleteRule()
                            // + " a pour destination une liste non vide !");
                            return false;
                        }

                    } else if (destination instanceof BaseDataObject) {
                        // logger.debug("Objet '"
                        // + object.getObjectId().getEntityName()
                        // + "' propriété '" + property.getName()
                        // + "' deleteRule="
                        // + property.getRelationship().getDeleteRule()
                        // + " a pour destination un objet non vide !");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * <p>attach.</p>
     *
     * @param object l'objet
     * @param <A>    la classe de l'objet.
     * @return l'objet, en s'assurant qu'il est correctement attaché que contexte.
     */
    public <A extends BaseDataObject> A attach(A object) {

        if (object == null) {
            return null;
        }

        if (object.getPersistenceState() == PersistenceState.HOLLOW) {
            object = context.localObject(object);
        }
        // if (object.getPersistenceState() == PersistenceState.TRANSIENT) {
        // contextService.getContext().registerNewObject(object);
        // }
        // if (object.getPersistenceState() == PersistenceState.NEW) {
        // contextService.getContext().registerNewObject(object);
        // }

        return object;
    }

    /**
     * S'assure que tous les objets passés en paramètres ne sont plus hollow.
     *
     * @param objects la liste d'objets
     * @param <A>     la classe de l'objet.
     */
    @SafeVarargs
    final <A extends BaseDataObject> void unHollowObjects(A... objects) {

        if (objects == null) {
            // rien
            return;
        }

        for (A object : objects) {
            attach(object);
        }
    }

    /**
     * S'assure que tous les objets passés en paramètres ne sont plus hollow.
     *
     * @param <A>        la classe de l'objet.
     * @param objectList la liste d'objets
     */
    <A extends BaseDataObject> void unHollowObjectsList(List<A> objectList) {

        if (objectList == null) {
            // rien
            return;
        }

        for (A object : objectList) {
            attach(object);
        }

    }

    /**
     * @return un nouvel objet, dont toutes les propriétés ont été copiées de l'objet passé en paramètre, et dans le contexte de cet objet.
     */
    @SuppressWarnings("unchecked")
    public T copyObject(T object) {
        T copy = (T) object.getObjectContext().newObject(object.getClass());
        try {
            Field[] fields = object.getClass().getSuperclass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(Property.class)) {
                    // La Property :
                    Property<?> property = (Property<?>) field.get(object);
                    // La copie de la Property :
                    copy.writeProperty(property.getName(), object.readPropertyDirectly(property.getName()));
                }
            }
        } catch (IllegalAccessException e) {
            logger.warn("Erreur lors de la copie d'un objet Cayenne : " + e);
            return copy;
        }
        return copy;
    }
}
