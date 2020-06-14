package com.abrenchev;

import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import com.abrenchev.core.model.Account;
import com.abrenchev.core.model.User;
import com.abrenchev.core.service.DbServiceAccountImpl;
import com.abrenchev.core.service.DbServiceUserImpl;
import com.abrenchev.h2.DataSourceH2;
import com.abrenchev.jdbc.DbExecutorImpl;
import com.abrenchev.jdbc.dao.AccountMapperDao;
import com.abrenchev.jdbc.dao.UserMapperDao;
import com.abrenchev.jdbc.mapper.*;
import com.abrenchev.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        var dataSource = new DataSourceH2();
        var demo = new DbServiceDemo();

        demo.createTables(dataSource);

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userMetadata = new EntityClassMetadataImpl<>(User.class);
        var userSQLMetadata = new EntitySqlMetaDataImpl(userMetadata);
        var userJdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, userMetadata, userSQLMetadata);
        var userMapperDao = new UserMapperDao(userJdbcMapper);

        var dbServiceUser = new DbServiceUserImpl(userMapperDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 12));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}, age: {}", crUser.getName(), crUser.getAge()),
                () -> logger.info("user was not created")
        );

        DbExecutorImpl<Account> accountDbExecutor = new DbExecutorImpl<>();
        var accountMetadata = new EntityClassMetadataImpl<>(Account.class);
        var accountSQLMetadata = new EntitySqlMetaDataImpl(accountMetadata);
        var accountJdbcMapper = new JdbcMapperImpl<>(sessionManager, accountDbExecutor, accountMetadata, accountSQLMetadata);
        var accountMapperDao = new AccountMapperDao(accountJdbcMapper);
        var dbServiceAccount = new DbServiceAccountImpl(accountMapperDao);
        var accountId = dbServiceAccount.saveAccount(new Account(3, "regular", 100));
        Optional<Account> foundAccount = dbServiceAccount.getAccount(accountId);

        foundAccount.ifPresentOrElse(
                account -> logger.info("created account, type: {}, rest: {}", account.getType(), account.getRest()),
                () -> logger.info("account was not created")
        );

    }

    private void createTables(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var createUserStatement = connection.prepareStatement("create table user(id bigint(20) not null auto_increment, name varchar(255), age int(3))");
             var createAccountStatement = connection.prepareStatement("create table account(no bigint(20) not null auto_increment, type varchar(255), rest number)")) {
            createUserStatement.executeUpdate();
            createAccountStatement.executeUpdate();
        }
        System.out.println("table created");
    }
}
