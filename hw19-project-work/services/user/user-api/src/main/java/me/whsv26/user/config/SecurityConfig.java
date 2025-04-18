package me.whsv26.user.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import me.whsv26.libs.auth.CurrentUserProvider;
import me.whsv26.libs.auth.PreAuthenticationFilter;
import me.whsv26.libs.auth.SecurityContextHolderCurrentUserProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/actuator/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder(KeyPair keyPair, AuthConfig config) {
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();
        var rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(config.getKeyId())
            .build();
        var jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public KeyPair keyPair(AuthConfig config) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var privateKeyBytes = Base64.getDecoder().decode(config.getPrivateKey());
        var publicKeyBytes = Base64.getDecoder().decode(config.getPublicKey());
        var keyFactory = KeyFactory.getInstance("RSA");
        var privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        var privateKey = keyFactory.generatePrivate(privateKeySpec);
        var publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        var publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    CurrentUserProvider currentUserProvider() {
        return new SecurityContextHolderCurrentUserProvider();
    }

    @Bean
    FilterRegistrationBean<PreAuthenticationFilter> preAuthenticationFilterRegistration() {
        var filter = new PreAuthenticationFilter();
        var registration = new FilterRegistrationBean<PreAuthenticationFilter>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }
}
