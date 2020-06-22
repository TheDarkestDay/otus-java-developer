package com.abrenchev.core.dao;

import com.abrenchev.core.model.Account;
import com.abrenchev.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface AccountDao {
    Optional<Account> findById(long id);

    long insertAccount (Account account);

    //void updateUser(User user);
    //void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
