package ru.otus.hw.services;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    public AclServiceWrapperServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    public void createPermissions(Object object, Permission ...permissions) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var owner = new PrincipalSid(authentication);
        var objectId = new ObjectIdentityImpl(object);
        var admin = new GrantedAuthoritySid("ROLE_ADMIN");

        var acl = mutableAclService.createAcl(objectId);

        for (var permission : permissions) {
            acl.insertAce(acl.getEntries().size(), permission, owner, true);
            acl.insertAce(acl.getEntries().size(), permission, admin, true);
        }

        mutableAclService.updateAcl(acl);
    }
}
