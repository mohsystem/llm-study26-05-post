package com.um.springbootprojstructure.config;

import com.um.springbootprojstructure.repository.UserRepository;
import com.um.springbootprojstructure.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = auth.substring("Bearer ".length());

        try {
            Jws<Claims> jws = jwtService.parse(token);
            if (!jwtService.isAccessToken(jws)) {
                filterChain.doFilter(request, response);
                return;
            }

            String publicRef = jws.getPayload().getSubject();

            var userOpt = userRepository.findByPublicRef(publicRef);
            if (userOpt.isEmpty() || !userOpt.get().isActive()) {
                filterChain.doFilter(request, response);
                return;
            }

            List<String> roles = jws.getPayload().get("roles", List.class);
            var authorities = roles == null ? List.<SimpleGrantedAuthority>of()
                    : roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList();

            var authentication = new UsernamePasswordAuthenticationToken(publicRef, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            // invalid token -> proceed unauthenticated
        }

        filterChain.doFilter(request, response);
    }
}
