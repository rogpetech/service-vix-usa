/* 
 * ===========================================================================
 * File Name JobController.java
 * 
 * Created on Aug 5, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: JobController.java,v $
 * ===========================================================================
 */
package com.service.vix.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.dto.JobDTO;
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.dto.RoleDTO;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.dto.StaffDTO;
import com.service.vix.enums.EstimateStatus;
import com.service.vix.enums.JobStatus;
import com.service.vix.enums.Permissions;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.CustomerService;
import com.service.vix.service.JobService;
import com.service.vix.service.ProductCategoryService;
import com.service.vix.service.RoleService;
import com.service.vix.service.SalesPersonService;
import com.service.vix.service.ServiceCategoryService;
import com.service.vix.service.StaffService;
import com.service.vix.utility.Currency;
import com.service.vix.utility.EstimateEmailBody;
import com.service.vix.utility.Industry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller is used as Job Controller
 */
@Controller
@Slf4j
@RequestMapping("/job")
public class JobController extends BaseController {

	@Autowired
	private JobService jobService;
	@Autowired
	private CustomerService customerService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ServiceCategoryService serviceCategoryService;
	@Autowired
	private SalesPersonService salesPersonService;

	@Value("${spring.mail.username}")
	private String fromEmailAddress;
	@Value("${estimate.subject}")
	private String estimateEmailSubject;

	@Autowired
	private RoleService roleService;

	@Value("${role.organization}")
	private String organizationRole;

	@Autowired
	private UserRepository userRepository;

	/**
	 * This method is used to open add-job page
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param model
	 * @param httpServletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add")
	public String addJob(Model model, HttpServletRequest httpServletRequest, Principal principal) {
		log.info("Enter inside JobController.addJob()mthods.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal)
				.getData();
		model.addAttribute("currencies", Currency.currencies);
		model.addAttribute("industries", Industry.industries);
		model.addAttribute("serviceCategories", serviceCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("jobStatus", JobStatus.values());
		model.addAttribute("job", new JobDTO());
		return "/job/add-job";
	}

	/**
	 * This method is used to add job
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add")
	public String processAddJob(HttpSession httpSession, HttpServletRequest servletRequest, Principal principal) {
		log.info("Enter inside JobController.processAddJob() method.");
		JobDTO extractJobFormObject = this.jobService.extractJobFormObject(servletRequest);
		this.jobService.saveJob(extractJobFormObject, httpSession, principal).getData();
		return "redirect:/job/jobs";
	}

	/**
	 * This method is used to show job listing page
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/jobs")
	public String showJobs(Model model, Principal principal) {
		List<JobDTO> jobs = this.jobService.getOrganizationJobs(principal).getData();
		List<JobDTO> userJobs = this.jobService.getUserJobs(principal).getData();
		model.addAttribute("jobs", jobs);
		model.addAttribute("userJobs", userJobs);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		return "job/jobs";
	}

	/**
	 * This method is used to view job
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param operationType
	 * @param estimateId
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/operation/{operationType}/{jobId}")
	public String viewJob(@PathVariable String operationType, @PathVariable Long jobId, Model model,
			Principal principal) {
		log.info("Enter inside JobController.viewJob() method.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		JobDTO jobDTO = this.jobService.getJobsById(jobId).getData();

		List<Long> technicianId = jobDTO.getTechnicianId();
		List<String> technicianNames = this.userRepository.findUserNamesByIdIn(technicianId);
		Boolean requestForView = false;
		if (operationType.equals("view"))
			requestForView = true;
		model.addAttribute("requestForView", requestForView);
		model.addAttribute("technicianNames", technicianNames);
		model.addAttribute("fromEmailAddress", fromEmailAddress);
		model.addAttribute("estimateEmailSubject", estimateEmailSubject);
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		model.addAttribute("bodyContent", EstimateEmailBody.getBody(jobDTO.getCustomerDTO().getCustomerName()));
		model.addAttribute("options", jobDTO.getOptions());
		model.addAttribute("jobStatus", JobStatus.values());
		if (jobId != null && jobId > 0)
			model.addAttribute("job", jobDTO);
		return "job/view-job";
	}

	/**
	 * This method is used to remove estimate
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param jobId
	 * @param model
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	@GetMapping("/remove/{jobId}")
	public String deleteJob(@PathVariable Long jobId, Model model, HttpSession httpSession) {
		log.info("Enter inside JobController.deleteJob() method.");
		this.jobService.removeJob(jobId, httpSession);
		return "redirect:/job/jobs";
	}

	/**
	 * This method is used to open update estimate page
	 * 
	 * @author ritiks
	 * @date Jul 26, 2023
	 * @return String
	 * @param estimateId
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/update/{jobId}")
	public String updateJob(@PathVariable Long jobId, Model model, Principal principal) {
		log.info("Enter inside JobController.updateJob() method.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();

		JobDTO jobDTO = this.jobService.getJobsById(jobId).getData();
		Boolean requestForView = true;
		List<RoleDTO> roles = this.roleService.getRoles().getData();
		roles.removeIf(rd -> (rd.getName().equals(organizationRole)));
		model.addAttribute("roles", roles);
		model.addAttribute("permissions", Permissions.getCapitalisePermissions());
		List<StaffDTO> staffs = this.staffService.getAllStaff(principal).getData();
		staffs.removeIf(staff -> (staff.getUserDTO().getUsername().equals(principal.getName())));
		model.addAttribute("staffs", staffs);
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("customerNames", customerName);
		model.addAttribute("jobStatus", JobStatus.values());
		model.addAttribute("requestForView", requestForView);
		model.addAttribute("fromEmailAddress", fromEmailAddress);
		model.addAttribute("estimateEmailSubject", estimateEmailSubject);
		model.addAttribute("bodyContent", EstimateEmailBody.getBody(jobDTO.getCustomerDTO().getCustomerName()));
		model.addAttribute("options", jobDTO.getOptions());
		if (jobId != null && jobId > 0)
			model.addAttribute("job", jobDTO);
		return "job/edit-job";
	}

	/**
	 * This method is used to update estimate
	 * 
	 * @author ritiks
	 * @date Nov 2, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-update")
	public String processUpdateEstimate(@ModelAttribute JobDTO jobDTO, HttpSession httpSession,
			HttpServletRequest servletRequest) {
		log.info("Enter inside JobController.processUpdateEstimate() method.");
		this.jobService.updateJob(jobDTO, httpSession);
		return "redirect:/job/jobs";
	}

}
