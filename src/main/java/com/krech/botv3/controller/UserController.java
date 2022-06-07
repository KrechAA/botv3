package com.krech.botv3.controller;

import com.krech.botv3.domain.User;
import com.krech.botv3.domain.rest.request.CreateUserRequest;
import com.krech.botv3.domain.rest.request.UpdateUserRequest;
import com.krech.botv3.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value = "/users/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping(value = "/users/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser( @RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.create(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }


    @PutMapping(value = "users/update/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,@RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = userService.updateUser(userId, updateUserRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @DeleteMapping(value = "users/delete/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}