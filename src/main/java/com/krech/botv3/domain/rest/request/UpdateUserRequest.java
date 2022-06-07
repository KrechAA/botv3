package com.krech.botv3.domain.rest.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String password;

    private String name;

    private String surname;

    private String role;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String password, String name, String surname, String role) {
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }
}