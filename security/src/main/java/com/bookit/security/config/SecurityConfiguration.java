package com.bookit.security.config;


import com.bookit.security.AuthorizationTokenCreationFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@Profile("production")
public class SecurityConfiguration extends SecurityConfigurationBase {
    private SecurityContextRepository securityContextRepository;

    public SecurityConfiguration(FilterRegistrationBean<AuthorizationTokenCreationFilter> registrationBean,
                                 SecurityContextRepository securityContextRepository) {
        super(registrationBean.getFilter());
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = super.createFilters(http);
        httpSecurity
                .securityContext(securityContext -> {
                    securityContext.securityContextRepository(securityContextRepository);
                })
                .csrf(csrf -> {
                    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
                    repository.setCookieCustomizer(responseCookieBuilder -> {
                        responseCookieBuilder.secure(true);
                        responseCookieBuilder.sameSite("strict");
                        responseCookieBuilder.httpOnly(false);
                    });
                    csrf.csrfTokenRepository(repository);
                    csrf.ignoringRequestMatchers("/api/logout", "/api/user/activate/*");
                    csrf.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
                })

                .logout(logout -> {
                    logout.permitAll();
                    logout.permitAll(true);
                    logout.logoutUrl("/api/logout");
                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
                    logout.addLogoutHandler((request, response, auth) -> {
                        try {
                            if (request.getCookies() != null) {
                                for (Cookie cookie : request.getCookies()) {
                                    String cookieName = cookie.getName();
                                    Cookie cookieToDelete = new Cookie(cookieName, null);
                                    cookieToDelete.setMaxAge(0);
                                    response.addCookie(cookieToDelete);
                                }
                            }
                            request.logout();
                        } catch (ServletException e) {
                            System.out.println(e.getMessage());
                        }
                    });
                });
        return httpSecurity.build();
    }


}
