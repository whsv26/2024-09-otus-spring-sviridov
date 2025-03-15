package me.whsv26.user.application;

import me.whsv26.user.domain.PublicTokens;
import me.whsv26.user.domain.Token;

public interface TokenService {

    PublicTokens listPublicTokens();

    Token createToken(String username, String password);
}
