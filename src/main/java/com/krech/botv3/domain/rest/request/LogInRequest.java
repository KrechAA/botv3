package com.krech.botv3.domain.rest.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {

    private String login;

    private String password;


}
