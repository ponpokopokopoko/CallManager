package com.example.demo.common;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (isAdmin) {
        	System.out.println("ADMIN");
            response.sendRedirect("/admin-menu");///admin-menu　基本はsendRedirectを使う。nullは他を痛がった方がいい
            //request.getRequestDispatcher("/general-menu").forward(request, response);

        } else {
        	System.out.println("USER");
            response.sendRedirect("/general-menu");
        }
    }
}

