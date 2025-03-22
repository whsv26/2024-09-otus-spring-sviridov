package me.whsv26.user.domain.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private final String username;

    public UserAlreadyExistsException(String username, Throwable cause) {
        super("User " + username + " already exists", cause);
        this.username = username;
    }
}
