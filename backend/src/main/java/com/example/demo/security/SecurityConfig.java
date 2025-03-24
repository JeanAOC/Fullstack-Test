package com.example.demo.security;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Import(PasswordEncoderConfig.class)
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(@Lazy JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso sin autenticación
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/posts/public").permitAll()

                // Configuración para posts
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll() // Permitir acceso público a la lectura de posts
                .requestMatchers(HttpMethod.POST, "/api/posts").hasRole("USER") // Solo USER puede crear posts
                .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasAnyRole("USER", "ADMIN") // USER y ADMIN pueden editar posts
                .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyRole("USER", "ADMIN") // USER y ADMIN pueden eliminar posts

                // Configuración para usuarios (solo ADMIN)
                .requestMatchers(HttpMethod.GET, "/api/admin/users/**").hasRole("ADMIN") // Solo ADMIN puede listar usuarios
                .requestMatchers(HttpMethod.POST, "/api/admin/users").hasRole("ADMIN") // Solo ADMIN puede crear usuarios
                .requestMatchers(HttpMethod.PUT, "/api/admin/users/**").hasRole("ADMIN") // Solo ADMIN puede editar usuarios
                .requestMatchers(HttpMethod.DELETE, "/api/admin/users/**").hasRole("ADMIN") // Solo ADMIN puede eliminar usuarios
                .requestMatchers(
            "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**"
                    ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
        CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}