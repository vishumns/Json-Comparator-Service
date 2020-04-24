package com.json.comparator.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class to filter the user request with given jwt token.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOGGER=LoggerFactory.getLogger(JwtRequestFilter.class);
	
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
    	try {
	        final String authorizationHeader = request.getHeader("Authorization");
	        String username = null;
	        String jwt = null;
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwt = authorizationHeader.substring(7);
	            username = jwtUtil.extractUsername(jwt);
	        }
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	
	            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
	
	            if (Boolean.TRUE.equals(jwtUtil.validateToken(jwt, userDetails))) {
	
	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                usernamePasswordAuthenticationToken
	                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	            }
	        }
	        chain.doFilter(request, response);
    	} catch (Exception e) {
    		response.setStatus(401);
    		response.setContentType("Text");
    		response.setCharacterEncoding("UTF-8");
    		response.getWriter().write("Please check jwt token");
    		LOGGER.error(e.getMessage());
    	}
    }

}
