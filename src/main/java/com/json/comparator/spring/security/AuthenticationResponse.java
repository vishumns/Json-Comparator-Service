package com.json.comparator.spring.security;

import java.io.Serializable;

/**
 * Authentication response class with jwt property.
 * Jwt token is required to pass some user request (/compare-jsons)
 */
public class AuthenticationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
