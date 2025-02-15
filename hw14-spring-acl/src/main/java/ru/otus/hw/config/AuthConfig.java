package ru.otus.hw.config;

import java.net.URL;

public interface AuthConfig {
    int getTokenTtl();
    URL getTokenIssuer();
    String getKeyId();
}
