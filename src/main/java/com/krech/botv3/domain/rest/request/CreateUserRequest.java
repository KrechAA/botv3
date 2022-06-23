package com.krech.botv3.domain.rest.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserRequest extends CreateUserBaseRequest {

    private String role;


}