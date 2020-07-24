package ru.otus.controllers;

import com.google.gson.Gson;
import ru.otus.core.dto.SaveUserDto;
import ru.otus.core.model.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.DbServiceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersApiServlet extends HttpServlet {
    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.dbServiceUser = dbServiceUser;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String requestBody = req.getReader().readLine();
        SaveUserDto dto = gson.fromJson(requestBody, SaveUserDto.class);
        User newUser = new User();
        newUser.setName(dto.name);

        try {
            dbServiceUser.saveUser(newUser);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DbServiceException exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
