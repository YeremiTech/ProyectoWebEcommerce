package com.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.auth.service.AuthUsuarioService;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwt;
	private final AuthUsuarioService users;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			try {
				var claims = jwt.parse(token).getBody();
				var username = claims.getSubject();
				var role = (String) claims.get("role");

				System.out
						.println("JWT -> username=" + username + ", role=" + role + ", uri=" + request.getRequestURI());

				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					var user = users.porUsername(username.trim().toLowerCase()).orElse(null);
					if (user != null) {
						var auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
								List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(role)));
						auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		chain.doFilter(request, response);
	}
}
