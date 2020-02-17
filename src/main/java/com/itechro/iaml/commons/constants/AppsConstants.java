package com.itechro.iaml.commons.constants;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;

import java.util.*;

public class AppsConstants {
    public enum ResponseStatus {
        SUCCESS, FAILED;

        public static ResponseStatus resolveStatus(String statusStr) {
            ResponseStatus matchingStatus = null;
            if (!StringUtils.isEmpty(statusStr)) {
                matchingStatus = ResponseStatus.valueOf(statusStr.trim());
            }
            return matchingStatus;
        }
    }

    public enum YesNo {

        Y("Yes", true, 1), N("No", false, 0);

        private String strVal;

        private Boolean boolVal;

        private Integer intVal;

        YesNo(String strVal, Boolean boolVal, Integer intVal) {
            this.strVal = strVal;
            this.boolVal = boolVal;
            this.intVal = intVal;
        }

        public static YesNo valueOf(Boolean boolVal) {
            YesNo matchingStatus = null;
            for (YesNo yesno : YesNo.values()) {
                if (yesno.getBoolVal().equals(boolVal)) {
                    matchingStatus = yesno;
                    break;
                }
            }
            return matchingStatus;
        }

        public static YesNo resolver(String val) {
            YesNo matchingStatus = null;
            if (val != null) {
                matchingStatus = YesNo.valueOf(val);
            }
            return matchingStatus;
        }

        public static Map<String, String> getYesNoMap() {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (YesNo yesNo : YesNo.values()) {
                map.put(yesNo.toString(), yesNo.getStrVal());
            }
            return map;
        }

        public String getStrVal() {
            return strVal;
        }

        public Boolean getBoolVal() {
            return boolVal;
        }

        public Integer getIntVal() {
            return intVal;
        }
    }

    public enum Status {
        ACT("Active"), INA("Inactive");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static Status resolveStatus(String statusStr) {
            Status matchingStatus = null;
            if (!StringUtils.isEmpty(statusStr)) {
                matchingStatus = Status.valueOf(statusStr.trim());
            }
            return matchingStatus;
        }

        public static Map<String, String> getStatusMap() {
            Map<String, String> result = new HashMap<>();
            Arrays.asList(Status.values()).forEach(status -> {
                result.put(status.toString(), status.getDescription());
            });
            return result;
        }
    }

    public enum EntityOperationType {
        ADDED, UPDATED, REMOVED;
    }

    public enum DaysOfWeek {

        Su(Calendar.SUNDAY, DateTimeConstants.SUNDAY, "Sun"),
        Mo(Calendar.MONDAY, DateTimeConstants.MONDAY, "Mon"),
        Tu(Calendar.TUESDAY, DateTimeConstants.TUESDAY, "Tue"),
        We(Calendar.WEDNESDAY, DateTimeConstants.WEDNESDAY, "Wed"),
        Th(Calendar.THURSDAY, DateTimeConstants.THURSDAY, "Thu"),
        Fr(Calendar.FRIDAY, DateTimeConstants.FRIDAY, "Fri"),
        Sa(Calendar.SATURDAY, DateTimeConstants.SATURDAY, "Sat");

        private Integer dayOfWeek;
        private Integer dayOfWeekJoda;
        private String shortCodeOfDay;

        DaysOfWeek(Integer dayOfWeek, Integer dayOfWeekJoda, String shortCodeOfDay) {
            this.dayOfWeek = dayOfWeek;
            this.dayOfWeekJoda = dayOfWeekJoda;
            this.shortCodeOfDay = shortCodeOfDay;
        }

        public static DaysOfWeek resolveDayOfWeek(Integer dayOfWeek) {
            DaysOfWeek matchingDay = null;
            if (dayOfWeek != null) {
                DaysOfWeek[] values = DaysOfWeek.values();
                for (DaysOfWeek day : values) {
                    if (day.getDayOfWeek().equals(dayOfWeek)) {
                        matchingDay = day;
                    }
                }
            }
            return matchingDay;
        }


        public static String resolveShortCodeOfDay(Integer dayOfWeek) {
            String matchingDay = null;
            if (dayOfWeek != null) {
                DaysOfWeek[] values = DaysOfWeek.values();
                for (DaysOfWeek day : values) {
                    if (day.getDayOfWeek().equals(dayOfWeek)) {
                        matchingDay = day.getShortCodeOfDay();
                    }
                }
            }
            return matchingDay;
        }

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public Integer getDayOfWeekJoda() {
            return dayOfWeekJoda;
        }

        public String getShortCodeOfDay() {
            return shortCodeOfDay;
        }
    }

    public enum AuthorizationError {
        USER_UNAUTHORIZED("User is unauthorized for action");

        private String description;

        AuthorizationError(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum NotificationStatus {
        PENDING("Pending"),
        ACTIVE("Active"),
        INVALID("Invalid"),
        ACKNOWLEDGE("Acknowledge");

        private String description;

        NotificationStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public static NotificationStatus resolveNotificationStatus(String notificationStatusStr) {
            NotificationStatus matchingNotificationStatus = null;
            if (!StringUtils.isEmpty(notificationStatusStr)) {
                matchingNotificationStatus = NotificationStatus.valueOf(notificationStatusStr.trim());
            }
            return matchingNotificationStatus;
        }
    }
}
