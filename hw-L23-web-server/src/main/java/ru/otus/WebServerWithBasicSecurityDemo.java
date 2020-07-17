package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Address;
import ru.otus.core.model.Phone;
import ru.otus.core.model.User;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithBasicSecurity;
import ru.otus.services.DBServiceUser;
import ru.otus.services.DbServiceUserImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/*
    // Index page
    http://localhost:8080

    // Create new user page
    http://localhost:8080/users/create

    // Show all users page
    http://localhost:8080/users/

    // Users REST endpoint
    http://localhost:8080/api/users/
*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, Address.class, Phone.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, dbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
