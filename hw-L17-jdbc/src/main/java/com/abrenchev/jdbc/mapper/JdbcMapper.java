package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.sessionmanager.SessionManager;

import java.sql.SQLException;
import java.util.Optional;

public interface JdbcMapper<T> {
    void insert(T objectData) throws SQLException;

    void update(T objectData);

    void insertOrUpdate(T objectData);

    Optional<T> findById(long id);

    SessionManager getSessionManager();
}
