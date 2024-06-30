/* 
 * ===========================================================================
 * File Name JobServiceImpl.java
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
 * $Log: JobServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.JobDTO;
import com.service.vix.dto.Message;
import com.service.vix.dto.UserDTO;
import com.service.vix.enums.JobStatus;
import com.service.vix.mapper.CustomerMapper;
import com.service.vix.mapper.JobMapper;
import com.service.vix.mapper.UserMapper;
import com.service.vix.models.Customer;
import com.service.vix.models.Job;
import com.service.vix.models.Option;
import com.service.vix.models.OptionProduct;
import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.Services;
import com.service.vix.models.User;
import com.service.vix.repositories.CustomerRepository;
import com.service.vix.repositories.JobRepository;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.repositories.SalesPersonsRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.CustomerService;
import com.service.vix.service.JobService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as implementation class of JobService
 */
@Service
@Slf4j
public class JobServiceImpl implements JobService {

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private SalesPersonsRepository salesPersonsRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Environment env;

	@Autowired
	private CommonService commonService;
	@Autowired
	private CustomerService customerService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.JobService#saveJob(com.service.vix.dto.JobDTO,
	 * jakarta.servlet.http.HttpSession, java.security.Principal)
	 */
	@Override
	public CommonResponse<JobDTO> saveJob(JobDTO jobDTO, HttpSession httpSession, Principal principal) {
		log.info("Enter inside JobServiceImpl.saveJob() method.");
		CommonResponse<JobDTO> result = new CommonResponse<JobDTO>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		log.info("Going to get customer object from job DTO object");
		Customer customer = this.customerRepository.findByCustomerName(jobDTO.getCustomerName()).get();
		log.info("Going to get estimate object from estimate DTO object");
		Job job = JobMapper.INSTANCE.jobDTOToJob(jobDTO);
		job.setCustomerId(customer.getId());
		job.setOrganization(organization);
		Job savedJob = new Job();
		JobDTO savedJobDTO = new JobDTO();
		String msg = "";
		try {
			log.info("Going to save job.");
			savedJob = this.jobRepository.save(job);
			savedJobDTO = JobMapper.INSTANCE.jobToJobDTO(savedJob);
			savedJobDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			msg = env.getProperty("job.save.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(savedJobDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Job.");
			msg = env.getProperty("job.save.fail") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(savedJobDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.JobService#extractJobFormObject(jakarta.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	public JobDTO extractJobFormObject(HttpServletRequest httpServletRequest) {
		log.info("Enter inside JobServiceImpl.extractJobFormObject() method.");
		JobDTO jobDTO = new JobDTO();
		log.info("Going to get customer details.");
		String customerName = httpServletRequest.getParameter("customerName");
		log.info("Going to get job category details.");
		String jobCategoryName = httpServletRequest.getParameter("jobCategoryName");
		log.info("Going to get sales data details.");
		String jobStatusStr = httpServletRequest.getParameter("jobStatus");
		JobStatus jobStatus = JobStatus.UNSCHEDULED;
		try {
			jobStatus = JobStatus.valueOf(jobStatusStr);
		} catch (IllegalArgumentException e) {
			log.error("Job Status not found by given Job Status String. So Job Status will be set by UNSCHEDULED.");
		}
		int counter = 1;
		byte jobTableCounter = 1;
		// Get values for locationNickName form
		log.info("Going to calculate job Table values from form.");
		List<Option> options = new ArrayList<Option>();
		while (true) {
			log.info("Going to get values from " + jobTableCounter + " jobTable.");
			Option option = new Option();
			// estimate-table1EstimateTotal
			String jobTotal = httpServletRequest.getParameter("job-table" + jobTableCounter + "JobTotal");
			String jobCost = httpServletRequest.getParameter("job-table" + jobTableCounter + "JobCost");
			String grossProfit = httpServletRequest.getParameter("job-table" + jobTableCounter + "GrossProfit");
			String optionId = httpServletRequest.getParameter("job-table" + jobTableCounter + "JobOptionId");
			if (optionId != null && !optionId.equals(""))
				option.setId(Long.parseLong(optionId));
			if (jobTotal != null && !jobTotal.equals(""))
				option.setJobTotal(Float.valueOf(jobTotal));
			if (jobCost != null && !jobCost.equals(""))
				option.setJobCost(Float.valueOf(jobCost));
			if (grossProfit != null && !grossProfit.equals(""))
				option.setGrossProfit(Float.valueOf(grossProfit));

			String jobTable = httpServletRequest.getParameter("job-table" + jobTableCounter + "-input");
			if (jobTable != null) {
				int optProductCounter = 1;
				List<OptionProduct> optionProducts = new ArrayList<>();
				while (true) {
					String productName = httpServletRequest
							.getParameter("job-table" + jobTableCounter + "ProductName" + optProductCounter);
					String serviceName = httpServletRequest
							.getParameter("job-table" + jobTableCounter + "ServiceName" + optProductCounter);

					if (productName != null && !productName.equals("")
							&& this.productRepository.findByProductName(productName).isPresent()) {
						OptionProduct optionProduct = new OptionProduct();
						String productQuantity = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Quantity" + optProductCounter);
						String productRate = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Rate" + optProductCounter);
						productRate = productRate.replace("$", "");
						productRate = productRate.replace(",", "");
						String productTotal = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Total" + optProductCounter);
						productTotal = productTotal.replace("$", "");
						productTotal = productTotal.replace(",", "");
						String productDiscription = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Description" + optProductCounter);
						String productCostStr = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "AverageCost" + optProductCounter);
						productCostStr = productCostStr.replace("$", "");
						productCostStr = productCostStr.replace(",", "");
						Float quantity = 0f;
						if (productQuantity != null)
							quantity = Float.valueOf(productQuantity);
						Float total = 0f;
						if (productTotal != null)
							total = Float.valueOf(productTotal);
						Float rate = 0f;
						if (productRate != null)
							rate = Float.valueOf(productRate);
						Float productCost = 0f;
						if (productCostStr != null)
							productCost = Float.valueOf(productCostStr);

						optionProduct.setQuantity(quantity);
						optionProduct.setTotal(total);
						optionProduct.setRate(rate);
						optionProduct.setProductName(productName);
						optionProduct.setProductDiscription(productDiscription);
						optionProduct.setProductCost(productCost);
						optionProducts.add(optionProduct);
					} else if (serviceName != null && !serviceName.equals("")
							&& this.serviceRepository.findByServiceName(serviceName).isPresent()) {
						OptionProduct optionService = new OptionProduct();
						String serviceQuantity = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Quantity" + optProductCounter);
						String serviceTotal = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Total" + optProductCounter);
						serviceTotal = serviceTotal.replace("$", "");
						serviceTotal = serviceTotal.replace(",", "");
						String serviceRate = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Rate" + optProductCounter);
						serviceRate = serviceRate.replace("$", "");
						serviceRate = serviceRate.replace(",", "");
						String serviceDiscription = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "Description" + optProductCounter);
						String serviceCostStr = httpServletRequest
								.getParameter("job-table" + jobTableCounter + "AverageCost" + optProductCounter);
						serviceCostStr = serviceCostStr.replace("$", "");
						serviceCostStr = serviceCostStr.replace(",", "");
						Float quantity = 0f;
						if (serviceQuantity != null)
							quantity = Float.valueOf(serviceQuantity);
						Float total = 0f;
						if (serviceTotal != null)
							total = Float.valueOf(serviceTotal);
						Float rate = 0f;
						if (serviceRate != null)
							rate = Float.valueOf(serviceRate);
						Float serviceCost = 0f;
						if (serviceCostStr != null)
							serviceCost = Float.valueOf(serviceCostStr);

						optionService.setQuantity(quantity);
						optionService.setTotal(total);
						optionService.setRate(rate);
						optionService.setServiceName(serviceName);
						optionService.setServiceDiscription(serviceDiscription);
						optionService.setServiceCost(serviceCost);
						optionProducts.add(optionService);
					} else if (productName == null && serviceName == null) {
						log.info("No value present in " + (optProductCounter + 1) + " Product Name field.");
						break;
					}
					optProductCounter++;
				}
				option.setOptionProducts(optionProducts);
				options.add(option);
			} else {
				log.info("No value present in " + (counter + 1) + " estimate table.");
				break;
			}
			jobTableCounter++;
		}
		jobDTO.setCustomerName(customerName);
		jobDTO.setJobCategoryName(jobCategoryName);
		jobDTO.setJobStatus(jobStatus);
		jobDTO.setOptions(options);
		return jobDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.JobService#getJobsById(java.lang.Long)
	 */
	@Override
	public CommonResponse<JobDTO> getJobsById(Long jobId) {
		log.info("Enter inside JobServiceImpl.getJobsById() method.");
		CommonResponse<JobDTO> response = new CommonResponse<JobDTO>();
		if (jobId != null && jobId > 0) {
			JobDTO jobDTO = new JobDTO();
			Optional<Job> jobOpt = this.jobRepository.findById(jobId);
			if (jobOpt.isPresent()) {
				Job job = jobOpt.get();
				job.getOptions().stream().forEach(op -> op.getOptionProducts().stream().forEach(opPro -> {
					if (opPro.getProductName() != null && !opPro.getProductName().equals("")) {
						Product product = new Product();
						product.setProductName(opPro.getProductName());
						product.setAverageCost(opPro.getProductCost());
						product.setDiscription(opPro.getProductDiscription());
						product.setRegularPrice(opPro.getRate());
						opPro.setProduct(product);
					} else {
						Services services = new Services();
						services.setServiceName(opPro.getServiceName());
						services.setInternalCost(opPro.getServiceCost());
						services.setDiscription(opPro.getServiceDiscription());
						services.setRegularPrice(opPro.getRate());
						opPro.setServices(services);
					}
				}));
				jobDTO = JobMapper.INSTANCE.jobToJobDTO(job);
				Customer customer = this.customerRepository.findById(job.getCustomerId()).get();
				CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customer);
				if (customer.getSalesPersonId() != null)
					customerDTO.setSalesPerson(this.salesPersonsRepository.findbyId(customer.getSalesPersonId()).get());
				jobDTO.setCustomerDTO(customerDTO);
			}
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(jobDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
		String msg = env.getProperty("record.not.found");
		log.info(msg);
		response.setMessage(msg);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.JobService#removeJob(java.lang.Long,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<Boolean> removeJob(Long jobId, HttpSession httpSession) {
		log.info("Enter inside JobServiceImpl.removeJob() method.");
		CommonResponse<Boolean> response = new CommonResponse<Boolean>();
		if (jobId != null && jobId > 0) {
			String msg = "";
			Optional<Job> jobOpt = this.jobRepository.findById(jobId);
			if (jobOpt.isPresent()) {
				Job job = jobOpt.get();
				try {
					job.setIsDeleted(true);
					jobRepository.save(job);
				} catch (Exception e) {
					String errorMessage = "Failed to delete job with ID: " + jobId;
					log.error(errorMessage, e);
				}
				msg = env.getProperty("job.delete.success");
			} else
				msg = env.getProperty("job.delete.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("job.delete.fail");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
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
	 * @see com.service.vix.service.JobService#updateJob(com.service.vix.dto.JobDTO,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<JobDTO> updateJob(JobDTO jobDTO, HttpSession httpSession) {
		log.info("Enter inside JobServiceImpl.updateJob() method.");
		CommonResponse<JobDTO> result = new CommonResponse<JobDTO>();
		Job DBJob = this.jobRepository.findById(jobDTO.getId()).get();
		DBJob.setId(jobDTO.getId());
		DBJob.setTechnicianId(jobDTO.getTechnicianId());
//		if (jobDTO.getSalesPersonId() != null) {
//			SalesPerson salesPerson = this.salesPersonsRepository.findbyId(jobDTO.getSalesPersonId()).get();
//		}
		Job savedJob = new Job();
		JobDTO savedJobDTO = new JobDTO();
		try {
			log.info("Going to save job.");
			savedJob = this.jobRepository.save(DBJob);
			savedJobDTO = JobMapper.INSTANCE.jobToJobDTO(savedJob);
			savedJobDTO.setCustomerDTO(CustomerMapper.INSTANCE
					.customerToCustomerDTO(this.customerRepository.findById(savedJob.getCustomerId()).get()));
			String msg = env.getProperty("job.update.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(savedJobDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Job.");
			String msg = env.getProperty("job.update.fail") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(savedJobDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.JobService#getOrganizationJobs(java.security.
	 * Principal)
	 */
	@Override
	public CommonResponse<List<JobDTO>> getOrganizationJobs(Principal principal) {
		log.info("Enter inside JobServiceImpl.getOrganizationJobs() method.");
		CommonResponse<List<JobDTO>> response = new CommonResponse<>();
		List<Job> job = this.jobRepository.findByCreatedByAndIsDeletedFalse(principal.getName());
		List<JobDTO> jobDTOs = new ArrayList<JobDTO>();
		job.forEach(e -> {
			JobDTO jobDTO = JobMapper.INSTANCE.jobToJobDTO(e);
			if (e.getCustomerId() != null) {
				CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
				customerDTO.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
				jobDTO.setCustomerDTO(customerDTO);
				jobDTOs.add(jobDTO);
			}
			List<UserDTO> technicians = new ArrayList<UserDTO>();
			e.getTechnicianId().stream().forEach(ti -> {
				if (ti != null) {
					Optional<User> userOpt = this.userRepository.findById(ti);
					if (userOpt.isPresent())
						technicians.add(UserMapper.INSTANCE.userToUserDTO(userOpt.get()));
				}
			});
			jobDTO.setTechnicians(technicians);

		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(jobDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.JobService#getUserJobs(java.security.Principal)
	 */
	@Override
	public CommonResponse<List<JobDTO>> getUserJobs(Principal principal) {
		log.info("Enter inside JobServiceImpl.getUserJobs() methid.");
		CommonResponse<List<JobDTO>> response = new CommonResponse<List<JobDTO>>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		Optional<List<Job>> optUserOrgJobs = this.jobRepository.findByOrganizationAndIsDeletedFalse(organization);
		List<JobDTO> jobDTOs = new ArrayList<JobDTO>();
		if (!optUserOrgJobs.isEmpty()) {
			List<Job> userJobs = optUserOrgJobs.get();
			userJobs.stream().forEach(e -> {
				JobDTO jobDTO = JobMapper.INSTANCE.jobToJobDTO(e);
				if (e.getCustomerId() != null) {
					CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
					customerDTO
							.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
					jobDTO.setCustomerDTO(customerDTO);
					jobDTOs.add(jobDTO);
				}

				List<UserDTO> technicians = new ArrayList<UserDTO>();
				e.getTechnicianId().stream().forEach(ti -> {
					if (ti != null) {
						Optional<User> userOpt = this.userRepository.findById(ti);
						if (userOpt.isPresent())
							technicians.add(UserMapper.INSTANCE.userToUserDTO(userOpt.get()));
					}
				});
				jobDTO.setTechnicians(technicians);

			});
		}
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(jobDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}
}
