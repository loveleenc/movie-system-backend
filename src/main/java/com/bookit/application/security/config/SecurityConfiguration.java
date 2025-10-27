package com.bookit.application.security.config;

import com.bookit.application.security.CustomUserDetailsService;
import com.bookit.application.types.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//TODO: deal with duplicate code in the production and dev security configs

@Profile("production")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) ->
                                authorizationManagerRequestMatcherRegistry
                                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/movies/ongoing", "/movies/upcoming").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/movies").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/movie/\\d+/shows").permitAll()

                                        .requestMatchers(HttpMethod.POST, "/movie").hasAuthority(Role.ADMIN.code())

                                        .requestMatchers(HttpMethod.POST, "/show").hasAuthority(Role.THEATRE_OWNER.code())
                                        .requestMatchers(HttpMethod.PATCH, "/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())


                                        .requestMatchers(HttpMethod.POST, "/theatre").hasAuthority(Role.THEATRE_OWNER.code())
                                        .requestMatchers(HttpMethod.GET, "/theatre", "/theatre/\\d+/shows").hasAuthority(Role.THEATRE_OWNER.code())

                                        .requestMatchers(HttpMethod.PATCH, "/show/cancel").hasAuthority(Role.THEATRE_OWNER.code())


                                        .requestMatchers(HttpMethod.GET, "/tickets").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/tickets").hasAuthority(Role.THEATRE_OWNER.code())
                                        .requestMatchers(HttpMethod.PATCH, "/tickets/book", "/tickets/cancel").hasAuthority(Role.REGULAR_USER.code())

                                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
        return new CustomUserDetailsService(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }
}
