package ru.otus.hw.services;

import org.springframework.security.acls.model.Permission;

public interface AclServiceWrapperService {
    void createPermissions(Object object, Permission ...permissions);
}
