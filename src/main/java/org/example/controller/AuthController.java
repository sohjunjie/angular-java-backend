package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.JwtTokenRequest;
import org.example.model.JwtTokenResponse;
import org.example.model.UserProfile;
import org.example.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.example.config.SwaggerConfig.API_VERSION;

@Tag(name = "AUTH API")
@RestController
@RequestMapping("/" + API_VERSION + "/auth")
public class AuthController {

    @Autowired
    JwtTokenService jwtTokenService;

    @Operation(summary = "Retrieve public key")
    @GetMapping(path = "/public-key/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> publicKeyJwk() {
        return this.jwtTokenService.getPublicKeyJwk();
    }

    @Operation(summary = "Get Jwt Token")
    @PostMapping(path = "/jwt/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenResponse jwtToken(JwtTokenRequest request) {
        String token = this.jwtTokenService.generateJwtToken(request.getUsername());
        JwtTokenResponse res = new JwtTokenResponse();
        res.setToken(token);
        return res;
    }

    @Operation(summary = "Get auth token user profile")
    @GetMapping(path = "/jwt/token-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile tokenUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UserProfile.builder()
                .subject(auth.getName())
                .build();
    }

}
