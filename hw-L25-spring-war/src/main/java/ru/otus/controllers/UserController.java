package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.services.DBServiceUser;

@Controller
public class UserController {
    private final DBServiceUser dbServiceUser;

    public UserController(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @GetMapping("/")
    public String usersListView(Model model) {
        dbServiceUser.getUsers().ifPresent(foundUsers -> model.addAttribute("users", foundUsers));

        return "users-list.html";
    }

    @GetMapping("/users/create")
    public String usersCreateView() {
        return "create-user.html";
    }
}
