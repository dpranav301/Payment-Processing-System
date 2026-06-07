package com.user.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.user.entity.User;
import com.user.repository.UserRepository;
import com.user.utils.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String authHeader = request.getHeader("Authorization");

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {

				filterChain.doFilter(request, response);

				return;
			}

			String token = authHeader.substring(7);

			String email = jwtService.extractUsername(token);

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				User user = userRepository.findByEmail(email).orElse(null);

				if (user != null && jwtService.isTokenValid(token, email)) {

					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
							List.of(new SimpleGrantedAuthority(user.getRole().name())));

					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
		} catch (ExpiredJwtException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"message\":\"Token Expired\"}");
			response.setContentType("application/json");
			return;
		} catch (JwtException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"message\":\"Invalid Token\"}");
			response.setContentType("application/json");
			return;
		}

		filterChain.doFilter(request, response);
	}

}
