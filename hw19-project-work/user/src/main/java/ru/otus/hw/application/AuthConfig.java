package ru.otus.hw.application;

import java.net.URL;

public interface AuthConfig {

    int getTokenTtl();

    URL getTokenIssuer();

    String getKeyId();

    String getPublicKey();

    String getPrivateKey();
}
