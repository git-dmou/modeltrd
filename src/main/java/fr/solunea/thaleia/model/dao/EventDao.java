package fr.solunea.thaleia.model.dao;

import fr.solunea.thaleia.model.Event;
import fr.solunea.thaleia.model.User;
import fr.solunea.thaleia.utils.LogUtils;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.cayenne.query.SortOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class EventDao extends CayenneDao<Event> {

    public EventDao(ObjectContext context) {
        super(context);
    }

    @Override
    public String getDisplayName(Event object, Locale locale) {
        return object.getName() + " @ " + object.getDate();
    }

    /**
     *
     * @param event événement à rechercher.
     * @param user utilisateur concerné.
     * @return true s'il existe au moins un événement Event pour cet user.
     */
    public boolean exists(String event, User user) {
        if (user == null || event == null) {
            logger.warn("Un des paramètres est nul.");
            return false;
        }

        try {
            long shouldExistEvents = ObjectSelect.query(Event.class)
                    .where(Event.NAME.eq(event))
                    .and(Event.USER.eq(user))
                    .selectCount(context);
            return (shouldExistEvents > 0);

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return false;
        }
    }

    /**
     * @return true s'il existe au moins un événément eventExist, et aucun eventNotExist pour ce user.
     */
    public boolean existsAndNot(String eventExist, String eventNotExist, User user) {

        if (user == null || eventExist == null || eventNotExist == null) {
            logger.warn("Un des paramètres est nul.");
            return false;
        }

        try {
            long shouldExistEvents = ObjectSelect.query(Event.class)
                    .where(Event.NAME.eq(eventExist))
                    .and(Event.USER.eq(user))
                    .selectCount(context);
            long shouldNotExistEvents = ObjectSelect.query(Event.class)
                    .where(Event.NAME.eq(eventNotExist))
                    .and(Event.USER.eq(user))
                    .selectCount(context);
            return (shouldExistEvents > 0 && shouldNotExistEvents == 0);

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return false;
        }
    }

    /**
     * @return Le premier évenement chronologique de ce type et pour cet utilisateur.
     */
    public Event firstEvent(String eventName, User user) {
        if (user == null || eventName == null) {
            logger.warn("Un des paramètres est nul.");
            return null;
        }
        try {
            // Ne fonctionne pas : objets user de type ToOneFault
//            List<Event> events = ObjectSelect.query(Event.class)
//                    .where(Event.NAME.eq(eventName))
//                    .and(Event.USER.eq(user))
//                    .orderBy(Event.DATE.getName(), SortOrder.ASCENDING)
//                    .limit(1)
//                    .select(context);
//            return (events.size() > 0) ? events.get(0) : null;

            for (Event event : user.getEvents()) {
                if (eventName.equals(event.getName())) {
                    return event;
                }
            }
            return null;

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return null;
        }
    }

    public int countUserEvents(User user, List<String> eventNames, LocalDateTime from, LocalDateTime to) {
        if (user == null || eventNames == null || from == null || to == null) {
            logger.warn("Un des paramètres est nul.");
            return 0;
        }
        try {
            List<Event> events = ObjectSelect.query(Event.class)
                    .where(Event.NAME.in(eventNames))
                    .and(Event.USER.eq(user))
                    .and(Event.DATE.gt(from))
                    .and(Event.DATE.lt(to))
                    .orderBy(Event.DATE.getName(), SortOrder.ASCENDING)
                    .select(context);
            return events.size();

        } catch (Exception e) {
            logger.warn("Impossible d'exécuter la requête : " + LogUtils.getStackTrace(e.getStackTrace()));
            return 0;
        }
    }
}
