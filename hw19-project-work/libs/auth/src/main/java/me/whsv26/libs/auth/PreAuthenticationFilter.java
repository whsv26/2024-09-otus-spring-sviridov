package me.whsv26.libs.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PreAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        var userId = request.getHeader("X-User-ID");
        var authoritiesHeader = request.getHeader("X-User-Authorities");

        if (userId != null) {
            var authorities = parseAuthorities(authoritiesHeader);
            var authToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private static List<SimpleGrantedAuthority> parseAuthorities(String authoritiesHeader) {
        return Optional.ofNullable(authoritiesHeader)
            .stream()
            .flatMap(authorities -> Arrays.stream(authorities.split(" ")))
            .map(String::trim)
            .filter(authority -> !authority.isEmpty())
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
