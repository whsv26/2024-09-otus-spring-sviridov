package ru.otus.hw.application;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.domain.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
