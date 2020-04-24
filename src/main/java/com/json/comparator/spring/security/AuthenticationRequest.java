package com.json.comparator.spring.security;

import java.io.Serializable;

/**
 * Authentication request class with username and password.
 * Values of this properties are available in application.properties
 */
public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //need default constructor for JSON Parsing
    public AuthenticationRequest()
    {

    }

    public AuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
