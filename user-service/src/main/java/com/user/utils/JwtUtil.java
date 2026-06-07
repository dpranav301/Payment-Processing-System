package com.user.utils;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/*Generate Token
Extract Username
Validate Token
Check Expiration*/
@Service
public class JwtUtil {

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration-ms}")
	private long jwtExpiration;

	@Value("${app.jwt.refresh-expiration-ms}")
	private long refreshExpiration;

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(jwtExpiration + System.currentTimeMillis())).signWith(getSigningKey()).compact();
	}

	public String generateRefreshToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(refreshExpiration + System.currentTimeMillis())).signWith(getSigningKey())
				.compact();
	}

	public boolean isTokenValid(String token, String email) {

		String username = extractUsername(token);

		return username.equals(email) && !isTokenExpired(token);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {

		Claims claims = extractAllClaims(token);

		return resolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

}
