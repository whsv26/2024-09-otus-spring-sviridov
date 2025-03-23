package me.whsv26.user.service;

import me.whsv26.user.dto.PublicTokens;
import me.whsv26.user.dto.Token;

public interface TokenService {

    PublicTokens listPublicTokens();

    Token createToken(String username, String password);
}
