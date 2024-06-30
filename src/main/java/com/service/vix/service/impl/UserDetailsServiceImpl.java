package com.service.vix.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.vix.models.Organization;
import com.service.vix.models.User;
import com.service.vix.repositories.UserRepository;
import com.service.vix.utility.DataHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used for User Details Service Implemetation that implement all
 * the methods for spring security user
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Value("${project.organization.upload-dir}")
	private String organizationLogoUploadDirectory;
	@Value("${project.user.upload-dir}")
	private String userImageUploadDirectory;

	/**
	 * @param userRepository Description
	 */
	public UserDetailsServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	/*
	 * (non-Javadoc)This method is used to load user by given username or email and
	 * serve it to spring security
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Enter inside UserDetailsServiceImpl.loadUserByUsername() method");
		Optional<User> optionalUser = null;

		// Get user by username
		optionalUser = this.userRepository.findByUsername(username);

		// If user not found by username then check with email
		if (optionalUser.isEmpty())
			optionalUser = userRepository.findByEmail(username);

		User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

		DataHelper dataHelper = new DataHelper();
		if (user.getOrganizationDetails() != null) {
			Organization organizationDetails = user.getOrganizationDetails();
			dataHelper.setLoggedInOrganizationName(organizationDetails.getOrgName());
			String[] orgLogoUploadDirArr = this.organizationLogoUploadDirectory.split("/");
			int orgLogoUploadDirArrLength = orgLogoUploadDirArr.length;
			dataHelper.setLoggedInOrganizationProfileURL(
					orgLogoUploadDirArr[orgLogoUploadDirArrLength - 1] + "/" + organizationDetails.getOrgLogo());
			dataHelper.setLoggedInOrganizationDetails(organizationDetails);
		}
		if (user.getStaff() != null && user.getStaff().getOrganization() != null) {
			Organization organizationDetails = user.getStaff().getOrganization();
			dataHelper.setLoggedInOrganizationName(organizationDetails.getOrgName());
			String[] orgLogoUploadDirArr = this.organizationLogoUploadDirectory.split("/");
			int orgLogoUploadDirArrLength = orgLogoUploadDirArr.length;
			dataHelper.setLoggedInOrganizationProfileURL(
					orgLogoUploadDirArr[orgLogoUploadDirArrLength - 1] + "/" + organizationDetails.getOrgLogo());
			dataHelper.setLoggedInOrganizationDetails(organizationDetails);
		}
		dataHelper.setLoggedInUserId(user.getId());
		dataHelper.setLoggedInUserName(user.getFirstName());

		if (user.getProfileURL() != null) {
			String[] userProfileUploadDirArr = this.userImageUploadDirectory.split("/");
			int userProfileUploadDirArrLength = userProfileUploadDirArr.length;
			dataHelper.setLoggedInUserProfileURL(
					userProfileUploadDirArr[userProfileUploadDirArrLength - 1] + "/" + user.getProfileURL());
		}

		log.info("User found by given credentials.");
		return UserDetailsImpl.build(user, dataHelper);
	}

}
