package com.abrenchev.core.dao;

import com.abrenchev.core.model.User;
import com.abrenchev.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    //void updateUser(User user);
    //void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
