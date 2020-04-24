package com.json.comparator.spring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * This class loads the user specific data from application.properties file
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

	@Value("${spring.security.username}")
	private String username;
	
	@Value("${spring.security.password}")
	private String password;
	
    @Override
    public UserDetails loadUserByUsername(String s) {
        return new User(username, password,
                new ArrayList<>());
    }
}