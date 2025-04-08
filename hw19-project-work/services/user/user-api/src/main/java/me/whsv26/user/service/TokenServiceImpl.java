package me.whsv26.user.service;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import me.whsv26.user.config.AuthConfig;
import me.whsv26.user.config.CacheConfig;
import me.whsv26.user.dto.PublicTokens;
import me.whsv26.user.dto.Token;
import me.whsv26.user.model.RichUserDetails;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder jwtEncoder;

    private final AuthenticationManager authenticationManager;

    private final AuthConfig authConfig;

    private final KeyPair keyPair;

    @Cacheable(CacheConfig.CACHE_PUBLIC_TOKENS)
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
        var authorities = buildAuthoritiesClaim(authentication);
        var claimsBuilder = JwtClaimsSet.builder()
            .issuer(authConfig.getTokenIssuer().toString())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .subject(username)
            .claim("authorities", authorities);

        buildUserIdClaim(claimsBuilder, authentication);

        var encoderParameters = JwtEncoderParameters.from(claimsBuilder.build());
        var token = jwtEncoder.encode(encoderParameters).getTokenValue();
        return new Token(token);
    }

    private static void buildUserIdClaim(
        JwtClaimsSet.Builder claimsBuilder,
        Authentication authentication
    ) {
        Optional.ofNullable(authentication.getPrincipal())
            .filter(p -> p instanceof RichUserDetails)
            .map(p -> (RichUserDetails) p)
            .map(RichUserDetails::getUserId)
            .map(UUID::toString)
            .ifPresent(userId -> claimsBuilder.claim("user_id", userId));
    }

    private static String buildAuthoritiesClaim(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(s -> s.startsWith("ROLE_"))
            .map(s -> s.substring("ROLE_".length()))
            .collect(Collectors.joining(" "));
    }
}
