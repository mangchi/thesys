package com.thesys.titan.user.service;

import com.thesys.titan.dao.DAO;
// import com.thesys.titan.user.dto.UserRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService {

    private final DAO dao;

    // @Override
    // public int createUser(UserRequest userDto) {
    // return dao.update("User.createUser", userDto);
    // }
}
