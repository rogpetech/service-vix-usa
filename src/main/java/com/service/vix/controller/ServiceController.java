/* 
 * ===========================================================================
 * File Name ServiceController.java
 * 
 * Created on Jun 26, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: ServiceController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.dto.ServiceDTO;
import com.service.vix.service.ServiceCategoryService;
import com.service.vix.service.ServicesService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as controller class that handle all the request for
 * service pages
 */
@Controller
@Slf4j
@RequestMapping("/service")
public class ServiceController extends BaseController {

	@Autowired
	private ServicesService serviceService;

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	/**
	 * This method is used to open add service page
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 26, 2023
	 * @return String
	 * @param model
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add")
	public String addService(Model model,Principal principal) {
		log.info("Enter inside ServiceController.addService() method.");
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal).getData();
		model.addAttribute("serviceCategories", serviceCategories);
		return "service/add-service";
	}

	/**
	 * This method is used to save service
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 27, 2023
	 * @return String
	 * @param serviceDTO
	 * @param categoryImage
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add")
	public String processAddService(@ModelAttribute ServiceDTO serviceDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession, Principal principal) {
		log.info("Enter inside ServiceController.processAddService() method.");
		this.serviceService.saveService(serviceDTO, categoryImage, httpSession, principal);
		return "redirect:/product/products";
	}

	/**
	 * This method is used to save service
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 27, 2023
	 * @return Boolean
	 * @param serviceDTO
	 * @param categoryImage
	 * @param httpSession
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@ResponseBody
	@PostMapping("/process-popup-addService")
	public Boolean processAddServicePopupForm(@ModelAttribute ServiceDTO serviceDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession, Principal principal) {
		log.info("Enter inside ServiceController.processAddService() method.");
		if (categoryImage != null)
			serviceDTO.setServiceImage(categoryImage.getOriginalFilename());
		this.serviceService.saveService(serviceDTO, categoryImage, httpSession, principal);
		return true;
	}

	/**
	 * This method is used to show service category listing page
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 4, 2023
	 * @return String
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/services")
	public String showProducts(Model model, Principal principal) {
		log.info("Enter inside ServiceIdCategoryController.showService() method.");
		List<ServiceDTO> services = this.serviceService.getServices(principal).getData();
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal).getData();
		model.addAttribute("services", services);
		model.addAttribute("serviceCategories", serviceCategories);
		return "service/serviceList";
	}

	/**
	 * This method is used to open edit service page
	 * 
	 * @author hemantr
	 * @date Jul 6, 2023
	 * @param serviceId
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{serviceId}")
	public String updateService(@PathVariable Long serviceId, Model model,Principal principal) {
		log.info("Enter inside ServiceIdCategoryController.searchServiceIdByProductName() method.");
		CommonResponse<ServiceDTO> serviceById = this.serviceService.getServiceById(serviceId);
		ServiceDTO data = new ServiceDTO();
		if (serviceById.getResult() == true) {
			data = serviceById.getData();
			data.setServiceCategoryId(data.getServiceCategory().getId());
		}
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal).getData();
		model.addAttribute("serviceCategories", serviceCategories);
		model.addAttribute("serviceDto", data);
		return "service/edit-service";
	}

	/**
	 * This method is used to remove service from service list
	 * 
	 * @author hemantr
	 * @date Jul 6, 2023
	 * @param servicetDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-update")
	public String processUpdateServicet(@ModelAttribute ServiceDTO servicetDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession) {
		log.info("Enter inside ServiceIdCategoryController.processUpdateService() method.");
		servicetDTO.setServiceImage(categoryImage.getOriginalFilename()); //
		this.serviceService.updateService(servicetDTO);
		return "redirect:/product/products";
	}

	/**
	 * This method is used to remove service
	 * 
	 * @author hemantr
	 * @date Jul 6, 2023
	 * @param productId
	 * @return
	 */
	@GetMapping("/remove/{serviceId}")
	public String deleteServiceId(@PathVariable Long serviceId, HttpSession httpSession) {
		log.info("Enter inside ServiceIdCategoryController.deleteProduct() method.");
		CommonResponse<Boolean> removeService = this.serviceService.removeService(serviceId);
		if (!removeService.getResult() && !removeService.getData()) {
			httpSession.setAttribute("message", new Message(removeService.getMessage(), "danger"));
		} else if (removeService.getData() && removeService.getResult()) {
			httpSession.setAttribute("message", new Message(removeService.getMessage(), "success"));
		}
		return "redirect:/product/products";
	}

	/**
	 * This method is used to search Service by name
	 * 
	 * @author hemantr
	 * @date Jul 26, 2023
	 * @param serviceName
	 * @return
	 */
	@ResponseBody
	@GetMapping("/search/{serviceName}")
	public List<ServiceDTO> searchProductByProductName(@PathVariable String serviceName, Principal principal) {
		return this.serviceService.searchService(serviceName, principal);
	}

}
