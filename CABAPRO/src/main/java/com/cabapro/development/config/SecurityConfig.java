package com.cabapro.development.config;

import com.cabapro.development.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final CustomUserService customUserService;

    @Autowired
    public SecurityConfig(CustomUserService customUserService) {
        this.customUserService = customUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configurar reglas de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/h2-console/**").permitAll()
                // Rutas que requieren autenticación
                .requestMatchers("/matches/**", "/specialties/**", "/users/**").authenticated()
                // Cualquier otra ruta requiere estar autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Vista en home/login.html
                .defaultSuccessUrl("/", true) // Redirige al home tras login exitoso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()) 
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())); // To allow H2 console

        return http.build();
    }

    // Autenticación con tu CustomUserService y BCrypt
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService).passwordEncoder(passwordEncoder());
    }
}
