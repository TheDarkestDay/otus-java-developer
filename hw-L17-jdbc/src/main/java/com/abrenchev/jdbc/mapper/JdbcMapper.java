package com.abrenchev.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface JdbcMapper<T> {
    void insert(T objectData) throws SQLException;

    void update(T objectData);

    void insertOrUpdate(T objectData);

    Optional<T> findById(long id);

    T createInstance(ResultSet rs);

    List<Object> getParamsForInsertion(T object);
}
