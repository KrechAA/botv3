package com.krech.botv3.domain.rest.request;

import lombok.Data;

@Data
public abstract class CreateUserBaseRequest {

    private String login;

    private String password;

    private String name;

    private String surname;

    public CreateUserBaseRequest() {
    }

    public CreateUserBaseRequest(String login, String password, String name, String surname) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public abstract String getRole();
}
