package com.abrenchev.jdbc.dao;

import com.abrenchev.core.dao.AccountDao;
import com.abrenchev.core.dao.UserDaoException;
import com.abrenchev.core.model.Account;
import com.abrenchev.core.sessionmanager.SessionManager;
import com.abrenchev.jdbc.mapper.JdbcMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AccountMapperDao implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountMapperDao.class);

    private final JdbcMapper<Account> accountJdbcMapper;

    public AccountMapperDao(JdbcMapper<Account> accountJdbcMapper) {
        this.accountJdbcMapper = accountJdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        return this.accountJdbcMapper.findById(id);
    }

    @Override
    public long insertAccount(Account account) {
        try {
            accountJdbcMapper.insert(account);

            return 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return accountJdbcMapper.getSessionManager();
    }
}
