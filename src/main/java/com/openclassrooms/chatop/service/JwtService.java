package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.repository.CredentialRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final CredentialService credentialService;

    @Autowired
    public JwtService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    /**
     * Récupère la clé secrète depuis la base de données (table CREDENTIALS).
     * @return la clé secrète utilisée pour signer le token.
     */
    private SecretKey getSigningKey() {
        String secretKeyFromDb = credentialService.getValueByKey("jwt.secret");
        if (secretKeyFromDb == null || secretKeyFromDb.isEmpty()) {
            throw new IllegalStateException("La clé secrète JWT n'est pas définie dans la base de données.");
        }
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyFromDb);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un token JWT pour un utilisateur donné en fonction de son email.
     * @param email l'email de l'utilisateur pour lequel générer le token.
     * @return le token JWT généré.
     */
    public String generateToken(String email) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(email) // Utilise l'email de l'utilisateur
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    

    /**
     * Valide un token JWT en vérifiant son authenticité et sa date d'expiration.
     * @param token le token JWT à valider.
     * @param email l'email de l'utilisateur pour vérifier si le token correspond.
     * @return true si le token est valide, false sinon.
     */
    public boolean validateToken(String token, String email) {
        SecretKey key = getSigningKey();
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Token invalide : " + e.getMessage());
            return false;
        }

        // Vérifier que le sujet (email) correspond et que le token n'a pas expiré
        return claims.getSubject().equals(email) && !claims.getExpiration().before(new Date());
    }

    /**
     * Extrait l'email du sujet du token JWT.
     * @param token le token JWT duquel extraire l'email.
     * @return l'email contenu dans le token.
     */
    public String extractEmailFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();  // Retourne l'email qui est stocké dans le champ 'subject' du JWT
    }

    /**
     * Extrait les informations (claims) du token JWT.
     * @param token le token JWT duquel extraire les claims.
     * @return les claims du token.
     */
    private Claims extractClaims(String token) {
        SecretKey key = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie la validité du token JWT en s'assurant qu'il n'est pas expiré et qu'il est bien signé.
     * @param token le token JWT à vérifier.
     * @return true si le token est valide, false sinon.
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expiré");
        } catch (JwtException e) {
            System.out.println("Token invalide");
        }
        return false;  // Le token est invalide
    }
}
