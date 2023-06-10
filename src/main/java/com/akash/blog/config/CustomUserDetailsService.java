package com.akash.blog.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.akash.blog.entity.User;
import com.akash.blog.repository.UserRepository;


public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = Optional.of(userRepository.findByEmail(email));
		return user.map(CustomUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("user with email=" + email + " not found."));
	}

}
