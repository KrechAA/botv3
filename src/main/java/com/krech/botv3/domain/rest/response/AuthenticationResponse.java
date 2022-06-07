package com.krech.botv3.domain.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuthenticationResponse {

    private String jwtToken;
}