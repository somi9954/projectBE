package org.project.configs.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;


import java.security.Key;

public class TokenProvider {
    private final String secret;

    private final long TokenValidityInSeconds;

    private Key key;

    public TokenProvider(String secret, Long tokenValidityInSeconds) { //HAAC -> SHA512 + Message
        this.secret = secret;
        this.TokenValidityInSeconds = tokenValidityInSeconds;

        byte[] bytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Authentication authentication) {

        return null;
    }
}
