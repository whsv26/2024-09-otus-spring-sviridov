package me.whsv26.user.application;

import me.whsv26.user.domain.entity.User;

import java.util.UUID;

public interface UserService {

    User register(String username, String password);

    User findById(UUID userId);
}
