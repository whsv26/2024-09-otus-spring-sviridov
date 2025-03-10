package ru.otus.hw.application;

import ru.otus.hw.domain.Token;
import ru.otus.hw.domain.PublicTokens;

public interface TokenService {

    PublicTokens listPublicTokens();

    Token createToken(String username, String password);
}
