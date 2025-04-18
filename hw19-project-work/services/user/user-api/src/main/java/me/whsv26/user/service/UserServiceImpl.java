package me.whsv26.user.service;

import lombok.RequiredArgsConstructor;
import me.whsv26.user.config.CacheConfig;
import me.whsv26.user.repository.UserRepository;
import me.whsv26.user.model.User;
import me.whsv26.user.model.exception.UserAlreadyExistsException;
import me.whsv26.user.model.exception.UserNotFoundException;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(CacheConfig.CACHE_USERS)
    @Transactional(readOnly = true)
    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
