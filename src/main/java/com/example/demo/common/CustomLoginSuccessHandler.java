package com.example.demo.common;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//ADMIN権限を持ったユーザーの有無で遷移先を分岐するロジック
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (isAdmin) {
            response.sendRedirect("/admin-menu");
            //request.getRequestDispatcher("/general-menu").forward(request, response);

        } else {
            response.sendRedirect("/general-menu");
        }
    }
}

