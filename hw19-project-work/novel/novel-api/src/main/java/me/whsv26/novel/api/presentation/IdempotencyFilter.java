package me.whsv26.novel.api.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.whsv26.novel.api.infrastructure.RedisProps;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotencyFilter implements Filter {

    public static final String PROCESSING = "processing";

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper mapper;

    private final RedisProps props;

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        var idempotencyKey = Optional.of(httpRequest.getMethod())
            .filter("POST"::equalsIgnoreCase)
            .flatMap(post -> getIdempotencyKey(httpRequest))
            .orElse(null);

        if (idempotencyKey != null) {
            doFilterIdempotent(httpRequest, httpResponse, chain, idempotencyKey);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void doFilterIdempotent(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        String idempotencyKey
    ) throws IOException, ServletException {

        var isNew = redisTemplate.opsForValue()
            .setIfAbsent(idempotencyKey, PROCESSING, props.idempotencyProcessingTtl());

        if (Boolean.FALSE.equals(isNew)) {
            var cachedResponse = redisTemplate.opsForValue().get(idempotencyKey);
            if (cachedResponse != null && !cachedResponse.equals(PROCESSING)) {
                writeCachedResponse(response, cachedResponse);
            } else {
                writeTooManyRequestsResponse(response);
            }
        } else {
            var capturedBody = captureResponseBody(request, response, chain);
            redisTemplate.opsForValue().set(idempotencyKey, capturedBody, props.idempotencyCacheTtl());
            response.getWriter().write(capturedBody);
        }
    }

    private static Optional<String> getIdempotencyKey(HttpServletRequest request) {
        var uri = request.getRequestURI();
        var method = request.getMethod();
        return Optional.ofNullable(request.getHeader("X-Idempotency-Key"))
            .filter(s -> !s.isBlank())
            .map(key -> "idempotency:%s:%s:%s".formatted(method, uri, key));
    }

    private static String captureResponseBody(
        ServletRequest request,
        HttpServletResponse httpResponse,
        FilterChain chain
    ) throws IOException, ServletException {
        var responseWrapper = new IdempotentResponseWrapper(httpResponse);
        chain.doFilter(request, responseWrapper);
        return responseWrapper.getCaptureAsString();
    }

    private static void writeCachedResponse(
        HttpServletResponse httpResponse,
        String cachedResponse
    ) throws IOException {
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write(cachedResponse);
    }

    private void writeTooManyRequestsResponse(HttpServletResponse httpResponse) throws IOException {
        var error = new ErrorInfo(
            "TOO_MANY_REQUESTS",
            "Request is already being processed",
            Collections.emptyMap()
        );
        httpResponse.getWriter().write(mapper.writeValueAsString(error));
        httpResponse.setStatus(429);
    }
}
