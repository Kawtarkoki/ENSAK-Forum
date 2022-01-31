package com.forum.controller.userControllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.forum.model.RegistrationRequest;
import com.forum.model.User;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public List<User> usersList(){
        return userService.listAll();
    }


    @PostMapping("/new")
    public void registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        userService.encodeAndSave(new User(registrationRequest));
    }

    @PostMapping("/validateField")
    public void validateFields(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return;
    }

    @PostMapping("/validateUsername")
    public Boolean validateUsername(@RequestBody JsonNode usernameJson) {
        String username = usernameJson.get("username").asText();
        Boolean userFound = userService.findByUsername(username).isPresent();
        return userFound;
    }

}
