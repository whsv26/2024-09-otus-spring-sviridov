package ru.otus.hw.config;

import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
public class AclConfig {

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public AclCache aclCache() {
        var cache = new ConcurrentMapCache("acl");
        return new SpringCacheBasedAclCache(cache, permissionGrantingStrategy(), aclAuthorizationStrategy());
    }

    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource) {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }

    @Bean
    public JdbcMutableAclService aclService(DataSource dataSource) {
        return new JdbcMutableAclService(dataSource, lookupStrategy(dataSource), aclCache());
    }

    @Bean
    protected MethodSecurityExpressionHandler createExpressionHandler(AclService aclService) {
        var expressionHandler = new DefaultMethodSecurityExpressionHandler();
        // Allow hasPermission() checks
        expressionHandler.setPermissionEvaluator(
            new AclPermissionEvaluator(aclService)
        );
        return expressionHandler;
    }
}
