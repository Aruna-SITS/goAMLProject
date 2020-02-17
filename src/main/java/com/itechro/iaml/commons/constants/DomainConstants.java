package com.itechro.iaml.commons.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DomainConstants {

    public enum Title {
        MR("Mr"), MRS("Mrs"), MS("Ms"), DR("Dr");
        private String description;

        Title(String description) {
            this.description = description;
        }

        public static Title resolveTitle(String titleStr) {
            Title result = null;

            if (StringUtils.isNotEmpty(titleStr)) {
                for (Title title : Title.values()) {
                    if (title.toString().equals(titleStr.toUpperCase())) {
                        result = title;
                        break;
                    }
                }
            }

            return result;
        }

        public String getDescription() {
            return description;
        }

        public static Map<String, String> getTitleMap() {
            Map<String, String> result = new HashMap<>();
            Arrays.asList(Title.values()).forEach(title -> {
                result.put(title.toString(), title.getDescription());
            });
            return result;
        }
    }

    public enum Gender {
        M("Male"), F("Female");

        private String gender;

        Gender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return gender;
        }

        public static Gender resolveGender(String genderStr) {
            Gender result = null;
            if (StringUtils.isNotEmpty(genderStr)) {
                for (Gender gender : Gender.values()) {
                    if (gender.name().equals(genderStr.toUpperCase())) {
                        result = gender;
                        break;
                    }
                }
            }
            return result;
        }

        public static Map<String, String> getGenderMap() {
            Map<String, String> result = new HashMap<>();
            Arrays.asList(Gender.values()).forEach(gender -> {
                result.put(gender.toString(), gender.getGender());
            });
            return result;
        }
    }


    public enum PasswordUpdateAction {
        UPDATE, RESET, FORGET
    }

    public enum EmailSendType {
        TEXT, HTML
    }

    public enum EmailPurpose {
        OTHER, REPORT
    }

    public enum PrivilegeModule {
        WEB("Web"), MOBILE("Mobile");

        private String description;

        PrivilegeModule(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


    public enum AmountSpecifiedAs {
        PERCENTAGE("Percentage"), VALUE("Value");

        private String description;

        AmountSpecifiedAs(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum LanguageCode {
        EN("en");


        private String code;

        public String getCode() {
            return code;
        }

        LanguageCode(String code) {
            this.code = code;
        }
    }

}
