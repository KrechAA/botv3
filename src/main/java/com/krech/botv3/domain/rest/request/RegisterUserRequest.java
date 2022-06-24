package com.krech.botv3.domain.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krech.botv3.domain.Role;

public class RegisterUserRequest extends CreateUserBaseRequest {


    public RegisterUserRequest() {
    }

    @Override
    @JsonIgnore
    public String getRole(){
        return Role.USER.getName();
    }
}