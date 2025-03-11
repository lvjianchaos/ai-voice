package com.example.service;

import com.example.entity.User;

public interface UserService {
    User findUserByName(String username);

    void register(String username, String password);
}
