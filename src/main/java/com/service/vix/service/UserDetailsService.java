package com.service.vix.service;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * This class is used as service class for spring security that handles user
 * request for spring security
 */
public interface UserDetailsService {
	/**
	 * This method is used to load user by given username and also give user after
	 * load to spring security
	 * 
	 * @author hemantr
	 * @date May 31, 2023
	 * @return UserDetails
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 * @exception Description
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
