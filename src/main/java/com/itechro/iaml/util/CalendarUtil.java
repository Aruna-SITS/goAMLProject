package com.itechro.iaml.util;

import com.itechro.iaml.exception.impl.AppsCommonErrorCode;
import com.itechro.iaml.exception.impl.AppsException;
import com.itechro.iaml.exception.impl.AppsRuntimeException;
import com.itechro.iaml.service.ctr.JavaToXMLAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Utilities
 *
 * @author : chamara
 */
public class CalendarUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JavaToXMLAdaptor.class);

    public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";

    public static final String DEFAULT_DATE_AND_HOUR_FORMAT = "dd/MM/yyyy HH";

    public static final String DEFAULT_DATE_TIME_FORMAT_SECONDS = "dd/MM/yyyy HH:mm:ss";

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static final String DEFAULT_YEAR_WEEK_FORMAT = "YYYY-ww";

    public static final String DEFAULT_YEAR_MONTH_FORMAT = "yyyy-MM";

    public static final String DEFAULT_YEAR_FORMAT = "yyyy";

    public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd";

    public static final String MYSQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String MYSQL_DATE_AND_HOUR_FORMAT = "yyyy-MM-dd HH";

    public static final String MYSQL_DATE_TIME_FORMAT_SECONDS = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_TIME_FORMAT = "HH:mm";

    public static Date getParsedDate(String date, String dateFormat) throws AppsException {

        Date parsedDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            parsedDate = format.parse(date);
            return parsedDate;
        } catch (ParseException e) {
            throw new AppsException(AppsCommonErrorCode.APPS_EXCEPTION_INVALID_DATE_FORMAT);
        }

    }

    public static Date getDefaultParsedDateTime(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_DATE_TIME_FORMAT);
    }


    public static Date getMySqlParsedDateTime(String date) throws AppsException {
        return getParsedDate(date, MYSQL_DATE_TIME_FORMAT_SECONDS);
    }

    public static Date getMySqlParsedDateAndHour(String date) throws AppsException {
        return getParsedDate(date, MYSQL_DATE_AND_HOUR_FORMAT);
    }

    public static Date getMySqlParsedDate(String date) throws AppsException {
        return getParsedDate(date, MYSQL_DATE_FORMAT);
    }

    public static Date getDefaultParsedDateTimeMaskNull(String date) throws AppsException {
        return date != null ? getParsedDate(date, DEFAULT_DATE_TIME_FORMAT) : null;
    }

    public static Date getParsedDateTime(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_DATE_TIME_FORMAT_SECONDS);
    }

    public static Date getDefaultParsedDateOnly(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_DATE_FORMAT);
    }

    public static Date getDefaultParsedWeekOfYear(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_YEAR_WEEK_FORMAT);
    }

    public static Date getDefaultParsedMonthOfYear(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_YEAR_MONTH_FORMAT);
    }

    public static Date getDefaultParsedYear(String date) throws AppsException {
        return getParsedDate(date, DEFAULT_YEAR_FORMAT);
    }

    public static Date addYears(Date date, int numberOfYears) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, numberOfYears);

        return calendar.getTime();
    }

    public static Date addMonths(Date date, int numberOfMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, numberOfMonths);

        return calendar.getTime();
    }

    public static Date addWeek(Date date, int numberOfWeeks) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeeks);

        return calendar.getTime();
    }

    public static Date addDate(Date date, int numberOfDates) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDates);

        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int numberOfMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, numberOfMinutes);

        return calendar.getTime();
    }

    public static Date addHours(Date date, int numberOfHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, numberOfHours);

        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int numberOfSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, numberOfSeconds);

        return calendar.getTime();
    }

    public static Date minusMinutes(Date date, int numberOfMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, (-1) * numberOfMinutes);

        return calendar.getTime();
    }

    public static String getDefaultDateTimeFormat() {
        return DEFAULT_DATE_TIME_FORMAT;
    }

    public static String getDefaultDateFormat() {
        return DEFAULT_DATE_FORMAT;
    }

    public static String getDefaultTimeFormat() {
        return DEFAULT_TIME_FORMAT;
    }

    public static String getDefaultFormattedDateOnly(Date date) {
        return getFormattedDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String getDefaultFormattedWeekOfYear(Date date) {
        return getFormattedDate(date, DEFAULT_YEAR_WEEK_FORMAT);
    }

    public static String getDefaultFormattedMonthYear(Date date) {
        return getFormattedDate(date, DEFAULT_YEAR_MONTH_FORMAT);
    }

    public static String getDefaultFormattedYearOnly(Date date) {
        return getFormattedDate(date, DEFAULT_YEAR_MONTH_FORMAT);
    }

    public static String getDefaultFormattedWeekOnly(Date date) {
        return getFormattedDate(date, "ww");
    }

    public static String getDefaultFormattedMonthOnly(Date date) {
        return getFormattedDate(date, "MM");
    }

    public static String getDefaultFormattedHourOnly(Date date) {
        return getFormattedDate(date, "HH");
    }

    public static String getDefaultFormattedDayOfWeek(Date date) {
        return getFormattedDate(date, "E");
    }

    public static String getDefaultFormattedMinutesOnly(Date date) {
        return getFormattedDate(date, "mm");
    }

    public static String getDefaultFormattedDateMaskNull(Date date) {
        if (date != null) {
            return CalendarUtil.getDefaultFormattedDateOnly(date);
        }
        return null;
    }

    public static String getDefaultFormattedDateTimeMaskNull(Date date) {
        if (date != null) {
            return CalendarUtil.getDefaultFormattedDateTime(date);
        }
        return null;
    }

    public static String getDefaultFormattedTimeOnly(Date date) {
        return getFormattedDate(date, DEFAULT_TIME_FORMAT);
    }

    public static String getDefaultFormattedDateTime(Date date) {
        return getFormattedDate(date, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String getDefaultFormattedDateTimeWithSeconds(Date date) {
        return getFormattedDate(date, DEFAULT_DATE_TIME_FORMAT_SECONDS);
    }

    public static String getDefaultFormattedDateAndHour(Date date) {
        return getFormattedDate(date, DEFAULT_DATE_AND_HOUR_FORMAT);
    }

    public static String getDefaultFormattedTimeMaskNull(Date date) {
        if (date != null) {
            return CalendarUtil.getDefaultFormattedTimeOnly(date);
        }
        return null;
    }

    public static String getFormattedDateMaskNull(Date date, String dateFormat) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return formatter.format(date);
        } else {
            return null;
        }
    }


    public static String getFormattedDate(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    public static Date getParsedStartDateTime(String date) throws AppsException {
        date = date.trim().concat(" 00:00:00");
        return getParsedDateTime(date);
    }

    public static Date getParsedEndDateTime(String date) throws AppsException {
        date = date.trim().concat(" 23:59:59");
        return getParsedDateTime(date);
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) throws AppsException {
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;

        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException ex) {
            throw new AppsException(AppsCommonErrorCode.APPS_INTERNAL_SERVER_ERROR);
        }

        return xmlCalendar;
    }

    public static String getMysqlDateOnlyString(Date date) {
        if (date == null) {
            return null;
        }
        return getFormattedDate(date, MYSQL_DATE_FORMAT);
    }

    public static String getMysqlDateTimeString(Date date) {
        if (date == null) {
            return null;
        }
        return getFormattedDate(date, MYSQL_DATE_TIME_FORMAT);
    }

    public static String getMysqlDateTimeSecondString(Date date) {
        if (date == null) {
            return null;
        }
        return getFormattedDate(date, MYSQL_DATE_TIME_FORMAT_SECONDS);
    }

    public static Date getDateOnly(Date date) {
        return getDateOnly(date, MYSQL_DATE_FORMAT);

    }

    public static Date getDefaultParsedDateOnly(Date date) {
        return getDateOnly(date, DEFAULT_DATE_FORMAT);

    }

    public static Date getDateOnly(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        Date todayWithZeroTime = null;
        try {
            todayWithZeroTime = formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            throw new AppsRuntimeException(AppsCommonErrorCode.APPS_EXCEPTION_INVALID_DATE_FORMAT);
        }

        return todayWithZeroTime;
    }

    public static Date getCombinedDateTime(Date date, String time) throws AppsException {
        String formattedDate = CalendarUtil.getDefaultFormattedDateOnly(date);
        formattedDate = formattedDate + " " + time;
        return CalendarUtil.getDefaultParsedDateTime(formattedDate);
    }

    public static String extractTime(Date date) {
        return CalendarUtil.getDefaultFormattedDateTime(date).substring(11, 16);
    }

    public static int getDay(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getMonth(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getDate(Date date) {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static boolean isBeforeOrEqual(Date checkDate, Date checkAgainstDate) {
        boolean status = false;
        GregorianCalendar checkDateCalendar = getCalendar(checkDate);
        GregorianCalendar checkAgainstDateCalendar = getCalendar(checkAgainstDate);

        if (checkDateCalendar.equals(checkAgainstDateCalendar) || checkDate.before(checkAgainstDate)) {
            status = true;
        }
        return status;
    }


    public static boolean isBefore(Date checkDate, Date checkAgainstDate) {
        boolean status = false;
        if (checkDate.before(checkAgainstDate)) {
            status = true;
        }
        return status;
    }

    public static boolean isBetween(Date parseDate, Date fromDate, Date toDate) {

        boolean status = false;
        GregorianCalendar compareDate = getCalendar(parseDate);
        GregorianCalendar dateFrom = getCalendar(fromDate);
        GregorianCalendar dateTo = getCalendar(toDate);

        if (compareDate.after(dateFrom) && compareDate.before(dateTo)) {
            status = true;
        }
        return status;
    }

    public static boolean isBetweenOrEqual(Date parseDate, Date fromDate, Date toDate) {

        boolean status = false;
        GregorianCalendar compareDate = getCalendar(parseDate);
        GregorianCalendar dateFrom = getCalendar(fromDate);
        GregorianCalendar dateTo = getCalendar(toDate);


        if (compareDate.equals(dateFrom) || compareDate.equals(dateTo) || (compareDate.after(
                dateFrom) && compareDate.before(dateTo))) {
            status = true;
        }
        if (dateFrom.after(dateTo)) {
            status = false;
        }
        return status;
    }

    public static boolean isEqual(Date date1, Date date2) {
        GregorianCalendar dateFrom = getCalendar(date1);
        GregorianCalendar dateTo = getCalendar(date2);

        return dateFrom.equals(dateTo);
    }

    private static GregorianCalendar getCalendar(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    public static Integer getMinutesFromTimeStr(String timeStr) {
        Integer minutes = 0;

        if (timeStr != null) {
            String[] split = StringUtils.split(timeStr, ":");
            minutes = Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
        }

        return minutes;
    }
    //to get the XMLGregorianCalendar date form java date
    public static XMLGregorianCalendar getXMLGregorianCalendarFromDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            return null;
        }
        String dateStr = date.toString();
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(date);
        XMLGregorianCalendar calendar = null;
        try {
            calendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            gregory);
            calendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            calendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException e) {
            LOG.error("Error while parsing date ", e);
        }
        return calendar;
    }

    //to get the XMLGregorianCalendar date form java String
    public static XMLGregorianCalendar getXMLGregorianCalendarFromString(String date) {
        Date xmlDate = null;
        try {
            xmlDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            LOG.error("Date Parse Error ", e);
        }
        return getXMLGregorianCalendarFromDate(xmlDate);
    }
}
