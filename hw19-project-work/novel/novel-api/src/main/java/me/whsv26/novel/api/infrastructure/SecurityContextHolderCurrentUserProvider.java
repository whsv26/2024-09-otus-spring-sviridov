package me.whsv26.novel.api.infrastructure;

import me.whsv26.novel.api.application.CurrentUser;
import me.whsv26.novel.api.application.CurrentUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityContextHolderCurrentUserProvider implements CurrentUserProvider {

    @Override
    public CurrentUser getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .map(userId -> new CurrentUser((String) userId))
            .orElseThrow(() -> new AccessDeniedException("Current user is not authenticated"));
    }
}
