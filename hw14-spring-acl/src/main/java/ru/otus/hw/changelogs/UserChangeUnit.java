package ru.otus.hw.changelogs;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.hw.domain.User;
import ru.otus.hw.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@ChangeUnit(id = "userChangeUnitId", order = "005", runAlways = true)
public class UserChangeUnit {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Execution
    public void initUsers() {
        var password = passwordEncoder.encode("admin");
        var user = new User(
            "1",
            Set.of(new SimpleGrantedAuthority("ADMIN")),
            "admin",
            password
        );
        userRepository.insert(user);
    }

    @RollbackExecution
    public void rollback() {
        userRepository.deleteAll();
    }
}
