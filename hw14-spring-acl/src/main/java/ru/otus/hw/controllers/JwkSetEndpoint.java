package ru.otus.hw.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwkSetEndpoint {

    private final KeyPair keyPair;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getKey() {
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var rsaKey = new RSAKey.Builder(publicKey)
            .keyID("token-key")
            .build();
        return new JWKSet(rsaKey).toJSONObject();
    }
}
