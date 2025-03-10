package ru.otus.hw.application;

import ru.otus.hw.domain.User;

public interface UserService {

    User register(String username, String password);
}
