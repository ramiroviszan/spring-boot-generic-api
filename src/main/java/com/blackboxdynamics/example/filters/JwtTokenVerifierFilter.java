package com.blackboxdynamics.example.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blackboxdynamics.example.config.JwtConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenVerifierFilter extends OncePerRequestFilter {

    private final JwtConfig config;

    public JwtTokenVerifierFilter(JwtConfig config) {
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith(config.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.replace(config.getTokenPrefix(), "");
            Algorithm algorithm = Algorithm.HMAC256(config.getSecret()); //use more secure key
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(config.getIssuer())
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();
            List<String> authorities = jwt.getClaim("authorities").asList(String.class);

            Set<SimpleGrantedAuthority> grantedAuthoritySet = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m))
                    .collect(Collectors.toSet());

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, grantedAuthoritySet);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JWTVerificationException e){
            throw new IllegalStateException("Illegal Auth Token");
        }
        filterChain.doFilter(request, response);
    }
}
