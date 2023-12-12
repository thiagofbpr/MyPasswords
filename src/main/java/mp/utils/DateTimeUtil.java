package mp.utils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private DateTimeUtil() {

    }

    public static ZonedDateTime convertToZonedDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
    }

    public static String getDatetimeFormat(ZonedDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
        return date.format(dateTimeFormatter);
    }

    public static String getDateFormat(ZonedDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return date.format(dateTimeFormatter);
    }

    public static Timestamp getCurrentTimestamp() {
        return Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());
    }


}
