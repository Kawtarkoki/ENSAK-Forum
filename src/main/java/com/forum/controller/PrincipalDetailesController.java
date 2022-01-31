package com.forum.controller;

import com.forum.model.User;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class PrincipalDetailesController
{

    @Autowired
    private UserService userService;

    @GetMapping("/principal_id")
    public Long prirncipalProvider(Principal principal){
        if(principal == null) return null;
        User user = userService.getUserByUsername(principal.getName()).get();
        return user.getId();
    }
}
