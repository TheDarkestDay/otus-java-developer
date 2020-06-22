package com.abrenchev;

import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import com.abrenchev.cachehw.CacheEvent;
import com.abrenchev.cachehw.HwCache;
import com.abrenchev.cachehw.HwListener;
import com.abrenchev.cachehw.MyCache;
import com.abrenchev.core.model.User;
import com.abrenchev.core.service.DbServiceUserImpl;
import com.abrenchev.h2.DataSourceH2;
import com.abrenchev.jdbc.DbExecutorImpl;
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

        var cache = new MyCache<String, Optional<User>>();

        cache.addListener(new HwListener<String, Optional<User>>() {
            @Override
            public void notify(String key, Optional<User> value, CacheEvent action) {
                logger.info("event: {}; key: {}; value: {}", action, key, value);
            }
        });

        var dbServiceUser = new DbServiceUserImpl(userMapperDao, cache);

        var usersCount = 10;
        for (int i = 0; i < usersCount; i++) {
            dbServiceUser.saveUser(new User(i, "dbServiceUser" + i, 12));
        }

        // Getting users from DbService
        for (int i = 0; i < usersCount; i++) {
            logger.info("Got user: {}", dbServiceUser.getUser(i));
        }

        // Here we suppose to get users form cache
        for (int i = 0; i < usersCount; i++) {
            logger.info("Got user: {}", dbServiceUser.getUser(i));
        }
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
