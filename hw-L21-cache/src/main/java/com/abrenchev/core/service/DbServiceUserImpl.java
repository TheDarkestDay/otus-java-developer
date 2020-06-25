package com.abrenchev.core.service;

import com.abrenchev.cachehw.HwCache;
import com.abrenchev.core.dao.UserDao;
import com.abrenchev.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    private final HwCache<String, User> cache;

    public DbServiceUserImpl(UserDao userDao, HwCache<String, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                var stringId = String.valueOf(userId);
                sessionManager.commitSession();
                user.setId(userId);
                cache.put(stringId, user);

                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        String stringId = String.valueOf(id);
        User cachedUser = cache.get(stringId);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }

        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                User user = userOptional.orElse(null);
                Thread.sleep(1500);
                cache.put(stringId, user);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
