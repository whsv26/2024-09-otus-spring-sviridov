package ru.otus.hw.controllers;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import ru.otus.hw.config.AuthConfig;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManager;

    private final AuthConfig authConfig;

    private final KeyPair keyPair;

    @GetMapping("/tokens")
    public Map<String, Object> listTokens() {
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var rsaKey = new RSAKey.Builder(publicKey)
            .keyID(authConfig.getTokenId())
            .build();
        return new JWKSet(rsaKey).toJSONObject();
    }

    @PostMapping("/tokens")
    public AuthResponse createToken(@RequestBody @Valid AuthRequest request) {
        var username = request.username;
        var password = request.password;
        var user = new UsernamePasswordAuthenticationToken(username, password);
        var authentication = authenticationManager.authenticate(user);
        var issuedAt = Instant.now();
        var expiresAt = issuedAt.plusSeconds(authConfig.getTokenTtl());
        var authorities = buildAuthorities(authentication);
        var claims = JwtClaimsSet.builder()
            .issuer(authConfig.getTokenIssuer().toString())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .subject(username)
            .claim("authorities", authorities)
            .build();

        var encoderParameters = JwtEncoderParameters.from(claims);
        var token = jwtEncoder.encode(encoderParameters).getTokenValue();
        return new AuthResponse(token);
    }

    private static String buildAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(s -> s.startsWith("ROLE_"))
            .map(s -> s.substring("ROLE_".length()))
            .collect(Collectors.joining(" "));
    }

    public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
    ) { }

    public record AuthResponse(String token) { }
}
