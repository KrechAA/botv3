package com.krech.botv3.domain.rest.request;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class CreateUserBaseRequest {

    private String login;

    private String password;

    private String name;

    private String surname;


    public abstract String getRole();
}
