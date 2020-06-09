package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.sessionmanager.SessionManager;
import com.abrenchev.jdbc.DbExecutor;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseJdbcMapper<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseJdbcMapper.class);

    private final DbExecutor<T> dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;

    private final SessionManagerJdbc sessionManager;

    public BaseJdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, Class<T> clazz) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;

        var classMetadata = new EntityClassMetadataImpl<T>(clazz);
        entitySQLMetaData = new EntitySqlMetaDataImpl(
                classMetadata.getName(),
                classMetadata.getIdField().getName(),
                classMetadata.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList())
        );
    }

    @Override
    public void insert(T objectData) throws SQLException {
        dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                getParamsForInsertion(objectData));
    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void insertOrUpdate(T objectData) {

    }

    @Override
    public Optional<T> findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return createInstance(rs);
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Optional.empty();
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
