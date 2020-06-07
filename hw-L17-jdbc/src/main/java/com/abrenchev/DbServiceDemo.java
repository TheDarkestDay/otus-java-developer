package com.abrenchev;

import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import com.abrenchev.core.model.User;
import com.abrenchev.core.service.DbServiceUserImpl;
import com.abrenchev.h2.DataSourceH2;
import com.abrenchev.jdbc.DbExecutorImpl;
import com.abrenchev.jdbc.dao.UserMapperDao;
import com.abrenchev.jdbc.mapper.UserJdbcMapper;
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

        demo.createTable(dataSource);

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        var userJdbcMapper = new UserJdbcMapper(sessionManager, dbExecutor);
        var userMapperDao = new UserMapperDao(userJdbcMapper);

        var dbServiceUser = new DbServiceUserImpl(userMapperDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser"));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );

    }

    private void createTable(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }
}
