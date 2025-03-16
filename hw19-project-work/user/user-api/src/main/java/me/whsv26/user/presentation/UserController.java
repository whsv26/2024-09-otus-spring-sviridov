package me.whsv26.user.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import me.whsv26.user.application.UserService;
import me.whsv26.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @PostMapping("/api/users")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        var user = userService.register(request.username, request.password);
        return new RegisterResponse(userMapper.map(user));
    }

    @PostMapping("/internal/users/{userId}")
    public RegisterResponse readUser(@RequestBody @Valid RegisterRequest request) {
        var user = userService.register(request.username, request.password);
        return new RegisterResponse(userMapper.map(user));
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

    public record UserResponse(String username) { }

    @Mapper
    interface UserMapper {
        UserResponse map(User source);
    }
}
