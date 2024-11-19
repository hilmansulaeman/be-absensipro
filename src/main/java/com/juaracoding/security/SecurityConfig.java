package com.juaracoding.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security settings...");

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // URL frontend Anda
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    return config;
                }))
                .authorizeRequests(auth -> auth
                        // Izin khusus untuk endpoint di AuthController yang terbuka untuk umum
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login", "/auth/forgot-password").permitAll()

                        // Izin khusus untuk semua endpoint di AdminController (membutuhkan autentikasi)
                        .requestMatchers("/admin/**").authenticated()

                        // Contoh endpoint lain di aplikasi yang bisa diakses publik atau butuh autentikasi
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/akses/**").permitAll()
                        .requestMatchers("/api/divisi/**").permitAll()
                        .requestMatchers("/api/karyawan/**").permitAll()
                        .requestMatchers("/api/employee/**").permitAll()
                        .requestMatchers("/api/absen/save").authenticated()
                        .requestMatchers("/api/absen/all").authenticated()
                        .requestMatchers("/api/absen/report/**").authenticated()
                        .anyRequest().authenticated() // Semua permintaan lainnya membutuhkan autentikasi
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // Mengaktifkan JWT support

        logger.info("Security settings configured successfully for Absen API.");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Menggunakan NimbusJwtDecoder dengan kunci rahasia
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")).build();
    }
}
