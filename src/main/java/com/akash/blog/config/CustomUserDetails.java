package com.akash.blog.config;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.akash.blog.entity.User;

public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String authority;
	private String profilePicture;
	private int userId;
	private String fullname;

	public CustomUserDetails(User user) {
		this.username = user.getEmail();
		this.password = user.getPassword();
		this.authority = user.getRole();
		this.profilePicture = user.getProfilePicture();
		this.userId = user.getId();
		this.fullname = user.getFirstName() + " " + user.getLastName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(authority));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getRole() {
		return authority;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public int getUserId() {
		return userId;
	}

	public String getFullname() {
		return fullname;
	}
}
