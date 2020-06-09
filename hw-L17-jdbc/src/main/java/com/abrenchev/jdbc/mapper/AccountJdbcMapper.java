package com.abrenchev.jdbc.mapper;

import com.abrenchev.core.model.Account;
import com.abrenchev.core.model.User;
import com.abrenchev.jdbc.DbExecutor;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountJdbcMapper extends BaseJdbcMapper<Account> {
    private static final Logger logger = LoggerFactory.getLogger(UserJdbcMapper.class);

    public AccountJdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<Account> dbExecutor) {
        super(sessionManager, dbExecutor, Account.class);
    }

    @Override
    public Account createInstance(ResultSet rs) {
        try {
            return new Account(rs.getLong("no"), rs.getString("type"), rs.getInt("rest"));
        } catch (SQLException exception) {
            logger.error(exception.getMessage(), exception);
        }

        return null;
    }

    @Override
    public List<Object> getParamsForInsertion(Account object) {
        return List.of(object.getType(), object.getRest());
    }
}
