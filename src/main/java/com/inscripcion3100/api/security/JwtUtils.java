package com.inscripcion3100.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component
public class JwtUtils {
    //para registrar errores y eventos en registro y loggin
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${api-inscripcion.secret.jwt}")
    private String jwtSecret;

    @Value("${api-inscripcion.jwt.expiration}")
    private long jwtExpirationMs;

    public String generateJwtToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        //se genera el JWT
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))    //guarda email de usuario
                .setIssuedAt(new Date())                    //guarda fecha de creacion
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))  //fecha de expiracion
                .signWith(key(), SignatureAlgorithm.HS256)  //firma digital
                .compact();     //compacta en un string
    }

    //decodifica la clave sevreta para usar en firmas
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //obtiene el username desde un token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    //valida si un token es valido o no expiro
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("El string de claims de JWT está vacío: {}", e.getMessage());
        }
        return false;
    }
}
