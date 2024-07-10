package com.service.vix.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.vix.models.Permission;
import com.service.vix.models.User;
import com.service.vix.utility.DataHelper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is used for Service Implemetation that implement all the methods
 * for spring security user
 */
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	private DataHelper dataHelper;

	/**
	 * This method is used to build the user and get the authority for given user
	 * 
	 * @author hemantrs
	 * @date May 31, 2023
	 * @return UserDetailsImpl
	 * @param user
	 * @return
	 * @exception Description
	 */
	public static UserDetailsImpl build(User user, DataHelper dataHelper) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getName().toString()));

		List<GrantedAuthority> grantedAuthorities = convertUserPermissionsToSimpleGrantedAuthorities(
				user.getRole().getPermissions());
		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName().toString()));

		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(),
				grantedAuthorities, dataHelper);
	}

	/**
	 * This method is used to convert Permissions to Granted Authority
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 23, 2023
	 * @return List<GrantedAuthority>
	 * @param permissions
	 * @return
	 * @exception Description
	 */
	static List<GrantedAuthority> convertUserPermissionsToSimpleGrantedAuthorities(List<Permission> permissions) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		permissions.stream().forEach(permission -> {
			String permissionName = permission.getPermissionName().toString().toUpperCase();

			if (permission.getListView() || permission.getIndividualView() || permission.getAddNew()
					|| permission.getUpdateExisting() || permission.getDelete())
				authorities.add(new SimpleGrantedAuthority(permissionName));

			if (permission.getListView())
				authorities.add(new SimpleGrantedAuthority(permissionName + "_LIST_VIEW"));

			if (permission.getIndividualView())
				authorities.add(new SimpleGrantedAuthority(permissionName + "_INDIVIDUAL_VIEW"));

			if (permission.getAddNew())
				authorities.add(new SimpleGrantedAuthority(permissionName + "_ADD"));

			if (permission.getUpdateExisting())
				authorities.add(new SimpleGrantedAuthority(permissionName + "_UPDATE"));

			if (permission.getDelete())
				authorities.add(new SimpleGrantedAuthority(permissionName + "_DELETE"));
		});

		if (authorities.contains(new SimpleGrantedAuthority("PRODUCT"))
				|| authorities.contains(new SimpleGrantedAuthority("PRODUCT_CATEGORY"))
				|| authorities.contains(new SimpleGrantedAuthority("SERVICE"))
				|| authorities.contains(new SimpleGrantedAuthority("SERVICE_CATEGORY")))
			authorities.add(new SimpleGrantedAuthority("PRODUCT_AND_SERVICE"));

		return authorities;
	}

	/*
	 * (non-Javadoc)This method is used to get authorities for user that build by
	 * spring security
	 * 
	 * @see
	 * org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
