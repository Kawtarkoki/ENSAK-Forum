package com.forum.model.role;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static com.forum.model.role.UserPermission.*;


public enum UserRole {

    ADMIN(Arrays.asList(
            POST_READ, POST_WRITE,
            USER_READ, USER_WRITE
    )),

    USER(Arrays.asList(
            POST_READ, POST_WRITE,
            USER_READ
    )),
    ENTREPRENEUR(Arrays.asList(
            POST_READ, POST_WRITE,
         USER_READ
     ));

    private final List<UserPermission> permissions;

    UserRole(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public List<UserPermission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        List<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
