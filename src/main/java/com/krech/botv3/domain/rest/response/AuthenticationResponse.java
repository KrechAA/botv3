package com.krech.botv3.domain.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;




@ToString
@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {

    private String jwtToken;
}