package com.krech.botv3.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column (name = "login", nullable = false)
    private String login;

    @JsonIgnore
    @Column (name = "password", nullable = false)
    private String password;

    @Column (name = "name", nullable = false)
    private String name;

    @Column (name = "surname", nullable = false)
    private String surname;

    @JsonIgnore
    private int role;



    @JsonProperty("role")

    public String getRoleStr() {
        return Role.getById(role).getName();
    }


}