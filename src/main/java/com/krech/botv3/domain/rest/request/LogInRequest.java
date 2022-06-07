package com.krech.botv3.domain.rest.request;

import lombok.Data;

@Data
public class LogInRequest {

    private String login;

    private String password;

    public LogInRequest() {
    }

    public LogInRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
