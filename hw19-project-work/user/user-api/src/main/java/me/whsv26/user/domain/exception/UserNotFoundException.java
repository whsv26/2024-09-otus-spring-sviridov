package me.whsv26.user.domain.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final UUID userId;

    public UserNotFoundException(UUID userId) {
        super("User " + userId + " doesn't exist");
        this.userId = userId;
    }
}
