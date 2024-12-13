package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.service.JwtTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnExpression("${api-security.enabled:true}")
public class JwtAuthManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final AuthenticationManager authManager;

    public JwtAuthManagerResolver(JwtTokenService jwtTokenService) {

        TokenClaimsConverter converter = new TokenClaimsConverter();
        JwtDecoder decoder = NimbusJwtDecoder.withPublicKey(jwtTokenService.getRSAPublicKey()).build();

        this.authManager = createAuthManager(decoder, converter);

    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        return this.authManager;
    }

    private AuthenticationManager createAuthManager(JwtDecoder decoder, Converter<Jwt, AbstractAuthenticationToken> converter) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(decoder);
        authenticationProvider.setJwtAuthenticationConverter(converter);
        return authenticationProvider::authenticate;
    }

    private static class TokenClaimsConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final Converter<Jwt, AbstractAuthenticationToken> converter = new JwtAuthenticationConverter();

        @Override
        public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
            Map<String, Object> claims = new LinkedHashMap<>(jwt.getClaims());
            Jwt newToken = new Jwt(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), claims);
            return this.converter.convert(newToken);
        }
    }

}
