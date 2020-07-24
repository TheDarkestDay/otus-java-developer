package ru.otus.controllers;

import ru.otus.services.DBServiceUser;
import ru.otus.services.TemplateProcessor;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersListServlet extends HttpServlet {
    private static final String USERS_LIST_PAGE_TEMPLATE = "users-list.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final DBServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public UsersListServlet(TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        dbServiceUser.getUsers()
                .ifPresent(foundUsers -> paramsMap.put(TEMPLATE_ATTR_USERS, foundUsers));

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_LIST_PAGE_TEMPLATE, paramsMap));
    }
}
