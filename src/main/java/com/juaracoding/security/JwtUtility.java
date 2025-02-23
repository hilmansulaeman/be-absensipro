package com.juaracoding.security;

import com.juaracoding.config.JwtConfig;
import com.juaracoding.model.Userz;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static javax.crypto.Cipher.SECRET_KEY;

@Component
public class JwtUtility implements Serializable {
    private static final long serialVersionUID = 234234523523L;
    public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60;//untuk t menit 5 * 60 * 1000

    /**
     * Function disini hanya menerima token JWT yang sudah di decrypt
     * Yang dapat di claims disini adalah key yang diinput dari api Login
     */
    public Map<String, Object> mappingBodyToken(String token,
                                                Map<String, Object> mapz) {
        /** claims adalah data payload yang ada di token
         * PASTIKAN YANG DIISI SAAT PROSES LOGIN SAMA SAAT PROSES CLAIMS
         */
        Claims claims = getAllClaimsFromToken(token);
        mapz.put("userId", claims.get("uid"));
        mapz.put("email", claims.get("ml"));//untuk email
        mapz.put("namaLengkap", claims.get("nl"));
        mapz.put("noHp", claims.get("pn"));

        return mapz;
    }

    /**
     * KONFIGURASI CUSTOMISASI BERAKHIR DISINI
     * KONFIGURASI UNTUK JWT DIMULAI DARI SINI
     */
//    username dari token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //parameter token habis waktu nya
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //kita dapat mengambil informasi dari token dengan menggunakan secret key
    //disini juga validasi dari expired token dan lihat signature  dilakukan
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JwtConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token untuk user
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        claims = (claims == null) ? new HashMap<String, Object>() : claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * proses yang dilakukan saat membuat token adalah :
     * mendefinisikan claim token seperti penerbit (Issuer) , waktu expired , subject dan ID
     * generate signature dengan menggunakan secret key dan algoritma HS512 (HMAC - SHA),
     */

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long timeMilis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(timeMilis))
                .setExpiration(new Date(timeMilis + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, JwtConfig.getJwtSecret()).compact();
    }

    public Boolean validateToken(String token) {
        /** Sudah otomatis tervalidaasi jika expired date masih aktif */
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }

    public String generateToken(String username) {
        return username;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000)) // Token validity (5 hours)
                .signWith(SignatureAlgorithm.HS512, String.valueOf(SECRET_KEY))
                .compact();
        /**
         * KONFIGURASI UNTUK JWT BERAKHIR DI SINI
         */

    }
    public String generateToken(Userz user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getIdUser());
        claims.put("ml", user.getEmail());
        claims.put("nl", user.getNamaLengkap());
        return doGenerateToken(claims, user.getEmail());
    }

    public String generateTokenForPasswordReset(Userz user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getIdUser());
        claims.put("ml", user.getEmail());
        claims.put("reset", true); // Custom claim to identify reset token
        return doGenerateToken(claims, user.getEmail());
    }
    public Boolean validatePasswordResetToken(String token, String email) {
        final String tokenEmail = getUsernameFromToken(token);
        final Boolean isResetToken = getClaimFromToken(token, claims -> claims.get("reset") != null && (boolean) claims.get("reset"));

        return (tokenEmail.equals(email) && !isTokenExpired(token) && isResetToken);
    }
}