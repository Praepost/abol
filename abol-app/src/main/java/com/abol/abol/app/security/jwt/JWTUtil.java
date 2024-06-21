package com.abol.abol.app.security.jwt;

import com.abol.abol.app.models.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY ;

    @Value("${jwt.session.access.time}")
    private long accessTime ;

    @Value("${jwt.session.refresh.time}")
    private long refreshTime;

    // генерация токена (кладем в него имя пользователя и authorities)
    public String generateAccessToken(Person person) {
        Map<String, Object> claims = new HashMap<>();
        String commaSeparatedListOfAuthorities = String.valueOf(person.getRoles());
        claims.put("authorities", commaSeparatedListOfAuthorities);
        claims.put("exp", expireAccessTimeFromNow());

        return createToken(claims, person.getUsername());
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        UUID uuid = UUID.randomUUID();

        claims.put("uuid", uuid);
        claims.put("exp", expireRefreshTimeFromNow());


        return createToken(claims, username);
    }

    //извлечение имени пользователя из токена (внутри валидация токена)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //извлечение authorities (внутри валидация токена)
    public String extractAuthorities(String token) {
        Function<Claims, String> claimsListFunction = claims -> (String) claims.get("authorities");

        return extractClaim(token, claimsListFunction);
    }

    public String extractUuid(String token) {
        Function<Claims, String> claimsListFunction = claims -> (String) claims.get("uuid");

        return extractClaim(token, claimsListFunction);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }


    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private Date expireAccessTimeFromNow() {
        return new Date(System.currentTimeMillis() + accessTime);
    }

    private Date expireRefreshTimeFromNow() {
        return new Date(System.currentTimeMillis() + refreshTime);
    }

//    private Boolean jwtVerify(String token) throws Exception {
//        JwtParser jwtParser = Jwts.parserBuilder().
//                .verifyWith(SECRET_KEY)
//                .build();
//        try {
//            jwtParser.parse(token);
//        } catch (Exception e) {
//            throw new Exception("Could not verify JWT token integrity!", e);
//        }
//    }
}