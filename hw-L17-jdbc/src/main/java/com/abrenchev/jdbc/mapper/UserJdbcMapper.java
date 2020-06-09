package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.model.User;
import com.abrenchev.jdbc.DbExecutor;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserJdbcMapper extends BaseJdbcMapper<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserJdbcMapper.class);

    public UserJdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<User> dbExecutor) {
        super(sessionManager, dbExecutor, User.class);
    }

    @Override
    public List<Object> getParamsForInsertion(User object) {
        return List.of(object.getName(), object.getAge());
    }

    @Override
    public User createInstance(ResultSet rs) {
        try {
            return new User(rs.getLong("id"), rs.getString("name"), rs.getInt("age"));
        } catch (SQLException exception) {
            logger.error(exception.getMessage(), exception);
        }

        return null;
    }
}
