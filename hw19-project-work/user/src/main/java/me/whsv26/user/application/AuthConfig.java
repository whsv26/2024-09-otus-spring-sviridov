package me.whsv26.user.application;

import java.net.URL;

public interface AuthConfig {

    int getTokenTtl();

    URL getTokenIssuer();

    String getKeyId();

    String getPublicKey();

    String getPrivateKey();
}
