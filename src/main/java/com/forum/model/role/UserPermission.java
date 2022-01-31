package com.forum.model.role;

public enum UserPermission {
    POST_READ("post:read"),
    POST_WRITE("post:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;


    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
