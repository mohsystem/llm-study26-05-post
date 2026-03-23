package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.JwtProperties;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user, String sessionRef) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getAccessTokenTtlSeconds());

        Set<String> roles = user.getRoles() == null ? Set.of() :
                user.getRoles().stream().map(Role::name).collect(Collectors.toSet());

        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(user.getPublicRef())
                .claim("typ", "access")
                .claim("sid", sessionRef)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(User user, String sessionRef) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.getRefreshTokenTtlSeconds());

        return Jwts.builder()
                .issuer(props.getIssuer())
                .subject(user.getPublicRef())
                .claim("typ", "refresh")
                .claim("sid", sessionRef)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(props.getIssuer())
                .build()
                .parseSignedClaims(token);
    }

    public boolean isRefreshToken(Jws<Claims> jws) {
        return "refresh".equals(jws.getPayload().get("typ", String.class));
    }

    public boolean isAccessToken(Jws<Claims> jws) {
        return "access".equals(jws.getPayload().get("typ", String.class));
    }
}
