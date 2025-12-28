package com.example.E_commerce.Service.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;  // ✅ MISSING - ADD THIS
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Component  // ✅ Spring manages this bean
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // ✅ School's secret stamp key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 🪪 CREATE ID CARD (Login → Token)
    public String generateToken(String email) {
        return Jwts.builder()                           // 🛠️ Plastic card maker
                .setSubject(email)                       // 📸 Your photo (email)
                .setIssuedAt(new Date())                 // 📅 Issue date (today)
                .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000))  // ⏰ Expire in 24h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)  // 🖍️ School PRINCIPAL stamp
                .compact();                              // 📦 Pack into 1 line
    }

    // 👤 EXTRACT NAME FROM ID CARD (Safe version)
    public String extractEmail(String token) {
        try {
            return extractAllClaims(token).getSubject();     // 📖 Read name from card
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);  // 🚫 Fake/expired card
        }
    }


    // 🔍 OPEN & READ ID CARD (Guard's full check)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()                      // 🔓 Guard's tool
                .setSigningKey(getSignKey())             // 🖍️ Check PRINCIPAL stamp
                .build()                                 // ✅ Ready to verify
                .parseClaimsJws(token)                   // 📖 Open + Check stamp + Check expiry
                .getBody();                              // 📄 All info inside
    }
}
