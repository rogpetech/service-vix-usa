/* 
 * ===========================================================================
 * File Name ProfileServiceImpl.java
 * 
 * Created on Aug 16, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: ProfileServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.OrganizationDTO;
import com.service.vix.dto.PasswordChangeDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.mapper.OrganizationMapper;
import com.service.vix.mapper.UserMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.User;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as ProfileService Implementation
 */
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private Environment env;
	@Autowired
	private PasswordEncoder encoder;

	@Value("${project.organization.upload-dir}")
	private String organizationLogoUploadDirectory;
	@Value("${project.user.upload-dir}")
	private String userImgUploadDirectory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProfileService#loggedInUserDetails(java.security.
	 * Principal)
	 */
	@Override
	public CommonResponse<UserDTO> loggedInUserDetails(Principal principal) {
		log.info("Enter inside ProfileServiceImpl.loggedInUserDetails() method.");

		CommonResponse<UserDTO> response = new CommonResponse<>();
		UserDTO loggedInUserDTO = new UserDTO();
		String loggedInUserName = principal.getName();
		if (loggedInUserName != null) {
			Optional<User> loggedInUserOpt = this.userRepository.findByUsername(loggedInUserName);
			if (!loggedInUserOpt.isEmpty()) {
				User user = loggedInUserOpt.get();
				loggedInUserDTO = UserMapper.INSTANCE.userToUserDTO(user);

				if (user.getProfileURL() != null) {
					String profileURL = user.getProfileURL();
					String[] userImageUploadDirArr = this.userImgUploadDirectory.split("/");
					int userImageUploadDirArrLength = userImageUploadDirArr.length;
					loggedInUserDTO.setUserProfilePictureURL(
							userImageUploadDirArr[userImageUploadDirArrLength - 1] + "/" + profileURL);
				}

				if (user.getOrganizationDetails() != null) {
					Organization organizationDetails = user.getOrganizationDetails();
					OrganizationDTO organizationDTO = OrganizationMapper.INSTANCE
							.organizationToOrganizationDTO(organizationDetails);
					String orgLogo = organizationDetails.getOrgLogo();
					String[] orgLogoUploadDirArr = this.organizationLogoUploadDirectory.split("/");
					int orgLogoUploadDirArrLength = orgLogoUploadDirArr.length;
					loggedInUserDTO
							.setOrganizationLogoURL(orgLogoUploadDirArr[orgLogoUploadDirArrLength - 1] + "/" + orgLogo);
					loggedInUserDTO.setOrganizationDTO(organizationDTO);
				}
			}
		}
		response.setData(loggedInUserDTO);
		response.setMessage(env.getProperty("record.found.success"));
		response.setResult(false);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProfileService#changePassword(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<PasswordChangeDTO> changePassword(HttpServletRequest httpServletRequest,
			HttpSession httpSession) {
		log.info("Enter inside ProfileServiceImpl.changePassword() method.");
		CommonResponse<PasswordChangeDTO> response = new CommonResponse<>();
		PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
		String currentPassword = httpServletRequest.getParameter("currentPasswordInp");
		String newPassword = httpServletRequest.getParameter("confrimPasswordInp");
		String userId = httpServletRequest.getParameter("userId");
		String msg = "";
		if (userId != null) {
			Optional<User> userOpt = this.userRepository.findById(Long.valueOf(userId));
			if (userOpt.isPresent()) {
				User currentUser = userOpt.get();
				if (encoder.matches(currentPassword, currentUser.getPassword())) {
					msg = env.getProperty("password.changed.success");
					currentUser.setPassword(this.encoder.encode(newPassword));
					this.userRepository.save(currentUser);
					passwordChangeDTO.setIsPasswordChange(true);
					passwordChangeDTO.setReason(msg);
					httpSession.setAttribute("message", new Message(msg, "success"));
				} else {
					msg = env.getProperty("password.not.changed.current.password.not.matched");
					httpSession.setAttribute("message", new Message(msg, "danger"));
					passwordChangeDTO.setIsPasswordChange(false);
					passwordChangeDTO.setReason(msg);
				}
			} else {
				msg = env.getProperty("password.not.changed.user.not.found");
				httpSession.setAttribute("message", new Message(msg, "danger"));
				passwordChangeDTO.setIsPasswordChange(false);
				passwordChangeDTO.setReason(msg);
			}
			response.setData(passwordChangeDTO);
			response.setStatus(HttpStatus.OK.value());
			response.setResult(true);
			response.setMessage(env.getProperty("record.found.success"));
		} else {
			response.setData(passwordChangeDTO);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			response.setResult(false);
			response.setMessage(env.getProperty("record.not.found"));
		}
		return response;
	}

}
