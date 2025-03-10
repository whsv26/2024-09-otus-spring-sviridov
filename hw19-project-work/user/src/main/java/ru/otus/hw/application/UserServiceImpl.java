package ru.otus.hw.application;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.domain.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User register(String username, String password) {
        var passwordHash = passwordEncoder.encode(password);
        var user = new User(username, passwordHash);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException cause) {
            throw new UserAlreadyExistsException(user.getUsername(), cause);
        }
    }
}
