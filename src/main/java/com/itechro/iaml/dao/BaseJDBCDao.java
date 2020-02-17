package com.itechro.iaml.dao;

import com.itechro.iaml.model.common.Page;
import com.itechro.iaml.model.common.PagedParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Repository
public class BaseJDBCDao {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected <T extends Serializable> Page<T> getPagedData(String dataQuery, Map<String, Object> paramsMap,
                                                            RowMapper<T> rowMapper, PagedParamDTO gridPramDTO) {

        Integer pageIndex = gridPramDTO.getPage() - 1;
        Integer rows = gridPramDTO.getRows();
        Integer start = pageIndex * rows;

        Long totalNoOfRecs = namedParameterJdbcTemplate.queryForObject(this.getCountQuery(dataQuery), paramsMap,
                Long.class);

        paramsMap.put("start", start);
        paramsMap.put("noOfRows", rows);

        Collection<T> resultsList = namedParameterJdbcTemplate.query(getPagedQuery(dataQuery), paramsMap,
                rowMapper);

        if (resultsList == null) {
            resultsList = new ArrayList<T>();
        }

        return new Page<T>(totalNoOfRecs, start, resultsList.size(), rows, resultsList);
    }

    protected <T extends Serializable> Page<T> getPagedData(String dataQuery, MapSqlParameterSource paramsMap,
                                                            RowMapper<T> rowMapper, PagedParamDTO gridPramDTO) {

        Integer pageIndex = gridPramDTO.getPage() - 1;
        Integer rows = gridPramDTO.getRows();
        Integer start = pageIndex * rows;

        Long totalNoOfRecs = namedParameterJdbcTemplate.queryForObject(this.getCountQuery(dataQuery), paramsMap,
                Long.class);

        paramsMap.addValue("start", start);
        paramsMap.addValue("noOfRows", rows);

        Collection<T> resultsList = namedParameterJdbcTemplate.query(getPagedQuery(dataQuery), paramsMap,
                rowMapper);

        if (resultsList == null) {
            resultsList = new ArrayList<T>();
        }

        return new Page<T>(totalNoOfRecs, start, resultsList.size(), rows, resultsList);
    }

    private String getPagedQuery(String query) {
        StringBuilder pagedQuery = new StringBuilder(query);
        pagedQuery.append(" LIMIT :start,:noOfRows  ");

        return pagedQuery.toString();
    }

    private String getCountQuery(String query) {
        StringBuilder countQuery = new StringBuilder();
        countQuery.append("SELECT count(*) FROM ( ");
        countQuery.append(query);
        countQuery.append(" ) count");

        return countQuery.toString();
    }
}
