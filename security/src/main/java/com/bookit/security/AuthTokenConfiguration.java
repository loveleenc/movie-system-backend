package com.bookit.security;


import com.bookit.security.user.UserService;
import com.bookit.security.user.token.AuthTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthTokenConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "token.auth")
    public AuthTokenProperties getAuthTokenProperties(){
        return new AuthTokenProperties();
    }

    @Bean("authTokenService")
    public ITokenService<?> getAuthTokenService(AuthTokenProperties authTokenProperties){
        return new AuthTokenService(authTokenProperties);
    }

    @Bean
    public FilterRegistrationBean<AuthorizationTokenCreationFilter> getAuthTokenCreationFilter(
            @Qualifier("authTokenService") ITokenService<Long> tokenService,
            UserService userService
    ){
        FilterRegistrationBean<AuthorizationTokenCreationFilter>
                registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthorizationTokenCreationFilter(tokenService,
                userService));
        return registrationBean;
    }
}
