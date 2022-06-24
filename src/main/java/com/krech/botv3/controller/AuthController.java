package com.krech.botv3.controller;

import com.krech.botv3.domain.User;
import com.krech.botv3.domain.rest.request.LogInRequest;
import com.krech.botv3.domain.rest.request.RegisterUserRequest;
import com.krech.botv3.domain.rest.response.AuthenticationResponse;
import com.krech.botv3.service.JwtService;
import com.krech.botv3.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserService userService;

    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping(path = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> doLogin(@RequestBody LogInRequest logInRequest) {
        User user = userService.findByLoginAndVerifyPassword(logInRequest.getLogin(), logInRequest.getPassword());
        String jwtToken = jwtService.generateToken(user.getLogin());
        return new ResponseEntity<>(new AuthenticationResponse(jwtToken), HttpStatus.OK);
    }


    @PostMapping(path = "/auth/register")
    public void register(@RequestBody RegisterUserRequest registerUserRequest) {
        userService.create(registerUserRequest);
    }
}
