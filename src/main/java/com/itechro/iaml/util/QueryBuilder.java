package com.itechro.iaml.util;

import com.itechro.iaml.commons.constants.AppsConstants;
import com.itechro.iaml.exception.impl.AppsCommonErrorCode;
import com.itechro.iaml.exception.impl.AppsRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(QueryBuilder.class);
    private StringBuilder query = new StringBuilder();
    private Map<String, Object> keyMap = new HashMap<>();
    private List<Map<String, Object>> keyMapList = new ArrayList<>();

    public QueryBuilder(String query) {
        this.query.append(query);
    }

    public QueryBuilder() {
    }

    public void nextkeyMap() {
        if (this.keyMap.size() > 0) {
            keyMapList.add(keyMap);
            keyMap = new HashMap<>();
        }
    }

    public Map<String, Object>[] getKeyMaps() {
        Map<String, Object>[] map = new Map[keyMapList.size()];
        return keyMapList.toArray(map);
    }

    private boolean canAppendQuery(Object parameter) {
        if (parameter instanceof String) {
            if (((String) parameter).replaceAll("%", "").trim().equals("") || ((String) parameter).replaceAll("%",
                    "").trim().equals("null")) {
                return false;
            }
        }

        if (parameter instanceof List) {
            if (((List) parameter).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private String getParamNameFromQueryAppend(String queryAppend) {
        int paramStartIndex = queryAppend.indexOf(":");
        int spaceEnd = queryAppend.indexOf(" ", paramStartIndex + 1);
        int commaEnd = queryAppend.indexOf(",", paramStartIndex + 1);
        int lastIndex = queryAppend.length();
        if (spaceEnd > 0 && spaceEnd < lastIndex) {
            lastIndex = spaceEnd;
        }
        if (commaEnd > 0 && commaEnd < lastIndex) {
            lastIndex = commaEnd;
        }
        return queryAppend.substring(paramStartIndex + 1, lastIndex).trim();
    }

    private void appendParameterIfPossible(String queryAppend, Object parameter, boolean isMandatory) {

        parameter = this.getStringFormOfKnownTypes(parameter);

        if (canAppendQuery(parameter)) {
            query.append(" ").append(queryAppend);
            keyMap.put(getParamNameFromQueryAppend(queryAppend), parameter);
        } else {
            if (isMandatory) {
                LOG.error("Manadatory field missing in query append {}", queryAppend);
                AppsRuntimeException exception = new AppsRuntimeException(AppsCommonErrorCode.APPS_INVALID_ARGUMENTS,
                        null,
                        AppsConstants.ResponseStatus.FAILED);
                exception.setBadRequest();
            }
        }
    }


    public QueryBuilder appendValueOrNull(String queryAppend, Object parameter) {
        parameter = getStringFormOfKnownTypes(parameter);
        appendParameterIfPossible(queryAppend, parameter, false);
        return this;
    }


    public QueryBuilder appendNotEmpty(String queryAppend, Object parameter) {
        if (parameter != null) {
            appendParameterIfPossible(queryAppend, parameter, false);
        }
        return this;
    }

    public QueryBuilder appendNotNullOrEmpty(String queryAppend, Object parameter) {
        if (parameter != null) {
            appendParameterIfPossible(queryAppend, parameter, false);
        }
        return this;
    }

    public QueryBuilder appendNotNullOrEmptyLike(String queryAppend, String... parameters) {
        if (parameters != null) {
            StringBuffer parameter = new StringBuffer("");
            for (String param : parameters) {
                if (param != null) {
                    parameter.append(param);
                }
            }

            appendParameterIfPossible(queryAppend, parameter.toString(), false);
        }
        return this;
    }

    public QueryBuilder appendNotNullMandatory(String queryAppend, Object parameter) {
        if (parameter == null) {
            LOG.error("Mandatory field missing : {}", queryAppend);
            AppsRuntimeException exception = new AppsRuntimeException(
                    AppsCommonErrorCode.APPS_INVALID_ARGUMENTS,
                    "Parameter missing for query line : [ " + queryAppend + " ]",
                    AppsConstants.ResponseStatus.FAILED);
            exception.setBadRequest();
            throw exception;
        }

        appendParameterIfPossible(queryAppend, parameter, true);
        return this;
    }

    public QueryBuilder appendNotNullOrEmptyMandatory(String queryAppend, Object parameter) {
        if (parameter == null) {
            LOG.error("Manadatory field missing in query append {}", queryAppend);
            AppsRuntimeException exception = new AppsRuntimeException(AppsCommonErrorCode.APPS_INVALID_ARGUMENTS,
                    null,
                    AppsConstants.ResponseStatus.FAILED);
            exception.setBadRequest();
            throw exception;
        }

        appendParameterIfPossible(queryAppend, parameter, true);
        return this;
    }

    public QueryBuilder appendParameterWithoutValidation(String queryAppend, Object parameter) {
        query.append(" ").append(queryAppend);
        keyMap.put(getParamNameFromQueryAppend(queryAppend), parameter);
        return this;
    }

    public QueryBuilder appendIfNotNull(String queryAppend, Object parameter) {
        if (parameter != null && canAppendQuery(parameter)) {
            query.append(" ").append(queryAppend);
        }
        return this;
    }

    public QueryBuilder addParameterForMap(String paramName, Object parameter) {

        parameter = getStringFormOfKnownTypes(parameter);
        this.keyMap.put(paramName, parameter);
        return this;
    }

    public QueryBuilder addParameterForMapMandatory(String paramName, Object parameter) {
        if (parameter != null && canAppendQuery(parameter)) {
            parameter = getStringFormOfKnownTypes(parameter);
            this.keyMap.put(paramName, parameter);
            return this;
        }
        LOG.error("Mandatory field missing in query append {}", paramName);
        AppsRuntimeException exception = new AppsRuntimeException(AppsCommonErrorCode.APPS_INVALID_ARGUMENTS,
                "Mandatory field is null : [ " + paramName + " ]",
                AppsConstants.ResponseStatus.FAILED);
        exception.setBadRequest();
        throw exception;
    }


    /**
     * This method only removes '=' && '!=' if null. Hence don't send queries with in
     *
     * @param queryAppend
     * @param parameter
     * @return
     */
    public QueryBuilder appendNullForNullOrEmptyParameterForWhere(String queryAppend, Object parameter) {
        if (parameter != null) {
            appendParameterIfPossible(queryAppend, parameter, false);
        } else {
            appendNotEqual(queryAppend);
        }
        return this;
    }

    private void appendNotEqual(String queryAppend) {
        if (queryAppend.contains(Operation.EQUAL.getOperator())) {
            String substring = queryAppend.substring(0, queryAppend.indexOf(Operation.EQUAL.getOperator()));
            query.append(substring).append(' ').append(Operation.EQUAL.getDescription()).append(' ');
        } else if (queryAppend.contains(Operation.NOT_EQUAL.getOperator())) {
            String substring = queryAppend.substring(0, queryAppend.indexOf(Operation.NOT_EQUAL.getOperator()));
            query.append(substring).append(' ').append(Operation.NOT_EQUAL.getDescription()).append(' ');
        }
    }

    /**
     * This method should not be called parameter values, since they won't be added to map
     *
     * @param queryAppend The string to append
     * @return query appended builder object
     */
    public QueryBuilder append(String queryAppend) {
        if ((queryAppend.toLowerCase().contains("order by")) || queryAppend.toLowerCase().trim().startsWith("and")
                || queryAppend.toLowerCase().trim().startsWith("or")) {

            // remove the where part from query string if it ends with where
            cleanupQuery(new StringBuilder(queryAppend));
        }

        query.append(" ").append(queryAppend);
        return this;
    }

    /**
     * Returnt the string representation of the query that was  built
     *
     * @return final string query
     */
    public String getQuery() {

        StringBuilder cleanedQuery = this.cleanupQuery(this.query);
        String querySting = cleanedQuery.toString().trim();
        return querySting;

    }

    /**
     * Clean up WHERE, AND, OR from the end of a query
     *
     * @param stringBuilder the built query builder init
     * @return Cleaned up query
     */
    private StringBuilder cleanupQuery(StringBuilder stringBuilder) {
        String queryString = stringBuilder.toString().trim().toLowerCase();
        if (queryString.endsWith(" where")) {
            stringBuilder.setLength(stringBuilder.lastIndexOf(" where"));
        } else if (queryString.endsWith(" and")) {
            stringBuilder.setLength(stringBuilder.lastIndexOf(" and"));
        } else if (queryString.endsWith(" or")) {
            stringBuilder.setLength(stringBuilder.lastIndexOf(" or"));
        }
        return stringBuilder;
    }


    /**
     * This will return a map with not null parameters to be attached.
     * For this map to work parameter queries should not be appended.
     *
     * @return
     */
    public Map<String, Object> getParameterMap() {
        return this.keyMap;
    }

    public String getTotalCountQuery() {

        String original = getQuery();

        int selectEnd = original.toLowerCase().indexOf("select") + 6;
        int fromStart = original.toLowerCase().indexOf("from");

        String totalRecQuery = "select count(" + (original.substring(selectEnd, fromStart)).trim() + ") "
                + original.substring(fromStart);

        return totalRecQuery;

    }

    @Override
    public String toString() {
        return getQuery();
    }

    private Object getStringFormOfKnownTypes(Object parameter) {
        if (parameter == null) {
            return null;
        } else if (parameter instanceof Enum) {
            parameter = parameter.toString();
        } else if (parameter instanceof Character) {
            parameter = parameter.toString();
        } else if (parameter instanceof BigDecimal) {
            parameter = ((BigDecimal) parameter).toPlainString();
        }
        return parameter;
    }

    private enum Operation {
        EQUAL("=", "IS NULL"), NOT_EQUAL("!=", "IS NOT NULL");

        private String operator;

        private String description;

        private Operation(String operator, String description) {
            this.operator = operator;
            this.description = description;
        }

        private String getOperator() {
            return operator;
        }

        private String getDescription() {
            return description;
        }
    }
}
