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

/**
 * controller for users administrating
 */
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * serching user by id
     * @param userId
     * @return existing user
     */
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * searching all users in DB
     * @return list of all users
     */
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * add new user
     * @param createUserRequest
     * @return new user
     */
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser( @RequestBody CreateUserRequest createUserRequest) {
        User createdUser = userService.create(createUserRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    /**
     * update existing user
     * @param userId
     * @param updateUserRequest
     * @return updated user
     */
    @PutMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,@RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = userService.updateUser(userId, updateUserRequest);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * delete user from DB
     * @param userId
     * @return http status
     */
    @DeleteMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}