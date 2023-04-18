package com.krech.botv3.service;

import com.krech.botv3.domain.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * authentication service
 */
@Service
public class AuthService {
    public User getCurrentAuthenticatedUser() {
        Authentication u = SecurityContextHolder.getContext().getAuthentication();
        if (u == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
        return (User) u.getPrincipal();
    }
}
