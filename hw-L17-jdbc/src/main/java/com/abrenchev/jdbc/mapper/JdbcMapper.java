package com.abrenchev.jdbc.mapper;

import java.sql.SQLException;
import java.util.Optional;

public interface JdbcMapper<T> {
    void insert(T objectData) throws SQLException;

    void update(T objectData);

    void insertOrUpdate(T objectData);

    T findById(long id);
}
