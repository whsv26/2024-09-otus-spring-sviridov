package me.whsv26.user.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
public class RichUserDetails implements UserDetails {

    private final UserDetails underlying;

    @Getter
    private final UUID userId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return underlying.getAuthorities();
    }

    @Override
    public String getPassword() {
        return underlying.getPassword();
    }

    @Override
    public String getUsername() {
        return underlying.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return underlying.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return underlying.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return underlying.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return underlying.isEnabled();
    }
}