package ru.otus.hw.presentation;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.application.TokenService;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/tokens")
    public Map<String, Object> listTokens() {
        return tokenService.listPublicTokens().value();
    }

    @PostMapping("/tokens")
    public CreateTokenResponse createToken(@RequestBody @Valid CreateTokenRequest request) {
        var token = tokenService.createToken(request.username, request.password);
        return new CreateTokenResponse(token.value());
    }

    public record CreateTokenRequest(
        @NotBlank String username,
        @NotBlank String password
    ) { }

    public record CreateTokenResponse(String token) { }
}
