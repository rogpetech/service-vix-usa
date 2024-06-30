/* 
 * ===========================================================================
 * File Name RoleServiceImpl.java
 * 
 * Created on Aug 18, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: RoleServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.RoleDTO;
import com.service.vix.enums.Permissions;
import com.service.vix.mapper.RoleMapper;
import com.service.vix.models.Permission;
import com.service.vix.models.Role;
import com.service.vix.models.User;
import com.service.vix.repositories.PermissionRepository;
import com.service.vix.repositories.RoleRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to implement all methods related to Role
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.RoleService#saveRole(com.service.vix.dto.RoleDTO,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<RoleDTO> saveRole(RoleDTO roleDTO, HttpSession httpSession) {
		log.info("Enter inside RoleServiceImpl.saveRole() method.");
		CommonResponse<RoleDTO> response = new CommonResponse<RoleDTO>();
		String msg = "";
		Boolean existByName = this.roleRepository.existsByName(roleDTO.getName());
		if (existByName) {
			Role role = this.roleRepository.findByName(roleDTO.getName()).get();
			role.setIsDeleted(false);
			Role existingRole = this.roleRepository.save(role);
			msg = env.getProperty("role.already.exists");
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(RoleMapper.INSTANCE.roleToRoleDTO(existingRole));
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			Role role = RoleMapper.INSTANCE.roleDTOToRole(roleDTO);
			Role savedRole = null;
			try {
				List<Permission> permissions = new ArrayList<Permission>();
				Arrays.stream(Permissions.values()).forEach(ePermission -> permissions
						.add(new Permission(ePermission, false, false, false, false, false, role)));
				// Save Permissions
//				List<Permission> savedPermissions = this.permissionRepository.saveAll(permissions);
				role.setPermissions(permissions);

				savedRole = this.roleRepository.save(role);

				roleDTO = RoleMapper.INSTANCE.roleToRoleDTO(savedRole);
				msg = env.getProperty("role.add.success");
				log.info(msg);
				httpSession.setAttribute("message", new Message(msg, "success"));
				response.setMessage(msg);
				response.setData(roleDTO);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			} catch (Exception e) {
				msg = env.getProperty("something.went.wrong");
				log.info(msg);
				httpSession.setAttribute("message", new Message(msg, "danger"));
				response.setMessage(msg);
				response.setData(roleDTO);
				response.setResult(false);
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return response;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.RoleService#getRoles()
	 */
	@Override
	public CommonResponse<List<RoleDTO>> getRoles() {
		log.info("Enter inside RoleServiceImpl.getRoless() method.");
		CommonResponse<List<RoleDTO>> response = new CommonResponse<>();
		List<Role> roles = this.roleRepository.findByIsDeletedFalse();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		roles.forEach(role -> {
			RoleDTO roleDTO = RoleMapper.INSTANCE.roleToRoleDTO(role);
			Map<String, Permission> permissionMap = new HashMap<>();
			role.getPermissions()
					.forEach(permission -> permissionMap.put(permission.getPermissionName().toString(), permission));
			roleDTO.setPermissionsMap(permissionMap);
			roleDTOs.add(roleDTO);
		});
		String msg = env.getProperty("role.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(roleDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.RoleService#getRoleById(java.lang.Long)
	 */
	@Override
	public CommonResponse<RoleDTO> getRoleById(Long roleId) {
		log.info("Enter inside RoleServiceImpl.getCustomerById() method.");
		CommonResponse<RoleDTO> response = new CommonResponse<>();
		if (roleId != null && roleId > 0) {
			RoleDTO roleDTO = new RoleDTO();
			Optional<Role> roleOpt = this.roleRepository.findById(roleId);
			if (roleOpt.isPresent()) {
				Role role = roleOpt.get();
				roleDTO = RoleMapper.INSTANCE.roleToRoleDTO(role);
				Map<String, Permission> permissionMap = new HashMap<>();
//				role.getPermissions().forEach(permission -> {
//					String perStr = permission.getPermissionName().toString();
//					if (perStr.contains("_"))
//						perStr.replaceAll("_", " ");
//					permissionMap.put(perStr, permission);
//				});

				for (Permission permission : role.getPermissions()) {
					String perStr = permission.getPermissionName().toString();
					if (perStr.contains("_")) {
						perStr = perStr.replaceAll("_", " ");
					}
					permissionMap.put(perStr, permission);
				}

				roleDTO.setPermissionsMap(permissionMap);
			}
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(roleDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.RoleService#removeRole(java.lang.Long)
	 */
	@Override
	public CommonResponse<Boolean> removeRole(Long roleId, HttpSession httpSession) {
		log.info("Enter inside RoleServiceImpl.removeRole() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		String msg = "";
		if (roleId != null && roleId > 0) {
			Optional<Role> roleOpt = this.roleRepository.findById(roleId);
			Optional<User> userWithRole = this.userRepository.findByRoleId(roleId);
			if (userWithRole.isPresent()) {
				msg = env.getProperty("role.associated.user");
				log.info(msg);
				httpSession.setAttribute("message", new Message(msg, "danger"));
				response.setMessage(msg);
				response.setData(false);
				response.setResult(true);
				response.setStatus(HttpStatus.BAD_REQUEST.value());
				return response;
			} else {
				Role role = roleOpt.get();
				role.setIsDeleted(true);
				roleRepository.save(role);
				msg = env.getProperty("role.delete.success");
				httpSession.setAttribute("message", new Message(msg, "success"));
				log.info(msg);
				response.setMessage(msg);
				response.setData(true);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			}
		} else {
			msg = env.getProperty("role.delete.failed");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setMessage(msg);
			response.setData(false);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.RoleService#updateRole(jakarta.servlet.http.
	 * HttpServletRequest, jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<RoleDTO> updateRole(RoleDTO roleDTO, HttpSession httpSession) {
		log.info("Enter inside RoleServiceImpl.updateRole() method.");
		CommonResponse<RoleDTO> response = new CommonResponse<RoleDTO>();
		String msg = "";
		try {
			Role role = RoleMapper.INSTANCE.roleDTOToRole(roleDTO);
			List<Permission> permissions = new ArrayList<>();
			int counter = 0;
			List<Permission> rolePermissions = this.permissionRepository.findByRole(role);
			for (Permission rp : rolePermissions) {
				Permission permission = roleDTO.getPermissions().get(counter);
				permission.setId(rp.getId());
				permissions.add(permission);
				counter++;
			}
			role.setPermissions(permissions);
			try {
				this.roleRepository.save(role);
				response.setData(roleDTO);
				msg = env.getProperty("role.permission.update.success");
				response.setMessage(msg);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			} catch (Exception e) {
				msg = env.getProperty("role.permission.update.fail");
				log.error("Exception occure in RoleServiceImpl.updateRole() method EXCEPTION(2).");
				httpSession.setAttribute("message", new Message(msg, "danger"));
				response.setData(roleDTO);
				response.setMessage(msg);
				response.setResult(false);
				response.setStatus(HttpStatus.OK.value());
				return response;
			}
		} catch (Exception e) {
			log.error("Exception occure in RoleServiceImpl.updateRole() method EXCEPTION(1).");
			msg = env.getProperty("role.permission.update.fail");
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setData(roleDTO);
			response.setMessage(msg);
			response.setResult(false);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.RoleService#extractRoleFormObject(jakarta.servlet.
	 * http.HttpServletRequest)
	 */
	@Override
	public RoleDTO extractRoleFormObject(HttpServletRequest httpServletRequest) {
		log.info("Enter inside RoleServiceImpl.extractRoleFormObject() method.");
		String roleName = httpServletRequest.getParameter("selected_role_name");
		Role role = this.roleRepository.findByName(roleName).get();
		log.info("Role fetch successfully from DB");
		List<Permission> permissions = new ArrayList<>();
		Arrays.stream(Permissions.values()).forEach(ePermission -> {
			String strPer = ePermission.toString();
			if (ePermission.toString().contains("_"))
				strPer = ePermission.toString().replaceAll("_", " ");
			Boolean listViewCheck = httpServletRequest.getParameter(strPer + "_list_view_check") != null ? true : false;
			Boolean individualViewCheck = httpServletRequest.getParameter(strPer + "_individual_view_check") != null
					? true
					: false;
			Boolean addNewCheck = httpServletRequest.getParameter(strPer + "_add_new_check") != null ? true : false;
			Boolean updateExistingCheck = httpServletRequest.getParameter(strPer + "_update_existing_check") != null
					? true
					: false;
			Boolean deleteCheck = httpServletRequest.getParameter(strPer + "_delete_check") != null ? true : false;
			permissions.add(new Permission(ePermission, listViewCheck, individualViewCheck, addNewCheck,
					updateExistingCheck, deleteCheck, role));
		});
		log.info("Permissions extract from form successfully");
		role.setPermissions(permissions);
		return RoleMapper.INSTANCE.roleToRoleDTO(role);
	}
}
