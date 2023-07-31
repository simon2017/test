/**
 * 
 */
package com.nttdata.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nttdata.service.JWTTokenService;

import io.jsonwebtoken.Claims;


public class JWTAuthorizationFilter extends OncePerRequestFilter {

	
	public static final String HEADER="Authorization";
	public static final String PREFIX="Bearer ";
	public static final String ISSUER="NTTDATA";
	
	private JWTTokenService tokenService=BeanUtils.instantiateClass(JWTTokenService.class);
	//public static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			if (existeJWTToken(request, response)) {
				Claims claims = tokenService.validateToken(request);
				setUpSpringAuthentication(claims);
			}
			else
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			chain.doFilter(request, response);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}
	
	/**
	 * Valida header buscando Token
	 * @param request
	 * @param res
	 * @return
	 */
	private boolean existeJWTToken(HttpServletRequest request, HttpServletResponse res) {
		String authenticationHeader = request.getHeader(HEADER);
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}
	
	
	/**
	 * Metodo para autenticarnos dentro del flujo de Spring
	 * 
	 * @param claims
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				claims.getSubject(), 
				null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
				);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
