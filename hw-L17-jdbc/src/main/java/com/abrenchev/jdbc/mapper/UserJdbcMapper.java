package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.dao.UserDaoException;
import com.abrenchev.core.model.User;
import com.abrenchev.core.sessionmanager.SessionManager;
import com.abrenchev.jdbc.DbExecutor;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserJdbcMapper implements JdbcMapper<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserJdbcMapper.class);

    private final DbExecutor<User> dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;

    private final SessionManagerJdbc sessionManager;

    public UserJdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;

        var classMetadata = new EntityClassMetadataImpl<User>(User.class);
        entitySQLMetaData = new EntitySqlMetaDataImpl(
                classMetadata.getName(),
                classMetadata.getIdField().getName(),
                classMetadata.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList())
        );
    }

    @Override
    public void insert(User objectData) throws SQLException {
        dbExecutor.executeInsert(getConnection(), entitySQLMetaData.getInsertSql(),
                Collections.singletonList(objectData.getName()));
    }

    @Override
    public void update(User objectData) {

    }

    @Override
    public void insertOrUpdate(User objectData) {

    }

    @Override
    public User findById(long id) {
        try {
            return dbExecutor.executeSelect(getConnection(), entitySQLMetaData.getSelectByIdSql(),
                    id, rs -> {
                        try {
                            if (rs.next()) {
                                return new User(rs.getLong("id"), rs.getString("name"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    }).get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
