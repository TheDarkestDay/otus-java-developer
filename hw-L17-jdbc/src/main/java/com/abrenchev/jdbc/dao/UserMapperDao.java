package com.abrenchev.jdbc.dao;

import com.abrenchev.core.dao.UserDao;
import com.abrenchev.core.dao.UserDaoException;
import com.abrenchev.core.model.User;
import com.abrenchev.core.sessionmanager.SessionManager;
import com.abrenchev.jdbc.mapper.JdbcMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserMapperDao implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserMapperDao.class);

    private final JdbcMapper<User> userJdbcMapper;

    public UserMapperDao(JdbcMapper<User> userJdbcMapper) {
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return this.userJdbcMapper.findById(id);
    }

    @Override
    public long insertUser(User user) {
        try {
            userJdbcMapper.insert(user);

            return 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return userJdbcMapper.getSessionManager();
    }
}
