/**
 *  Custom login success handler for the CABAPRO application.
 *
 * Author: Jean Londoño and Alejandra Ortiz
 * Date: 2025-09-05
 * Role: Security Configuration
 */

package com.cabapro.development.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.cabapro.development.repository.RefereeRepository;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RefereeRepository refereeRepository;

    public CustomLoginSuccessHandler(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        var authorities = authentication.getAuthorities();
        String redirectUrl = "/";

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            redirectUrl = "/admin";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_REFEREE"))) {
            String email = authentication.getName();
            var referee = refereeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalStateException("Referee not found for email: " + email));
            redirectUrl = "/referee/dashboard/" + referee.getId();
        }

        response.sendRedirect(redirectUrl);
    }
}