package me.whsv26.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.user.service.UserService;
import me.whsv26.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/api/users")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        var user = userService.register(request.username, request.password);
        return new RegisterResponse(userMapper.map(user));
    }

    @GetMapping("/api/users/me")
    public ReadUserResponse readUser() {
        var userId = UUID.fromString(currentUserProvider.getCurrentUser().userId());
        var user = userService.findById(userId);
        return new ReadUserResponse(userMapper.map(user));
    }

    public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String passwordConfirmation
    ) {
        @AssertTrue
        public boolean isSamePassword() {
            return password.equals(passwordConfirmation);
        }
    }

    public record RegisterResponse(UserResponse user) { }

    public record ReadUserResponse(UserResponse user) { }

    public record UserResponse(String username) { }

    @Mapper
    interface UserMapper {
        UserResponse map(User source);
    }
}
