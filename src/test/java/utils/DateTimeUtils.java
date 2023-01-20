package utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd, hh:mma");

        return dateFormat.format(date);
    }

    public static String getTimeInMinSecFormat(long time) {
        int minutes = (int) ((time / 1000) / 60);
        int seconds = (int) (time / 1000) % 60;

        return "" + minutes + " min " + seconds + " sec";
    }

    public static String getEightDaysFromDate(String month, int day, int year) {
        int monthNumber = convertMonthStringTohNumber(month);
        LocalDate localDate = LocalDate.of(year, monthNumber, day);

        String dayOfWeek;
        String monthName;
        String dayValue;
        StringBuilder eightDatesBuilder = new StringBuilder();

        for (int i = 0; i <= 7; i++) {
            dayOfWeek = DayOfWeek.from(localDate).getDisplayName(TextStyle.SHORT, Locale.US);
            monthName = Month.from(localDate).getDisplayName(TextStyle.SHORT, Locale.US);
            dayValue = dayFormatDToDD(localDate.getDayOfMonth());

            eightDatesBuilder.append(String.format("%s, %s %s, ", dayOfWeek, monthName, dayValue));
            localDate = localDate.plusDays(1);
        }
        String eightDatesString = eightDatesBuilder.toString();

        return eightDatesString.substring(0, eightDatesString.length() - 2);
    }

    private static int convertMonthStringTohNumber(String monthString) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.US);
        Month month = Month.from(format.parse(monthString));

        return month.getValue();
    }

    public static String dayFormatDToDD(int day) {

        return day < 10 ? "0" + day : String.valueOf(day);


    }
}
