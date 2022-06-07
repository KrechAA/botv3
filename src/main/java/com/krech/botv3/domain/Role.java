package com.krech.botv3.domain;

import lombok.Getter;


@Getter
public enum Role {
    USER(1, "user"),
    ADMIN(2, "admin");

    private final int id;
    private final String name;

    Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role getById(int id) {
        for (Role role : Role.values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Can't find user role by id: " + id);
    }

    public static Role getByName(String roleName) {
        for (Role role : Role.values()) {
            if (role.name.equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Can't find user role by name: " + roleName);
    }
}