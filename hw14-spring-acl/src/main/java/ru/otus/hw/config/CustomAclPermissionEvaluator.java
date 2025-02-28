package ru.otus.hw.config;

import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class CustomAclPermissionEvaluator extends AclPermissionEvaluator {

    public CustomAclPermissionEvaluator(AclService aclService) {
        super(aclService);
    }

    @Override
    public boolean hasPermission(
        Authentication authentication,
        Object domainObject,
        Object permission
    ) {
        if (isAdmin(authentication)) {
            return true;
        }
        return super.hasPermission(authentication, domainObject, permission);
    }

    @Override
    public boolean hasPermission(
        Authentication authentication,
        Serializable targetId,
        String targetType,
        Object permission
    ) {
        if (isAdmin(authentication)) {
            return true;
        }
        return super.hasPermission(authentication, targetId, targetType, permission);
    }

    private static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
