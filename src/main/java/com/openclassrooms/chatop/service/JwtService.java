package com.openclassrooms.chatop.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Générer la clé de signature
    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Générer un token JWT pour un utilisateur donné (par son email)
    public String generateToken(String email) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(email)  // Le sujet du token (ici, l'email de l'utilisateur)
                .setIssuedAt(new Date())  // Date d'émission du token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration dans 10 heures
                .signWith(key, SignatureAlgorithm.HS256)  // Utilisation de la clé de signature et de l'algorithme HS256
                .compact();
    }

    // Valider le token JWT
    public boolean validateToken(String token, String email) {
        SecretKey key = getSigningKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Vérifier que le sujet (email) correspond et que le token n'a pas expiré
        return claims.getSubject().equals(email) && !claims.getExpiration().before(new Date());
    }

    // Extraire l'email du sujet du token JWT
    public String extractEmailFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();  // Retourne l'email qui est stocké dans le champ 'subject' du JWT
    }

    // Extraire les informations du token (les claims)
    private Claims extractClaims(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Méthode pour vérifier la validité du token
    public boolean isTokenValid(String token) {
        try {
            // Si le token est expiré ou incorrect, une exception sera levée
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);  // Utilisation du parserBuilder() ici
            return true; // Le token est valide
        } catch (ExpiredJwtException e) {
            System.out.println("Token expiré");
        } catch (JwtException e) {
            System.out.println("Token invalide");
        }
        return false; // Le token est invalide
    }
}
