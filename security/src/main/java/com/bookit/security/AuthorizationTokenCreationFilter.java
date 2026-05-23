package com.bookit.security;

import com.bookit.security.user.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

public class AuthorizationTokenCreationFilter implements Filter {
    private ITokenService<Long> tokenService;
    private UserService userService;

    public AuthorizationTokenCreationFilter(ITokenService<Long> tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if(request instanceof HttpServletRequest){
            System.out.println("request is http servlet request");
            if(securityContext.getAuthentication() != null &&
                    !securityContext.getAuthentication().getName().equals("anonymousUser")){
                String token = tokenService.generateToken(userService.getCurrentUserId());
                HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request){
                    @Override
                    public String getHeader(String name){
                        if(name.equalsIgnoreCase("Authorization")){
                            return String.format("Bearer %s", token);
                        }
                        return super.getHeader(name);
                    }
                };
                System.out.println("auth is not null with request: " + ((HttpServletRequest) request).getServletPath());
                chain.doFilter(httpServletRequestWrapper, response);
                return;
            }
            else{
                System.out.println("auth is null with request: " + ((HttpServletRequest) request).getServletPath());
            }
        }

        chain.doFilter(request, response);
    }
}
