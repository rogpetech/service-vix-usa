/* 
 * ===========================================================================
 * File Name CommonServiceImpl.java
 * 
 * Created on Jul 31, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: CommonServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.vix.dto.ProductDTO;
import com.service.vix.dto.ServiceDTO;
import com.service.vix.mapper.ProductMapper;
import com.service.vix.mapper.ServiceMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.Services;
import com.service.vix.models.User;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.CommonService;

import lombok.extern.slf4j.Slf4j;

/**
 * This method is implementation of commonService interface methods
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private UserRepository userRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.CommonService#searchProductAndServicesByName(java.
	 * lang.String, java.security.Principal)
	 */
	@Override
	public Map<String, Object> searchProductAndServicesByName(String productServiceName, Principal principal) {
		log.info("Enter inside CommonServiceImpl.searchProductAndServicesByName() method");
		Organization organization = this.getLoggedInUserOrganization(principal);
		List<Product> searchProductList = this.productRepository
				.findByProductNameLikeAndIsDeletedFalseAndOrganization("%" + productServiceName + "%", organization);
		List<ProductDTO> searchProductDTOs = new ArrayList<>();
		searchProductList.stream().forEach(p -> searchProductDTOs.add(ProductMapper.INSTANCE.productToProductDTO(p)));

		List<Services> searchServiceList = this.serviceRepository
				.findByServiceNameLikeAndIsDeletedFalseAndOrganization("%" + productServiceName + "%", organization);
		List<ServiceDTO> searchServiceDTOs = new ArrayList<>();
		searchServiceList.stream().forEach(s -> searchServiceDTOs.add(ServiceMapper.INSTANCE.serviceTOServiceDTO(s)));

		Map<String, Object> productsAndServicesDTo = new HashMap<String, Object>();
		productsAndServicesDTo.put("products", searchProductDTOs);
		productsAndServicesDTo.put("services", searchServiceDTOs);

		return productsAndServicesDTo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.CommonService#getLoggedInUserOrganization(java.
	 * security.Principal)
	 */
	@Override
	public Organization getLoggedInUserOrganization(Principal principal) {
		log.info("Enter inside CommonServiceImpl.getLoggedInUserOrganization() method.");
		if (principal != null) {
			String loggedInUserName = principal.getName();
			Optional<User> loggedInUserOpt = this.userRepository.findByUsername(loggedInUserName);
			if (loggedInUserOpt.isPresent()) {
				User user = loggedInUserOpt.get();
				if (user.getRole().getName().equals("ORGANIZATION")) {
					if (user.getOrganizationDetails() != null) {
						log.info("Logged in user is an organization.");
						return user.getOrganizationDetails();
					}
				} else {
					if (user.getStaff() != null) {
						return user.getStaff().getOrganization();
					}
				}
			}
		}
		return null;
	}

}
