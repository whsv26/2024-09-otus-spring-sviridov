package me.whsv26.user.service;

import me.whsv26.user.model.User;

import java.util.UUID;

public interface UserService {

    User register(String username, String password);

    User findById(UUID userId);
}
