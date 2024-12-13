package org.example.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Slf4j
@Configuration
@AutoConfigureBefore(WebSecurityConfig.class)
@ConditionalOnExpression("${api-security.enabled:true}")
public class WebAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authSupplier, RequestAuthorizationContext object) {
        Authentication auth = authSupplier.get();
        boolean authorised = auth.getCredentials() instanceof Jwt;
        return new AuthorizationDecision(authorised);
    }

}
