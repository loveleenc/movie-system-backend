package com.bookit.application.security.config;


import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.security.entity.types.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;


public class SecurityConfigurationBase {


    public HttpSecurity createFilters(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) ->
                        authorizationManagerRequestMatcherRegistry
                                //IMPORTANT: THE ORDER OF REQUEST MATCHERS HAS TO BE CORRECT
                                //More specific patterns should be defined before more general ones to ensure proper matching
                                .requestMatchers(HttpMethod.GET, "/api/loginstatus").authenticated()

                                .requestMatchers(HttpMethod.GET, "/api/movies/ongoing", "/api/movies/upcoming", "/api/movies/filter").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/movies").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/movie/*/shows").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/movie/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/movie").hasAuthority(Role.ADMIN.code())

                                .requestMatchers(HttpMethod.POST, "/api/show").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.PATCH, "/api/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.POST, "/api/theatre").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.GET, "/api/theatre", "/api/theatre/*/shows").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.PATCH, "/api/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())

                                .requestMatchers(HttpMethod.GET, "/api/tickets").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/tickets").hasAuthority(Role.THEATRE_OWNER.code())
                                .requestMatchers(HttpMethod.GET, "/api/user/tickets").hasAuthority(Role.REGULAR_USER.code())
                                .requestMatchers(HttpMethod.PATCH, "/api/tickets/book", "/api/tickets/cancel").hasAuthority(Role.REGULAR_USER.code())
                                .requestMatchers(HttpMethod.POST, "/api/tickets").hasAuthority(Role.ADMIN.code())

                                .requestMatchers("/api/cart/**", "/api/cart").hasAuthority(Role.REGULAR_USER.code())

                                .requestMatchers(HttpMethod.GET,   "/**", "/assets/**", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/logout").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/user/activate/*").permitAll()

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
