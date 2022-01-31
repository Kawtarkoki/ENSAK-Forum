package com.forum.controller.userControllers;

import com.forum.model.MyUserDetailes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;


@RestController
public class UserSessionController {
    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/users/sessions")
    public List<Object> list() {
        return sessionRegistry.getAllPrincipals();
    }

    @GetMapping("/users/{userId}/sessions")
    public List<SessionInformation> listForUser(@PathVariable String userId) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            MyUserDetailes user = (MyUserDetailes) principal;
            if (user.getUsername().equals(userId)) {
                return sessionRegistry.getAllSessions(principal, false);
            }
        }
        throw new NotFoundException("Couldn't find sessions for user " + userId);
    }

    @RequestMapping(value = "/sessions/{sessionId}", method = RequestMethod.GET)
    public String expireForm(@PathVariable String sessionId) {
        SessionInformation sessionInfo = sessionRegistry.getSessionInformation(sessionId);
        sessionInfo.expireNow();
        return null;
    }
}

