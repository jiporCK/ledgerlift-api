package com.example.ledgerlift.security;

import com.example.ledgerlift.utils.KeyUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final KeyUtil keyUtil;

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("refreshJwtDecoder")JwtDecoder refreshJwtDecoder) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(refreshJwtDecoder);
        return provider;
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.POST, "api/v1/users/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "api/v1/users/me").hasAnyAuthority("SCOPE_donor:read", "SCOPE_admin:read", "SCOPE_organizer:read")
                            .requestMatchers(HttpMethod.GET, "api/v1/users/**").hasAnyAuthority("SCOPE_admin:read", "SCOPE_admin:write")
                            .requestMatchers("api/v1/donate/**").hasAnyAuthority("SCOPE_donor:read", "SCOPE_donor:write")
                            .requestMatchers(
                                    "/ledgeraiser-api-docs/**",  // OpenAPI docs
                                    "/v3/api-docs/**",          // Default API docs path
                                    "/swagger-ui/**",           // Default Swagger UI assets
                                    "/ledgeraiser-api-ui.html"  // Custom Swagger UI
                            ).permitAll()
                            .requestMatchers(HttpMethod.POST, "api/v1/auth/**").permitAll()
                            .requestMatchers("api/v1/enrollment/**").permitAll()
                            .requestMatchers("api/v1/media/**").permitAll()
                            .requestMatchers("api/v1/organizations/**").permitAll()
                            .requestMatchers("api/v1/events/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "api/v1/media/upload-single").permitAll()
                            .requestMatchers(HttpMethod.POST, "api/v1/media/{eventUuid}/upload-multiple").hasAnyAuthority("SCOPE_organizer:read", "SCOPE_organizer:write")
                            .requestMatchers(HttpMethod.DELETE, "api/v1/media/**").hasAnyAuthority("SCOPE_admin:read", "SCOPE_admin:write")
                            .requestMatchers("media/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"api/v1/categories/**").permitAll()
                            .requestMatchers(HttpMethod.POST,"api/v1/categories/**").permitAll()
                            .anyRequest().authenticated();
                });

        // Security mechanism
        http.oauth2ResourceServer(jwt -> jwt
                .jwt(Customizer.withDefaults()));

        // Disable
        http.csrf(AbstractHttpConfigurer::disable);

        // Change to stateless
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Primary
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet);
    }

    @Primary
    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Primary
    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getAccessTokenPublicKey())
                .build();
    }

    // JWT REFRESH TOKEN =====================================

    @Bean("refreshJwkSource")
    JWKSource<SecurityContext> refreshJwkSource() {
        RSAKey rsaKey = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector
                .select(jwkSet);
    }

    @Bean("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(@Qualifier("refreshJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(keyUtil.getRefreshTokenPublicKey())
                .build();
    }

}
