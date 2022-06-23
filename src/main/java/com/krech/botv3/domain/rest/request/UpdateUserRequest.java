package com.krech.botv3.domain.rest.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String password;

    private String name;

    private String surname;

    private String role;


}