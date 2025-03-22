package me.whsv26.user.application;

import lombok.RequiredArgsConstructor;
import me.whsv26.user.domain.entity.User;
import me.whsv26.user.domain.exception.UserAlreadyExistsException;
import me.whsv26.user.domain.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
            var savedUser = userRepository.save(user);
            userRepository.flush();
            return savedUser;
        } catch (DataIntegrityViolationException cause) {
            throw new UserAlreadyExistsException(user.getUsername(), cause);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
