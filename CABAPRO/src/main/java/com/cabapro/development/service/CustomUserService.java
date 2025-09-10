package com.cabapro.development.service;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalized = username == null ? "" : username.trim().toLowerCase(Locale.ROOT);

        CustomUser user = userRepo.findByEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + normalized));

        // role puede venir "ADMIN", "REFEREE" o incluso "ROLE_ADMIN". También soporta "ADMIN,REFEREE".
        String roleStr = user.getRole();
        if (roleStr == null || roleStr.isBlank()) {
            throw new UsernameNotFoundException("User without role: " + normalized);
        }

        List<GrantedAuthority> authorities = Arrays.stream(roleStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(r -> r.toUpperCase(Locale.ROOT))
                .map(r -> r.startsWith("ROLE_") ? r.substring("ROLE_".length()) : r) // quita prefijo si vino con ROLE_
                .map(r -> "ROLE_" + r)                                               // agrega prefijo correcto
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)          // usamos authorities para respetar lo normalizado
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)                   // cámbialo si luego agregas un flag "enabled" en tu entidad
                .build();
    }
}