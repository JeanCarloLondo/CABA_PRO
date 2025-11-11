/**
 *  Security configuration for the CABAPRO application.
 *
 * Author: Jean Londoño and Alejandra Ortiz
 * Date: 2025-09-05
 * Role: Security Configuration
 */

package com.cabapro.development.config;

import com.cabapro.development.service.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        private final CustomUserService customUserService;

        public SecurityConfig(CustomUserService customUserService) {
                this.customUserService = customUserService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        HttpSecurity http,
                        PasswordEncoder passwordEncoder) throws Exception {

                return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(customUserService)
                                .passwordEncoder(passwordEncoder)
                                .and()
                                .build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        CustomLoginSuccessHandler successHandler) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                //  Permitir acceso libre a la API
                                                .requestMatchers("/api/**").permitAll()

                                                //  Rutas públicas
                                                .requestMatchers(
                                                                "/login",
                                                                "/register",
                                                                "/css/**", "/js/**", "/images/**",
                                                                "/h2-console/**",
                                                                "/admin/users/create")
                                                .permitAll()

                                                //  Rutas protegidas por rol
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/referees/**").hasRole("REFEREE")

                                                //  Todo lo demás requiere autenticación
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler(successHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .sessionManagement(session -> session
                                                .maximumSessions(1)
                                                .expiredUrl("/login?expired"))
                                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

                return http.build();
        }
}