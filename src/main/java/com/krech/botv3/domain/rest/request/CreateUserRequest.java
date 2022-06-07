package com.krech.botv3.domain.rest.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest extends CreateUserBaseRequest {

    private String role;

    public CreateUserRequest() {
    }

}