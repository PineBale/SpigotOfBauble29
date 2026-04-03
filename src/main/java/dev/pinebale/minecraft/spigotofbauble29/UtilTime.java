package dev.pinebale.minecraft.spigotofbauble29;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Utility class for time formatting, duration conversion and human-readable time strings.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unused")
public final class UtilTime {

    private static final DateTimeFormatter DATE_FORMAT_NOW = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMAT_DAY = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private static final Supplier<Long> NOW_MILLIS = System::currentTimeMillis;

    /**
     * @return Current time formatted as "MM-dd-yyyy HH:mm:ss" in system default zone
     */
    public static String now() {
        return LocalDateTime.now().format(DATE_FORMAT_NOW);
    }

    /**
     * Formats a unix timestamp (milliseconds) to "MM-dd-yyyy HH:mm:ss" in system default zone
     *
     * @param time unix timestamp in milliseconds
     * @return formatted datetime string
     */
    public static String when(long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).format(DATE_FORMAT_NOW);
    }

    /**
     * @return Current date formatted as "MM-dd-yyyy" in system default zone
     */
    public static String date() {
        return LocalDateTime.now().format(DATE_FORMAT_DAY);
    }

    /**
     * Formats a unix timestamp (milliseconds) to "MM-dd-yyyy" in system default zone
     *
     * @param date unix timestamp in milliseconds
     * @return formatted date string
     */
    public static String date(long date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.systemDefault()).format(DATE_FORMAT_DAY);
    }

    /**
     * Returns the correct ordinal suffix for a day of month (1st, 2nd, 3rd, 4th, ..., 11th, 12th, 13th, ...)
     *
     * @param n day of month (1-31)
     * @return "st", "nd", "rd" or "th"
     */
    public static String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * Returns human-readable string how much time has passed since the given epoch timestamp
     *
     * @param epoch timestamp in milliseconds to compare against now
     * @return e.g. "Took 3.2 seconds."
     */
    public static String since(long epoch) {
        return since(NOW_MILLIS, epoch);
    }

    /**
     * Returns human-readable string how much time has passed since the given epoch timestamp
     *
     * @param base  supplier for current time in milliseconds
     * @param epoch timestamp in milliseconds to compare against
     * @return e.g. "Took 3.2 seconds."
     */
    public static String since(Supplier<Long> base, long epoch) {
        return "Took " + convertString(base.get() - epoch, 1, TimeUnit.FIT) + ".";
    }

    private static TimeUnit convert0(long time, TimeUnit timeUnit) {
        if (timeUnit != TimeUnit.FIT) {
            return timeUnit;
        }

        if (time < 1000) return TimeUnit.MILLISECONDS;
        if (time < 60000) return TimeUnit.SECONDS;
        if (time < 3600000) return TimeUnit.MINUTES;
        if (time < 86400000) return TimeUnit.HOURS;
        return TimeUnit.DAYS;
    }

    /**
     * Converts milliseconds to a double value in the most appropriate / requested unit
     *
     * @param time     duration in milliseconds
     * @param trim     decimal places (0 = no decimals)
     * @param timeUnit desired or FIT unit
     * @return value in selected unit
     */
    public static double convert(long time, int trim, TimeUnit timeUnit) {
        TimeUnit type = convert0(time, timeUnit);
        switch (type) {
            case DAYS:
                return doTrim(trim, time / 86400000.0);
            case HOURS:
                return doTrim(trim, time / 3600000.0);
            case MINUTES:
                return doTrim(trim, time / 60000.0);
            case SECONDS:
                return doTrim(trim, time / 1000.0);
            default:
                return doTrim(trim, time);
        }
    }

    /**
     * Convert time from one unit to another
     *
     * @param time value in source unit
     * @param from source unit
     * @param to   target unit
     * @return value in target unit
     */
    public static long convert(long time, TimeUnit from, TimeUnit to) {
        long milliseconds = time * from.milliseconds;
        return milliseconds / to.milliseconds;
    }

    /**
     * Returns human-readable duration string (e.g. "2.4 minutes")
     *
     * @param time duration in milliseconds
     * @return formatted string
     */
    public static String makeStr(long time) {
        return convertString(time, 1, TimeUnit.FIT);
    }

    /**
     * Converts milliseconds to HH:MM:SS format
     *
     * @param time time in milliseconds
     * @return colon separated string or "Permanent"/"0"
     */
    public static String convertColonString(long time) {
        return convertColonString(time, TimeUnit.HOURS, TimeUnit.SECONDS);
    }

    /**
     * Converts time into a colon separated string (e.g. 01:23:45)
     *
     * @param time duration in milliseconds
     * @param max  highest unit to display
     * @param min  lowest unit to display
     * @return formatted string or "Permanent"/"0"
     */
    public static String convertColonString(long time, TimeUnit max, TimeUnit min) {
        if (time <= -1L) {
            return "Permanent";
        }
        if (time == 0L) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        long curr = time;

        for (TimeUnit unit : TimeUnit.decreasingOrder()) {
            if (unit.milliseconds >= min.milliseconds && unit.milliseconds <= max.milliseconds) {
                long amt = curr / unit.milliseconds;
                if (amt < 10 && unit != max) {
                    sb.append('0');
                }
                sb.append(amt);
                if (unit.milliseconds > min.milliseconds) {
                    sb.append(':');
                }
                curr -= amt * unit.milliseconds;
            }
        }
        return sb.toString();
    }

    /**
     * Returns human readable duration string (e.g. "3.2 seconds", "1 day")
     *
     * @param time     duration in milliseconds
     * @param trim     maximum decimal places
     * @param timeUnit desired or FIT unit
     * @return formatted string (singular/plural handled)
     */
    public static String convertString(long time, int trim, TimeUnit timeUnit) {
        if (time <= -1L) {
            return "Permanent";
        }

        TimeUnit type = convert0(time, timeUnit);
        double num;
        String text;

        switch (type) {
            case DAYS:
                num = doTrim(trim, time / 86400000.0);
                text = num + " day";
                break;
            case HOURS:
                num = doTrim(trim, time / 3600000.0);
                text = num + " hour";
                break;
            case MINUTES:
                if (trim == 0) {
                    num = (int) doTrim(trim, time / 60000.0);
                    text = String.format("%d minute", (int) num);
                } else {
                    num = doTrim(trim, time / 60000.0);
                    text = num + " minute";
                }
                break;
            case SECONDS:
                if (trim == 0) {
                    num = (int) doTrim(trim, time / 1000.0);
                    text = String.format("%d second", (int) num);
                } else {
                    num = doTrim(trim, time / 1000.0);
                    text = num + " second";
                }
                break;
            default:
                num = (int) doTrim(0, (double) time);
                text = String.format("%d millisecond", (int) num);
                break;
        }

        if (num != 1.0) {
            text += "s";
        }
        return text;
    }

    /**
     * Checks if required amount of milliseconds has passed since from
     */
    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    /**
     * Formats a double value to specified decimal places
     *
     * @param degree number of decimal places (0 = integer)
     * @param d      value to format
     * @return trimmed double value
     */
    public static double doTrim(int degree, double d) {
        String pattern = "#.#" + StringUtils.repeat("#", Math.max(0, degree - 1));
        DecimalFormat twoDForm = new DecimalFormat(pattern, new DecimalFormatSymbols());
        return Double.parseDouble(twoDForm.format(d));
    }

    public enum TimeUnit {
        FIT(0),
        DAYS(86_400_000L),
        HOURS(3_600_000L),
        MINUTES(60_000L),
        SECONDS(1_000L),
        MILLISECONDS(1L);

        final long milliseconds;

        TimeUnit(long milliseconds) {
            this.milliseconds = milliseconds;
        }

        public static TimeUnit[] decreasingOrder() {
            return new TimeUnit[]{DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS};
        }
    }
}