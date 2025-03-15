package me.whsv26.user.application;

import me.whsv26.user.domain.User;

public interface UserService {

    User register(String username, String password);
}
