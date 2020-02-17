package com.itechro.iaml.util;


import java.util.Collection;

/**
 * @author chamara
 */
public abstract class QueryInBuilder {

    public static String buildSQLINQuery(String... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append("'");
            sb.append(values[i]);
            sb.append("'");
            if (i != values.length - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    public static String buildSQLINQuery(Integer... values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            sb.append("'");
            sb.append(values[i]);
            sb.append("'");
            if (i != values.length - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    public static <T> String buildSQLINQuery(Collection<T> values) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (T val : values) {
            sb.append("'");
            sb.append(val);
            sb.append("'");
            if (i != values.size() - 1)
                sb.append(",");

            i++;
        }
        return sb.toString();
    }

    public static <T> String buildSQLINExceedQuery(Collection<T> values, String field) {
        int maxLimit = 999;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        String prefix = "";

        sb.append("( ");
        sb.append(field);
        sb.append(" IN (");

        for (T val : values) {
            if (i > maxLimit) {
                prefix = "";
                sb.append(" ) OR ");
                sb.append(field);
                sb.append(" IN ( ");
                sb.append(prefix);
                prefix = ", ";
                sb.append(val);
                i = 0;
            } else {
                sb.append(prefix);
                prefix = ", ";
                sb.append(val);
            }
            i++;
        }
        sb.append(" ) ) \n");
        return sb.toString();
    }
}
