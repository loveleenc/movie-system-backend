package com.bookit.security.config;


import com.bookit.security.AuthorizationTokenCreationFilter;
import com.bookit.security.entity.types.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class SecurityConfigurationBase {
    private AuthorizationTokenCreationFilter authorizationTokenCreationFilter;

    public SecurityConfigurationBase(AuthorizationTokenCreationFilter authorizationTokenCreationFilter) {
        this.authorizationTokenCreationFilter = authorizationTokenCreationFilter;
    }

    public HttpSecurity createFilters(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorizationManagerRequestMatcherRegistry) ->
                        authorizationManagerRequestMatcherRegistry
                                //IMPORTANT: THE ORDER OF REQUEST MATCHERS HAS TO BE CORRECT
                                //More specific patterns should be defined before more general ones to ensure proper matching
                                .requestMatchers("/actuator/**").permitAll()

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

                                .requestMatchers(HttpMethod.GET, "/**", "/assets/**", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                .requestMatchers("/api/logout").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/api/user/activate/*").permitAll()

                )
                .addFilterAfter(this.authorizationTokenCreationFilter, BasicAuthenticationFilter.class);
        return http;
    }
}
