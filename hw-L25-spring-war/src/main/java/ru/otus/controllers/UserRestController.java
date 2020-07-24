package ru.otus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.core.dto.SaveUserDto;
import ru.otus.core.model.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.DbServiceException;

@RestController
public class UserRestController {
    private final DBServiceUser dbServiceUser;

    public UserRestController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @PostMapping("/api/user")
    public ResponseEntity<User> createUser(@RequestBody SaveUserDto newUserData) {
        try {
            User newUser = new User();
            newUser.setName(newUserData.name);
            dbServiceUser.saveUser(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DbServiceException exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
