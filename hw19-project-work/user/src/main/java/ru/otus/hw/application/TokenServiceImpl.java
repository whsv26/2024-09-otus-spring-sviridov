package ru.otus.hw.application;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Token;
import ru.otus.hw.domain.PublicTokens;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManager;

    private final AuthConfig authConfig;

    private final KeyPair keyPair;

    @Override
    public PublicTokens listPublicTokens() {
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var rsaKey = new RSAKey.Builder(publicKey)
            .keyID(authConfig.getKeyId())
            .build();
        return new PublicTokens(new JWKSet(rsaKey).toJSONObject());
    }

    @Override
    public Token createToken(String username, String password) {
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
        return new Token(token);
    }

    private static String buildAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(s -> s.startsWith("ROLE_"))
            .map(s -> s.substring("ROLE_".length()))
            .collect(Collectors.joining(" "));
    }
}
