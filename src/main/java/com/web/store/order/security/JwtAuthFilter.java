package com.web.store.order.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	@Autowired
	JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader=request.getHeader("Authorization");
		String token=null;
		String username=null;
		
		if(authHeader!=null&&authHeader.startsWith("Bearer ")) {
			token=authHeader.substring(7);
			username=jwtUtil.extractUserName(token);			
		}
		if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
			if(jwtUtil.validateToken(token)) {
				String role=jwtUtil.extractRole(token);
				UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
						username,null, List.of(new SimpleGrantedAuthority("ROLE_" +role)));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	
		
	}

}
