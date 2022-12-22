package fr.solunea.thaleia.utils;

import org.apache.commons.lang.time.FastDateFormat;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormat;

import java.util.Locale;

/**
 * <p>TimeFormatter class.</p>
 *
 * @author RMAR
 * @version $Id: $Id
 */
public class TimeFormatter {

    /**
     * <p>localizeTimestamp.</p>
     *
     * @param timestamp a long.
     * @param locale    a {@link java.util.Locale} object.
     * @return une représentation localisée de cette date, sous la forme
     * "03/09/15 13:12"
     */
    public static String localizeTimestamp(long timestamp, Locale locale) {
        FastDateFormat format = FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale);
        return format.format(timestamp);
    }

    /**
     * <p>localizeDuration.</p>
     *
     * @param durationInSeconds la durée, en nombre de secondes.
     * @param locale            a {@link java.util.Locale} object.
     * @return une représentation localisée de cette durée : "12 secondes",
     * "3 minutes 23 secondes", "4 heures 12 minutes",
     * "3 jours 12 heures".
     */
    public static String localizeDuration(long durationInSeconds, Locale locale) {
        // On localise la présentation des différents éléments de la
        // durée
        String seconds = PeriodFormat.wordBased(locale).print(Seconds.seconds(CastUtils.safeLongToInt(
                durationInSeconds % 60)));
        String minutes = PeriodFormat.wordBased(locale).print(Minutes.minutes(CastUtils.safeLongToInt(
                (durationInSeconds % 3600) / 60)));
        String hours = PeriodFormat.wordBased(locale).print(Hours.hours(CastUtils.safeLongToInt(
                (durationInSeconds % 86400) / 3600)));
        String days = PeriodFormat.wordBased(locale).print(Days.days(CastUtils.safeLongToInt(
                durationInSeconds / 86400)));

        if (durationInSeconds < 59) {
            // Entre 0s et 59s
            return seconds;

        } else if (durationInSeconds < 3599) {
            // Entre 1 min 0s et 59 min 59s
            return minutes + " " + seconds;

        } else if (durationInSeconds < 86399) {
            // Entre 1h00 et 23h59m59
            return hours + " " + minutes;

        } else {
            // Plus de 1 jours
            return days + " " + hours;

        }
    }

}
