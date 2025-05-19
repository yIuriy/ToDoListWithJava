package code.validator;

import lombok.extern.log4j.Log4j2;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Log4j2
public class Validator {
    private static final DateTimeFormatter DATE_FORMAT_FOR_SQL = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Date validateDate(String string) {
        log.info(string);
        if (!Pattern.matches("20\\d{2}-(1[0-2]|0?[1-9])-(3[01]|[12]\\d|0?[1-9])", string)) {
            throw new IllegalArgumentException("Invalid format of data.");
        }

        LocalDate dateTime = LocalDate.parse(LocalDate.now().format(DATE_FORMAT_FOR_SQL));
        String[] split = string.split("-");
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        if (!checkIfTheMonthHasValidNumbersOfDay(month, day, year)){
            throw new IllegalArgumentException("Invalid number of days.");
        }

        if (year < dateTime.getYear()) {
            throw new IllegalArgumentException("You cannot enter a year prior to the current one.");
        }
        if (month < dateTime.getMonthValue() && year == dateTime.getYear()) {
            throw new IllegalArgumentException("You cannot enter a month prior to the current one.");
        }
        if (day < dateTime.getDayOfMonth() && month == dateTime.getMonthValue()) {
            throw new IllegalArgumentException("You cannot enter a day prior to the current one.");
        }
        return Date.valueOf(string);
    }

    private static boolean checkIfTheMonthHasValidNumbersOfDay(int month, int day, int year) {
        boolean isValid = false;
        if (day < 1 || day > 31) {
            return false;
        }
        switch (month) {
            case 1 ,3, 5, 7, 8, 10, 12:
                if (day <= 31) {
                    isValid = true;
                }
                break;
            case 2:
                if (isLeapYear(year)) {
                    if (day <= 29) {
                        isValid = true;
                    }
                } else {
                    if (day <= 28) {
                        isValid = true;
                    }
                }
                break;
            case 4, 6, 9, 11:
                if (day <= 30) {
                    isValid = true;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
        return isValid;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static boolean isValidPriority(Integer priority){
        return  priority <= 10 && priority >= 0;
    }
}

