/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Ducnm62
 */
public class Utils {

    public static String combineLastKeyword(String[] keywords, int fromIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = fromIndex; i < keywords.length; i++) {
            builder.append(keywords[i]);
        }
        return builder.toString();
    }

    public static String getProperties(String propertyName, String defaultValue) {
        if (TiNosqlEngine.prop == null) {
            System.out.println("Properties file is not loaded");
            return "";
        } else {
            String output = defaultValue;
            try {
                output = TiNosqlEngine.prop.getProperty(propertyName).trim();
                if (output.equals("")) {
                    new Logger().WARNING(String.format("No config %s given, use default $s", propertyName, defaultValue));
                }
            } catch (NullPointerException ignore) {
                new Logger().WARNING(String.format("No config %s given, use default %s", propertyName, defaultValue));
            }
            return output;
        }
    }

    public static class DateUtils {

        public static Date getDateTrunc(Date input) {
            Calendar cal = Calendar.getInstance();

            cal.setTime(input);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return cal.getTime();
        }

        public static String toString(Date date, String format) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return date != null ? df.format(date) : "";
        }
    }

    public static class StringUtils {

        public static String combineLastKeyword(String[] keywords, int fromIndex, String cement) {
            StringBuilder builder = new StringBuilder();
            for (int i = fromIndex; i < keywords.length; i++) {
                builder.append(keywords[i] + cement);
            }
            return builder.toString();
        }
    }
}
