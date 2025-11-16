package com.bookit.application.security.config;


import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.types.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


public class SecurityConfigurationBase {
    private final String allowedOrigin;
    public SecurityConfigurationBase(SecurityConfigProperties securityConfigProperties){
        this.allowedOrigin = securityConfigProperties.getAllowedOrigin();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(this.allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public HttpSecurity createFilters(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers(HttpMethod.GET, "/loginstatus").authenticated()

                                .requestMatchers(HttpMethod.GET, "/movies/ongoing", "/movies/upcoming", "/movies/filter").permitAll()
                                .requestMatchers(HttpMethod.GET, "/movies").permitAll()
                                .requestMatchers(HttpMethod.GET, "/movie/*/shows").permitAll()
                                .requestMatchers(HttpMethod.GET, "/movie/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/movie").hasAuthority(Role.ADMIN.code())

                                .requestMatchers(HttpMethod.POST, "/show").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.PATCH, "/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.POST, "/theatre").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.GET, "/theatre", "/theatre/\\d+/shows").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.PATCH, "/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.GET, "/tickets").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/tickets").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.PATCH, "/tickets/book", "/tickets/cancel").hasAuthority(Role.REGULAR_USER.code())
                                .requestMatchers(HttpMethod.POST, "/tickets").hasAuthority(Role.ADMIN.code())
                                .anyRequest().authenticated()

                );
        return http;
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}
