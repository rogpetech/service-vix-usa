/* 
 * ===========================================================================
 * File Name ServicesServiceImpl.java
 * 
 * Created on Jun 24, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: ServicesServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ImageUtility;
import com.service.vix.dto.Message;
import com.service.vix.dto.ServiceDTO;
import com.service.vix.enums.ImageUploadDirectory;
import com.service.vix.mapper.ServiceMapper;
import com.service.vix.models.Estimate;
import com.service.vix.models.Organization;
import com.service.vix.models.ServiceCategory;
import com.service.vix.models.Services;
import com.service.vix.repositories.EstimateRepository;
import com.service.vix.repositories.ServiceCategoryRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.ServicesService;
import com.service.vix.utility.SaveImage;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServicesServiceImpl implements ServicesService {

	@Autowired
	private ServiceCategoryRepository serviceCategoryRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private EstimateRepository estimateRepository;
	@Autowired
	private ServiceMapper serviceMapper;
	@Autowired
	private CommonService commonService;

	@Autowired
	private SaveImage saveImage;
	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ServicesService#saveService(com.service.vix.dto.
	 * ServiceDTO, org.springframework.web.multipart.MultipartFile,
	 * jakarta.servlet.http.HttpSession, java.security.Principal)
	 */
	@Override
	public CommonResponse<ServiceDTO> saveService(ServiceDTO serviceDTO, MultipartFile file, HttpSession httpSession,
			Principal principal) {
		log.info("Enter inside ServicesServiceImpl.saveProducts() method. ");
		CommonResponse<ServiceDTO> response = new CommonResponse<ServiceDTO>();
		String msg = "";
		boolean existByServiceName = serviceRepository.existsByServiceName(serviceDTO.getServiceName());
		log.info("Service Exists By name | " + existByServiceName);
		if (existByServiceName) {
			msg = env.getProperty("service.name.already.exists");
			response.setMessage(msg);
			response.setData(null);
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			httpSession.setAttribute("message", new Message(msg, "danger"));
			return response;
		} else {
			log.info("Going to save service");
			Optional<ServiceCategory> serviceCategoryOpt = serviceCategoryRepository
					.findById(serviceDTO.getServiceCategoryId());
			Services service = this.serviceMapper.serviceDTOToService(serviceDTO);
			service.setServiceCategory(serviceCategoryOpt.get());
			ImageUtility imageUtility = new ImageUtility();
			imageUtility.setFile(file);
			imageUtility.setFileName(file.getOriginalFilename());
			String serviceImage = serviceDTO.getServiceName() + "-" + LocalDateTime.now().toString().substring(0, 10)
					+ ".jpg";
			imageUtility.setUniqueIdentifier(serviceImage);
			imageUtility.setImageUploadDirectory(ImageUploadDirectory.SERVICE);
			try {

				Organization organization = this.commonService.getLoggedInUserOrganization(principal);
				service.setOrganization(organization);

				msg = env.getProperty("service.add.success");
				this.saveImage.saveImage(imageUtility);
				this.serviceMapper.serviceTOServiceDTO(this.serviceRepository.save(service));
				httpSession.setAttribute("message", new Message(msg, "success"));
				response.setMessage(msg);
				response.setData(null);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
			} catch (Exception e) {
				log.error("Exception occuring while saving service.." + e.getMessage());
				msg = env.getProperty("something.went.wrong");
				response.setMessage(msg);
				response.setData(null);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				httpSession.setAttribute("message", new Message(msg, "danger"));
			}
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ServicesService#getServices(java.security.Principal)
	 */
	@Override
	public CommonResponse<List<ServiceDTO>> getServices(Principal principal) {
		log.info("Enter inside ServicesServiceImpl.getServices() method.");
		CommonResponse<List<ServiceDTO>> response = new CommonResponse<List<ServiceDTO>>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Services> services = this.serviceRepository.findAllByIsDeletedFalseAndOrganization(organization);
		List<ServiceDTO> serviceDTOs = new ArrayList<ServiceDTO>();
		services.forEach(s -> {
			ServiceDTO service = ServiceMapper.INSTANCE.serviceTOServiceDTO(s);
			serviceDTOs.add(service);
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(serviceDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ServicesService#getServiceById(java.lang.Long)
	 */
	@Override
	public CommonResponse<ServiceDTO> getServiceById(Long serviceId) {
		log.info("Enter inside ServicesServiceImpl.getServiceById() method. ");
		CommonResponse<ServiceDTO> response = new CommonResponse<ServiceDTO>();
		if (serviceId != null && serviceId > 0) {
			ServiceDTO serviceDto = new ServiceDTO();
			Optional<Services> serviceDtoOpt = serviceRepository.findById(serviceId);
			if (serviceDtoOpt.isPresent())
				serviceDto = ServiceMapper.INSTANCE.serviceTOServiceDTO(serviceDtoOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(serviceDto);
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
	 * @see com.service.vix.service.ServicesService#removeService(java.lang.Long)
	 */
	@Override
	public CommonResponse<Boolean> removeService(Long serviceId) {
		log.info("Enter inside ServicesServiceImpl.removeService() method. ");
		CommonResponse<Boolean> response = new CommonResponse<Boolean>();
		if (serviceId != null && serviceId > 0) {
			String msg = "";
			Optional<Services> serviceOpt = serviceRepository.findById(serviceId);
			if (serviceOpt.isPresent()) {
				Services service = serviceOpt.get();
				Optional<Estimate> estimateOpt = this.estimateRepository.findByProductName(service.getServiceName());
				if (estimateOpt.isEmpty()) {
					service.setIsDeleted(true);
					serviceRepository.save(service);
					msg = env.getProperty("service.delete.success");
				} else {
					msg = env.getProperty("service.associated.product");
					response.setMessage(msg);
					response.setData(false);
					response.setResult(false);
					response.setStatus(HttpStatus.ALREADY_REPORTED.value());
					return response;
				}

			} else {
				msg = env.getProperty("record.delete.success") + " 0.";
			}
			log.info(msg);
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}

		else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
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
	 * @see
	 * com.service.vix.service.ServicesService#updateService(com.service.vix.dto.
	 * ServiceDTO)
	 */
	@Override
	public CommonResponse<ServiceDTO> updateService(ServiceDTO serviceDTO) {
		log.info("Enter inside ServicesServiceImpl.updateService() method. ");
		CommonResponse<ServiceDTO> response = new CommonResponse<ServiceDTO>();
		Long serviceId = serviceDTO.getId();
		if (serviceId != null && serviceId > 0) {
			log.info("Service  found by given Service  details.");
			Optional<Services> serviceOpt = this.serviceRepository.findById(serviceId);
			if (serviceOpt.isPresent()) {

				Optional<ServiceCategory> serviceCategorie = this.serviceCategoryRepository
						.findById(serviceDTO.getServiceCategoryId());
				Services service = ServiceMapper.INSTANCE.serviceDTOToService(serviceDTO);

				if (serviceCategorie.isPresent()) {
					service.setServiceCategory(serviceCategorie.get());
				}

				Services DBServices = serviceOpt.get();
				Long DBServiceId = DBServices.getId();
				service.setId(DBServiceId);
				service.setOrganization(DBServices.getOrganization());
				Services savedService = this.serviceRepository.save(service);
				ServiceDTO savedServiceDTO = ServiceMapper.INSTANCE.serviceTOServiceDTO(savedService);
				String msg = env.getProperty("service.update.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedServiceDTO);
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
	 * @see com.service.vix.service.ServicesService#searchService(java.lang.String)
	 */
	@Override
	public List<ServiceDTO> searchService(String serviceName, Principal principal) {
		log.info("Enter inside ServicesServiceImpl.searchService() mwthod.");
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Services> searchServiceList = this.serviceRepository
				.findByServiceNameLikeAndIsDeletedFalseAndOrganization("%" + serviceName + "%", organization);
		List<ServiceDTO> searchServiceDTOs = new ArrayList<>();
		searchServiceList.stream().forEach(p -> searchServiceDTOs.add(this.serviceMapper.serviceTOServiceDTO(p)));
		return searchServiceDTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ServicesService#getServiceByServiceName(java.lang.
	 * String)
	 */
	@Override
	public CommonResponse<ServiceDTO> getServiceByServiceName(String serviceName) {
		log.info("Enter inside ServicesServiceImpl.existsByServiceName() method. ");
		CommonResponse<ServiceDTO> response = new CommonResponse<ServiceDTO>();
		if (serviceName != null) {
			ServiceDTO serviceDTO = new ServiceDTO();
			Optional<Services> serviceOpt = this.serviceRepository.findByServiceName(serviceName);
			if (serviceOpt.isPresent())
				serviceDTO = ServiceMapper.INSTANCE.serviceTOServiceDTO(serviceOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(serviceDTO);
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
}
