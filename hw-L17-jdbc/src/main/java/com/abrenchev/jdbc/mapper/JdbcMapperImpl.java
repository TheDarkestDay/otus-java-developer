package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.sessionmanager.SessionManager;
import com.abrenchev.jdbc.DbExecutor;
import com.abrenchev.jdbc.mapper.exceptions.JdbcMapperException;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final DbExecutor<T> dbExecutor;

    private final EntityClassMetaData<T> entityClassMetaData;

    private final EntitySQLMetaData entitySQLMetaData;

    private final SessionManagerJdbc sessionManager;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor, EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public void insert(T objectData) throws SQLException {
        dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                getParamsForInsertion(objectData));
    }

    @Override
    public void update(T objectData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertOrUpdate(T objectData) {
        throw new UnsupportedOperationException();
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

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private List<Object> getParamsForInsertion(T object) {
        List<Object> result = new ArrayList<>();
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();

        for (Field field : fields) {
            field.setAccessible(true);
            result.add(getFieldValue(field, object));
        }

        return result;
    }

    private T createInstance(ResultSet rs) {
        List<Field> fields = entityClassMetaData.getAllFields();
        T obj = createInstanceWithDefaultConstructor();

        for (Field field : fields) {
            setFieldValue(field, obj, rs);
        }

        return obj;
    }

    private Object getFieldValue(Field field, Object obj) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException exception) {
            throw new JdbcMapperException("Could't access field", exception);
        }
    }

    private void setFieldValue(Field field, Object obj, ResultSet rs) {
        try {
            field.setAccessible(true);
            var fieldType = field.getType();
            var fieldName = field.getName();

            if (fieldType.equals(Integer.TYPE)) {
                field.set(obj, rs.getInt(fieldName));
            } else {
                field.set(obj, rs.getObject(fieldName));
            }
        } catch (SQLException exception) {
            throw new JdbcMapperException("Could not retrieve field from ResultSet", exception);
        } catch (IllegalAccessException exception) {
            throw new JdbcMapperException("Could not access object field", exception);
        }
    }

    private T createInstanceWithDefaultConstructor() {
        try {
            return entityClassMetaData.getConstructor().newInstance();
        } catch (InstantiationException exception) {
            throw new JdbcMapperException("An object cannot be instantiated", exception);
        } catch (IllegalAccessException exception) {
            throw new JdbcMapperException("No available constructor was found", exception);
        } catch (InvocationTargetException exception) {
            throw new JdbcMapperException("An object constructor fired exception", exception);
        }
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
