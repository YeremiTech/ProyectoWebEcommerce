package com.ecommerce.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

import com.ecommerce.auth.repository.AuthUsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUsuarioRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = repo.findByUsername(username.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String roleName = user.getRol().getNombreRol().trim().toUpperCase();

        String authority = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;

        return new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(authority))
        );
    }
}
