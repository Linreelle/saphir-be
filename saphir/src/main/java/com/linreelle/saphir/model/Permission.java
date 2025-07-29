package com.linreelle.saphir.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin_read"),
    ADMIN_CREATE("admin_create"),
    ADMIN_DELETE("admin_delete"),
    ADMIN_UPDATE("admin_update"),

    MANAGER_READ("manager_read"),
    MANAGER_CREATE("manager_create"),
    MANAGER_DELETE("manager_delete"),
    MANAGER_UPDATE("manager_update"),

    USER_READ("user_read"),
    USER_CREATE("user_create"),
    USER_DELETE("user_delete"),
    USER_UPDATE("user_update");

    @Getter
    private final String permission;
}
