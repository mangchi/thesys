package com.thesys.titan.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private String status;
    private String createdAt;

}
