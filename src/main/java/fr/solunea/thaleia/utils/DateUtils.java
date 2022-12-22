package fr.solunea.thaleia.utils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <p>DateUtils class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class DateUtils {

    /**
     * La date au format de la locale, puis l'heure au format de la locale. Par
     * exemple : FR : "29/10/13 16:39" EN : "10/29/13 4:39 PM"
     *
     * @param date   a {@link java.util.Date} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     */
    public static String formatDateHour(Date date, Locale locale) {
        return DateFormatUtils.format(date,
                FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern() + " "
                        + FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale).getPattern());
    }

    /**
     * La date au format de la locale. Par
     * exemple : FR : "29/10/13" EN : "10/29/13"
     *
     * @param date   a {@link java.util.Date} object.
     * @param locale a {@link java.util.Locale} object.
     * @return a {@link java.lang.String} object.
     */
    public static String formatDate(Date date, Locale locale) {
        return DateFormatUtils.format(date, FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern());
    }

    /**
     * <p>getEndOfDay.</p>
     *
     * @param date a {@link java.util.Date} object.
     * @return le dernier instant de ce jour.
     */
    public static Date getEndOfDay(Date date) {
        return org.apache.commons.lang3.time.DateUtils.addMilliseconds(org.apache.commons.lang3.time.DateUtils
                .ceiling(date, Calendar.DATE), -1);
    }

    /**
     * <p>getStartOfDay.</p>
     *
     * @param date a {@link java.util.Date} object.
     * @return le premier instant de ce jour.
     */
    public static Date getStartOfDay(Date date) {
        return org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
    }

}
