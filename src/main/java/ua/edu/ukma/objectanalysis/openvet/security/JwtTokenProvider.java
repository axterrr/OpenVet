package ua.edu.ukma.objectanalysis.openvet.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final JwtProperties properties;
    private final Algorithm algorithm;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.algorithm = Algorithm.HMAC512(properties.getSecret());
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        List<String> authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getExpiration());

        return JWT.create()
                .withSubject(username)
                .withClaim("roles", authorities)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return JWT.require(algorithm)
            .build()
            .verify(token);
    }
}

