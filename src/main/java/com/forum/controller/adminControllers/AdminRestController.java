package com.forum.controller.adminControllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.forum.model.MyUserDetailes;
import com.forum.model.User;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/admin/sessions")
    public List<Object> getSession(){
        return sessionRegistry.getAllPrincipals();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(path = "/api/users/{username}", consumes = "application/json")
    public void disableAndExpireUserSessionByUsername(@PathVariable("username") String username, @RequestBody JsonNode mystring) {
        User user = userService.getUserByUsername(username).get();
        Boolean enabled = (mystring.get("enabled").asText().equalsIgnoreCase("Enabled"))? true : false;

        if(!enabled){
            List<Object> users = sessionRegistry.getAllPrincipals();
            for (Object principal : users) {
                if (principal instanceof MyUserDetailes) {
                    String principalUsername = ((MyUserDetailes) principal).getUsername();
                    if (principalUsername.equals(username)) {
                        List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                        if (sessionsInfo != null && sessionsInfo.size() > 0) {
                            for (SessionInformation sessionInformation : sessionsInfo) {
                                sessionInformation.expireNow();
                            }
                        }
                    }
                }
            }
        }
        user.setEnabled(enabled);
        userService.save(user);
    }


    @DeleteMapping(path = "/api/users/{username}")
    public void deleteAndExpireUserSessionByUsername(@PathVariable("username") String username) {
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof MyUserDetailes) {
                String principalUsername = ((MyUserDetailes) principal).getUsername();
                if (principalUsername.equals(username)) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (sessionsInfo != null && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        }
        userService.deleteByUsername(username);
    }
}
