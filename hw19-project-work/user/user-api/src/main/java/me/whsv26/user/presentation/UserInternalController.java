package me.whsv26.user.presentation;

import lombok.RequiredArgsConstructor;
import me.whsv26.user.application.UserService;
import me.whsv26.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserInternalController {

    private final UserService userService;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @GetMapping("/internal/users/{userId}")
    public ReadUserResponse readUser(@PathVariable("userId") UUID userId) {
        var user = userService.findById(userId);
        return new ReadUserResponse(userMapper.map(user));
    }

    public record ReadUserResponse(UserResponse user) { }

    public record UserResponse(String username) { }

    @Mapper
    interface UserMapper {
        UserResponse map(User source);
    }
}
