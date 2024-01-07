package org.project.configs.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.project.commons.Utils;
import org.project.commons.exceptions.BadRequestExeption;
import org.project.models.member.MemberInfo;
import org.project.models.member.MemberInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenProvider {
    private final String secret;

    private final long TokenValidityInSeconds;

    @Autowired
    private MemberInfoService infoService;

    private Key key;

    public TokenProvider(String secret, Long tokenValidityInSeconds) { //HAAC -> SHA512 + Message
        this.secret = secret;
        this.TokenValidityInSeconds = tokenValidityInSeconds;

        byte[] bytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Authentication authentication) {
        String authories = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Date expires = new Date((new Date()).getTime() + TokenValidityInSeconds * 1000 ); // 한시간 뒤에 토큰 만료 시간 설정

        return Jwts.builder()
                .setSubject(authentication.getName())//아이디
                .claim("auth", authories) // 권한
                .signWith(key, SignatureAlgorithm.HS512) // HMAC + SHA512
                .setExpiration(expires) // 토큰 유효시간
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        String email = claims.getSubject();
        MemberInfo userDetails = (MemberInfo) infoService.loadUserByUsername(email);

        String auth = claims.get("auth").toString();
        List<? extends GrantedAuthority> authorities = Arrays.stream(auth.split(","))
                .map(SimpleGrantedAuthority::new).toList();
        userDetails.setAuthorities(authorities);


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);

        return authentication;
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();


        } catch (ExpiredJwtException e) {
            throw new BadRequestExeption(Utils.getMessage("EXPIRED.JWR_TOKEN", "validation"));
        }catch (UnsupportedJwtException e) {
            throw new BadRequestExeption(Utils.getMessage("UNSUPPORTED.JWT_TOKEN", "validation"));
        }catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new BadRequestExeption(Utils.getMessage("INVALID_FORMAT.JWT.TOKEN", "validation"));
        }
    }
}
