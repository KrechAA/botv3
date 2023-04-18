package com.krech.botv3.repository;

import com.krech.botv3.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * repository of users.
 * exist standard methods of JpaRepository and customise request to DB
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * search user by login
     * @param name login in string
     * @return existing user or null if user does not found
     */
    Optional<User> findByLogin(String name);


}
