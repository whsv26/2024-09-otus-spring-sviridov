package me.whsv26.user.application;

import me.whsv26.user.application.dto.PublicTokens;
import me.whsv26.user.application.dto.Token;

public interface TokenService {

    PublicTokens listPublicTokens();

    Token createToken(String username, String password);
}
