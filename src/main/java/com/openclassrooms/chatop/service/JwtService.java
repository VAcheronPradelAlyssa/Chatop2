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

/**
 * Service pour la gestion des tokens JWT, incluant la génération, la validation et l'extraction d'informations.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey; // Clé secrète utilisée pour signer le token JWT

    /**
     * Génère une clé de signature secrète à partir de la clé secrète configurée.
     *
     * @return la clé secrète utilisée pour signer le JWT
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey); // Décodage de la clé secrète
        return Keys.hmacShaKeyFor(keyBytes); // Création de la clé de signature
    }

    /**
     * Génère un token JWT pour un utilisateur donné en fonction de son email.
     * Le token inclut des informations telles que l'email, la date d'émission et une date d'expiration.
     *
     * @param email l'email de l'utilisateur pour lequel générer le token
     * @return le token JWT généré
     */
    public String generateToken(String email) {
        SecretKey key = getSigningKey(); // Obtention de la clé secrète pour la signature du token
        return Jwts.builder()
                .setSubject(email)  // Le sujet du token (ici, l'email de l'utilisateur)
                .setIssuedAt(new Date())  // Date d'émission du token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expiration dans 10 heures
                .signWith(key, SignatureAlgorithm.HS256)  // Utilisation de la clé de signature et de l'algorithme HS256
                .compact();  // Construction du token
    }

    /**
     * Valide un token JWT en vérifiant son authenticité et sa date d'expiration.
     *
     * @param token le token JWT à valider
     * @param email l'email de l'utilisateur pour vérifier si le token correspond
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token, String email) {
        SecretKey key = getSigningKey(); // Obtention de la clé secrète pour la signature du token
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) // Parse le token JWT pour extraire les claims
                .getBody();

        // Vérifier que le sujet (email) correspond et que le token n'a pas expiré
        return claims.getSubject().equals(email) && !claims.getExpiration().before(new Date());
    }

    /**
     * Extrait l'email du sujet du token JWT.
     *
     * @param token le token JWT duquel extraire l'email
     * @return l'email contenu dans le token
     */
    public String extractEmailFromToken(String token) {
        Claims claims = extractClaims(token); // Extraction des claims du token
        return claims.getSubject();  // Retourne l'email qui est stocké dans le champ 'subject' du JWT
    }

    /**
     * Extrait les informations (claims) du token JWT.
     *
     * @param token le token JWT duquel extraire les claims
     * @return les claims du token
     */
    private Claims extractClaims(String token) {
        SecretKey key = getSigningKey(); // Obtention de la clé secrète pour la signature du token
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token) // Parse le token JWT pour extraire les claims
                .getBody();
    }

    /**
     * Vérifie la validité du token JWT en s'assurant qu'il n'est pas expiré et qu'il est bien signé.
     *
     * @param token le token JWT à vérifier
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token) {
        try {
            // Si le token est expiré ou incorrect, une exception sera levée
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);  // Utilisation du parserBuilder() pour analyser le JWT
            return true; // Le token est valide
        } catch (ExpiredJwtException e) {
            System.out.println("Token expiré");
        } catch (JwtException e) {
            System.out.println("Token invalide");
        }
        return false; // Le token est invalide
    }
}
