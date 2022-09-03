package com.blackboxdynamics.example.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.blackboxdynamics.example.config.JwtConfig;
import com.blackboxdynamics.example.models.AuthRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;

    private final JwtConfig config;

    public JwtUsernamePasswordAuthFilter(AuthenticationManager authManager,
                                         JwtConfig config) {
        this.authManager = authManager;
        this.config = config;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthRequestModel authRequestModel = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthRequestModel.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    authRequestModel.getUsername(),
                    authRequestModel.getPassword()
            );

            Authentication authentication = authManager.authenticate(auth);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(config.getSecret());
            String token = JWT.create()
                    .withSubject(authResult.getName())
                    .withClaim("authorities",
                             authResult.getAuthorities()
                            .stream()
                            .map(s-> s.getAuthority())
                            .collect(Collectors.toList()))
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * config.getExpirationDays()))
                    .withIssuer(config.getIssuer())
                    .sign(algorithm);
            response.addHeader("Authorization", config.getTokenPrefix() + token);
        } catch (JWTCreationException e){
            throw new RuntimeException(e);
        }
    }
}
