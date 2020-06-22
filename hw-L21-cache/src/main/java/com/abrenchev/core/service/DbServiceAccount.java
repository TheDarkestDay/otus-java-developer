package com.abrenchev.core.service;

import com.abrenchev.core.model.Account;
import java.util.Optional;

public interface DbServiceAccount {
    long saveAccount(Account account);

    Optional<Account> getAccount(long id);
}
