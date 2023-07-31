/**
 * 
 */
package com.nttdata.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.nttdata.security.JWTAuthorizationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * 
 */
@Service
public class JWTTokenService {

	private static final String SECRET_KEY = "fgbmo1tKvzYPxeJ9rbK8XdNZXeo8Fj1SNTTDATAKEYDD9D888D2E954";

	public String getJWTToken(String subject) {
		Instant now = Instant.now();
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
				
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

		return JWTAuthorizationFilter.PREFIX
				+ Jwts.builder()
				.setSubject(subject)
				.setIssuer(JWTAuthorizationFilter.ISSUER)
				.setIssuedAt(Date.from(now))
				.claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
				.signWith(Keys.hmacShaKeyFor(keyBytes)).compact();
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public Claims validateToken(HttpServletRequest request) throws Exception {
		String jwtToken = request.getHeader(JWTAuthorizationFilter.HEADER).replace(JWTAuthorizationFilter.PREFIX, "");
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

		Claims claims= Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(keyBytes)).build().parseClaimsJws(jwtToken)
				.getBody();
		if (claims.get("authorities") != null 
			&& claims.getIssuer().equals(JWTAuthorizationFilter.ISSUER) 
				) {
			return claims;
		}
		else
			throw new Exception("Invalid token");
	}

}
