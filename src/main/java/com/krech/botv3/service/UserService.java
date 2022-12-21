package com.krech.botv3.service;

import com.krech.botv3.domain.Role;
import com.krech.botv3.domain.User;
import com.krech.botv3.domain.rest.request.CreateUserBaseRequest;
import com.krech.botv3.domain.rest.request.UpdateUserRequest;
import com.krech.botv3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Can't find user by id " + userId));
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new NoSuchElementException("Can't find user by login " + login));
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
   public User create(CreateUserBaseRequest request) {
        userRepository.findByLogin(request.getLogin()).ifPresent(user -> {
            throw new IllegalArgumentException("User with login " + user.getLogin() + " already exists");
        });
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setRole(Role.getByName(request.getRole()).getId());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long userId, UpdateUserRequest request) {
        User existingUser = userRepository.findById(userId).orElseThrow(() ->  {
            throw new IllegalArgumentException("Can't find user by id " + userId);
        });
        if (!ObjectUtils.isEmpty(request.getPassword())) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        }
        existingUser.setName(request.getName());
        existingUser.setSurname(request.getSurname());
        existingUser.setRole(Role.getByName(request.getRole()).getId());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findByLoginAndVerifyPassword(String login, String password) {
        AuthenticationServiceException authenticationException = new AuthenticationServiceException("Invalid login/password pair");
        User user = userRepository.findByLogin(login).orElseThrow(() -> authenticationException);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw authenticationException;
    }
}