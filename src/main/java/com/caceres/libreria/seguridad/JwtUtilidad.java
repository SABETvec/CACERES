package com.caceres.libreria.seguridad;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtilidad {

    @Value("${aplicacion.jwt.secreto}")
    private String secreto;

    @Value("${aplicacion.jwt.tiempoExpiracion}")
    private long tiempoExpiracion;

    private Key obtenerClaveSecreta() {
        return Keys.hmacShaKeyFor(secreto.getBytes());
    }

    public String generarToken(String nombreUsuario, Map<String, Object> datosAdicionales) {
        return Jwts.builder()
                .setClaims(datosAdicionales)
                .setSubject(nombreUsuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(obtenerClaveSecreta(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerNombreUsuario(String tokenJwt) {
        return Jwts.parserBuilder()
                .setSigningKey(obtenerClaveSecreta())
                .build()
                .parseClaimsJws(tokenJwt)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String tokenJwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(obtenerClaveSecreta())
                    .build()
                    .parseClaimsJws(tokenJwt);
            return true;
        } catch (JwtException excepcionJwt) {
            return false;
        }
    }
}