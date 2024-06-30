/* 
 * ===========================================================================
 * File Name EstimateServiceImpl.java
 * 
 * Created on Jun 21, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: EstimateServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.EmailTemplateDTO;
import com.service.vix.dto.EstimateDTO;
import com.service.vix.dto.EstimateEmailDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.JobDTO;
import com.service.vix.dto.Message;
import com.service.vix.enums.EmailTemplateType;
import com.service.vix.enums.EstimateStatus;
import com.service.vix.enums.JobStatus;
import com.service.vix.mapper.CustomerMapper;
import com.service.vix.mapper.EstimateMapper;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Customer;
import com.service.vix.models.Email;
import com.service.vix.models.Estimate;
import com.service.vix.models.Option;
import com.service.vix.models.OptionProduct;
import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.Services;
import com.service.vix.models.StoredServiceLocation;
import com.service.vix.repositories.CustomerRepository;
import com.service.vix.repositories.EstimateRepository;
import com.service.vix.repositories.OptionRepository;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.repositories.SalesPersonsRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.CustomerService;
import com.service.vix.service.EmailService;
import com.service.vix.service.EmailTemplateService;
import com.service.vix.service.EstimateService;
import com.service.vix.service.JobService;
import com.service.vix.utility.PDFGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as implementation class for Define all the methods for
 * EstimateService
 */
@Service
@Slf4j
public class EstimateServiceImpl implements EstimateService {

	@Autowired
	private Environment env;

	@Autowired
	private EstimateRepository estimateRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private SalesPersonsRepository salesPersonsRepository;
	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private JobService jobService;
	@Autowired
	private EmailTemplateService emailTemplateService;

	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private PDFGenerator pdfGenerator;

	@Value("${admin.baseURL}")
	private String baseURL;
	@Value("${project.organization.upload-dir}")
	private String organizationLogoUploadDirectory;
	@Value("${image.base.path}")
	private String imageBasePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#saveEstimate(com.service.vix.dto.
	 * EstimateDTO, jakarta.servlet.http.HttpSession, java.security.Principal)
	 */
	@Override
	public CommonResponse<EstimateDTO> saveEstimate(EstimateDTO estimateDTO, HttpSession httpSession,
			Principal principal) {
		log.info("Enter inside EstimateServiceImpl.saveEstimate() method.");
		CommonResponse<EstimateDTO> result = new CommonResponse<EstimateDTO>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		log.info("Going to get customer object from estimate DTO object");
		Customer customer = this.customerRepository.findByCustomerName(estimateDTO.getCustomerName()).get();
		log.info("Going to get estimate object from estimate DTO object");
		Estimate estimate = EstimateMapper.INSTANCE.estimateDTOToEstimate(estimateDTO);
		estimate.setCustomerId(customer.getId());
		estimate.setOrganization(organization);
		Estimate savedEstimate = new Estimate();
		EstimateDTO savedEstimateDTO = new EstimateDTO();
		String msg = "";
		try {
			log.info("Going to save estimate.");
			savedEstimate = this.estimateRepository.save(estimate);
			savedEstimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(savedEstimate);
			savedEstimateDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			msg = env.getProperty("estimate.save.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(savedEstimateDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Estimate.");
			msg = env.getProperty("estimate.save.failed") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(savedEstimateDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#extractEstimateFormObject(jakarta.
	 * servlet.http.HttpServletRequest)
	 */
	@Override
	public EstimateDTO extractEstimateFormObject(HttpServletRequest httpServletRequest) {
		log.info("Enter inside EstimateServiceImpl.extractEstimateFormObject() method.");
		EstimateDTO estimateDTO = new EstimateDTO();
		log.info("Going to get customer details.");
		String customerName = httpServletRequest.getParameter("customerName");
		log.info("Going to get job category details.");
		String jobCategoryName = httpServletRequest.getParameter("jobCategoryName");
		log.info("Going to get sales data details.");
		String estimateStatusStr = httpServletRequest.getParameter("estimateStatus");
		EstimateStatus estimateStatus = EstimateStatus.ESTIMATE_REQUESTED;
		try {
			estimateStatus = EstimateStatus.valueOf(estimateStatusStr);
		} catch (IllegalArgumentException e) {
			log.error(
					"Estimate Status not found by given Estimate Status String. So Estimate Status will be set by ESTIMATE_REQUESTED.");
		}
		int counter = 1;
		byte estimateTableCounter = 1;
		// Get values for locationNickName form
		log.info("Going to calculate estimate Table values from form.");
		List<Option> options = new ArrayList<Option>();
		while (true) {
			log.info("Going to get values from " + estimateTableCounter + " estimateTable.");
			Option option = new Option();
			// estimate-table1EstimateTotal
			String estimateTotal = httpServletRequest
					.getParameter("estimate-table" + estimateTableCounter + "EstimateTotal");
			String jobCost = httpServletRequest.getParameter("estimate-table" + estimateTableCounter + "JobCost");
			String grossProfit = httpServletRequest
					.getParameter("estimate-table" + estimateTableCounter + "GrossProfit");
			String optionId = httpServletRequest
					.getParameter("estimate-table" + estimateTableCounter + "EstimateOptionId");
			if (optionId != null && !optionId.equals(""))
				option.setId(Long.parseLong(optionId));
			if (estimateTotal != null && !estimateTotal.equals(""))
				option.setEstimateTotal(Float.valueOf(estimateTotal));
			if (jobCost != null && !jobCost.equals(""))
				option.setJobCost(Float.valueOf(jobCost));
			if (grossProfit != null && !grossProfit.equals(""))
				option.setGrossProfit(Float.valueOf(grossProfit));

			String estimateTable = httpServletRequest.getParameter("estimate-table" + estimateTableCounter + "-input");
			if (estimateTable != null) {
				int optProductCounter = 1;
				List<OptionProduct> optionProducts = new ArrayList<>();
				while (true) {
					String productName = httpServletRequest
							.getParameter("estimate-table" + estimateTableCounter + "ProductName" + optProductCounter);
					String serviceName = httpServletRequest
							.getParameter("estimate-table" + estimateTableCounter + "ServiceName" + optProductCounter);

					if (productName != null && !productName.equals("")
							&& this.productRepository.findByProductName(productName).isPresent()) {
						OptionProduct optionProduct = new OptionProduct();
						String productQuantity = httpServletRequest
								.getParameter("estimate-table" + estimateTableCounter + "Quantity" + optProductCounter);
						String productRate = httpServletRequest
								.getParameter("estimate-table" + estimateTableCounter + "Rate" + optProductCounter);
						productRate = productRate.replace("$", "");
						productRate = productRate.replace(",", "");
						String productTotal = httpServletRequest
								.getParameter("estimate-table" + estimateTableCounter + "Total" + optProductCounter);
						productTotal = productTotal.replace("$", "");
						productTotal = productTotal.replace(",", "");
						String productDiscription = httpServletRequest.getParameter(
								"estimate-table" + estimateTableCounter + "Description" + optProductCounter);
						String productCostStr = httpServletRequest.getParameter(
								"estimate-table" + estimateTableCounter + "AverageCost" + optProductCounter);
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
								.getParameter("estimate-table" + estimateTableCounter + "Quantity" + optProductCounter);
						String serviceTotal = httpServletRequest
								.getParameter("estimate-table" + estimateTableCounter + "Total" + optProductCounter);
						serviceTotal = serviceTotal.replace("$", "");
						serviceTotal = serviceTotal.replace(",", "");
						String serviceRate = httpServletRequest
								.getParameter("estimate-table" + estimateTableCounter + "Rate" + optProductCounter);
						serviceRate = serviceRate.replace("$", "");
						serviceRate = serviceRate.replace(",", "");
						String serviceDiscription = httpServletRequest.getParameter(
								"estimate-table" + estimateTableCounter + "Description" + optProductCounter);
						String serviceCostStr = httpServletRequest.getParameter(
								"estimate-table" + estimateTableCounter + "AverageCost" + optProductCounter);
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
			estimateTableCounter++;
		}
		estimateDTO.setCustomerName(customerName);
		estimateDTO.setJobCategoryName(jobCategoryName);
		estimateDTO.setEstimateStatus(estimateStatus);
		estimateDTO.setOptions(options);
		return estimateDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EstimateService#getEstimatesById(java.lang.Long)
	 */
	@Override
	public CommonResponse<EstimateDTO> getEstimatesById(Long estimateId) {
		log.info("Enter inside EstimateServiceImpl.saveEstimate() method.");
		CommonResponse<EstimateDTO> response = new CommonResponse<EstimateDTO>();
		if (estimateId != null && estimateId > 0) {
			EstimateDTO estimateDTO = new EstimateDTO();
			Optional<Estimate> estimateOpt = this.estimateRepository.findById(estimateId);
			if (estimateOpt.isPresent()) {
				Estimate estimate = estimateOpt.get();
				estimate.getOptions().stream().forEach(op -> op.getOptionProducts().stream().forEach(opPro -> {
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
				estimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(estimate);
				Customer customer = this.customerRepository.findById(estimate.getCustomerId()).get();
				CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customer);
				if (customer.getSalesPersonId() != null)
					customerDTO.setSalesPerson(this.salesPersonsRepository.findbyId(customer.getSalesPersonId()).get());
				estimateDTO.setCustomerDTO(customerDTO);
			}
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(estimateDTO);
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
	 * @see
	 * com.service.vix.service.EstimateService#sendEstimateEmail(com.service.vix.dto
	 * .EstimateEmailDTO, jakarta.servlet.http.HttpServletResponse,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<Boolean> sendEstimateEmail(EstimateEmailDTO estimateEmailDTO, HttpServletResponse response,
			HttpSession httpSession) {
		CommonResponse<Boolean> commonResponse = new CommonResponse<>();
		List<Option> options = new ArrayList<>();
		estimateEmailDTO.getOptionIds().forEach(optId -> options.add(this.optionRepository.findById(optId).get()));
		options.stream().forEach(op -> {
			op.getOptionProducts().stream().forEach(opPro -> {
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
			});
		});
		Estimate estimate = this.estimateRepository.findById(estimateEmailDTO.getEstimateId()).get();
		estimate.setEstimateStatus(EstimateStatus.ESTIMATE_PROVIDED);
		this.estimateRepository.save(estimate);
		Customer customer = this.customerRepository.findById(estimate.getCustomerId()).get();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			// Prepare data for the PDF template
			Long optionIds = estimateEmailDTO.getOptionIds().get(0);

			Map<String, Object> data = new HashMap<>();
			Email email = customer.getEmails().get(0);
			String emailOrg = email.getEmail();
			ContactNumber contactNumber = customer.getContactNumbers().get(0);
			String number = contactNumber.getNumber();
			StoredServiceLocation storedServiceLocation = customer.getStoredServiceLocations().get(0);
			String locationNickName = storedServiceLocation.getLocationNickName();
			String address = storedServiceLocation.getAddress();
			String city = storedServiceLocation.getCity();
			String state = storedServiceLocation.getState();
			String unit = storedServiceLocation.getUnit();
			String orgName = estimate.getOrganization().getOrgName();
			String orgAddress = estimate.getOrganization().getOrgAddress();
			String orgEmail = estimate.getOrganization().getOrgEmail();
			String orgMobNum = estimate.getOrganization().getOrgMobNum();

			String[] orgLogoUploadDirArr = this.organizationLogoUploadDirectory.split("/");
			int orgLogoUploadDirArrLength = orgLogoUploadDirArr.length;
			String orgLogo = imageBasePath + orgLogoUploadDirArr[orgLogoUploadDirArrLength - 1] + "/"
					+ estimate.getOrganization().getOrgLogo();

			data.put("emailOrg", emailOrg);
			data.put("orgAddress", orgAddress);
			data.put("orgEmail", orgEmail);
			data.put("orgMobNum", orgMobNum);
			data.put("locationNickName", locationNickName);
			data.put("address", address);
			data.put("city", city);
			data.put("state", state);
			data.put("unit", unit);
			data.put("number", number);
			data.put("storedServiceLocation", storedServiceLocation);
			data.put("optionIds", optionIds);
			data.put("baseURL", baseURL);
			data.put("orgName", orgName);
			data.put("options", options);
			data.put("estimateId", estimateEmailDTO.getEstimateId());
			data.put("estimateDate", estimate.getCreatedAt().format(formatter));
			data.put("customerName", customer.getCustomerName());
			data.put("itemPrice", estimateEmailDTO.getItemPrice());
			data.put("itemTotal", estimateEmailDTO.getItemTotal());
			data.put("itemQuantity", estimateEmailDTO.getItemQuantity());
			data.put("grandTotal", estimateEmailDTO.getGrandTotal());
			byte[] pdfBytes = null;
			String body = "";
			String emailSubject = "";
			// Generate the PDF
			try {
				Context context = new Context();
				if (data != null) {
					for (Map.Entry<String, Object> pair : data.entrySet())
						context.setVariable(pair.getKey(), pair.getValue());
				}
				context.setVariable("isEmailTemplate", false);
				pdfBytes = pdfGenerator.generatePDF("pdf_template", context);
				context.removeVariable("isEmailTemplate");
				context.setVariable("isEmailTemplate", true);

				String estimateMessage = "";
				Organization organization = estimate.getOrganization();
				Customer estimateCustomer = new Customer();
				if (estimate.getCustomerId() != null)
					estimateCustomer = this.customerRepository.findById(estimate.getCustomerId()).get();

				EmailTemplateDTO emailTemplateDTO = this.emailTemplateService
						.emailTemplateDetailsByTemplateTypeAndOrganization(EmailTemplateType.ESTIMATE_TEMPLATE,
								organization, estimateCustomer);
				if (emailTemplateDTO.getTemplateText() != null) {
					emailSubject = emailTemplateDTO.getTemplateSubject();
					context.setVariable("message", "");
					context.setVariable("htmlText", emailTemplateDTO.getTemplateText());
				} else {
					emailSubject = estimateEmailDTO.getSubject();
					context.setVariable("message", estimateEmailDTO.getBody());
					context.setVariable("htmlText", "");
				}

				context.setVariable("message", estimateMessage);
				body = templateEngine.process("pdf_template", context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pdfBytes != null)
				// Send the PDF as an email attachment
				emailService.sendEmailWithAttachment(estimateEmailDTO.getToEmail(), emailSubject, body, pdfBytes,
						"Estimate.pdf", orgLogo);

			commonResponse.setData(Boolean.TRUE);
			commonResponse.setResult(true);
			commonResponse.setStatus(HttpStatus.OK.value());
			String msg = env.getProperty("estimate.send.succes");
			commonResponse.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			return commonResponse;
		} catch (Exception e) {
			// Handle exceptions and show an error page or message
			e.printStackTrace();
			commonResponse.setData(Boolean.FALSE);
			commonResponse.setResult(false);
			commonResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			String msg = env.getProperty("estimate.send.failed");
			commonResponse.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			return commonResponse;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EstimateService#removeEstimate(java.lang.Long,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<Boolean> removeEstimate(Long estimateId, HttpSession httpSession) {
		log.info("Enter inside EstimateServiceImpl.removeEstimate() method.");
		CommonResponse<Boolean> response = new CommonResponse<Boolean>();
		if (estimateId != null && estimateId > 0) {
			String msg = "";
			Optional<Estimate> estimateOpt = this.estimateRepository.findById(estimateId);
			if (estimateOpt.isPresent()) {
				Estimate estimate = estimateOpt.get();
				try {
					estimate.setIsDeleted(true);
					estimateRepository.save(estimate);
				} catch (Exception e) {
					String errorMessage = "Failed to delete estimate with ID: " + estimateId;
					log.error(errorMessage, e);
				}
				msg = env.getProperty("estimate.delete.success");
			} else
				msg = env.getProperty("estimate.delete.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("estimate.delete.fail");
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
	 * @see
	 * com.service.vix.service.EstimateService#updateEstimate(com.service.vix.dto.
	 * EstimateDTO)
	 */
	@Override
	public CommonResponse<EstimateDTO> updateEstimate(EstimateDTO estimateDTO, HttpSession httpSession) {
		log.info("Enter inside EstimateServiceImpl.saveEstimate() method.");
		CommonResponse<EstimateDTO> result = new CommonResponse<EstimateDTO>();
		log.info("Going to get customer object from estimate DTO object");
		Customer customer = this.customerRepository.findByCustomerName(estimateDTO.getCustomerName()).get();
		log.info("Going to get estimate object from estimate DTO object");
		Estimate estimate = EstimateMapper.INSTANCE.estimateDTOToEstimate(estimateDTO);
		Estimate DBEstimate = this.estimateRepository.findById(estimateDTO.getId()).get();
		estimate.setCustomerId(customer.getId());
		estimate.setId(estimateDTO.getId());
		estimate.setOrganization(DBEstimate.getOrganization());
		Estimate savedEstimate = new Estimate();
		EstimateDTO savedEstimateDTO = new EstimateDTO();
		try {
			log.info("Going to save estimate.");
			savedEstimate = this.estimateRepository.save(estimate);
			savedEstimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(savedEstimate);
			savedEstimateDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			String msg = env.getProperty("estimate.update.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(savedEstimateDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Estimate.");
			String msg = env.getProperty("estimate.update.failed") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(savedEstimateDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#optionEstimateStatusChange(java.lang.
	 * Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public void optionEstimateStatusChange(Long estimateId, String requestType, Long optionId, Principal principal,
			HttpSession httpSession) {
		log.info("Enter inside EstimateServiceImpl.optionEstimateStatusChange() method.");
		try {
			Estimate estimate = this.estimateRepository.findById(estimateId).get();
			if (requestType.equals("accept")) {
				estimate.setEstimateStatus(EstimateStatus.ESTIMATE_ACCEPTED);
				this.estimateRepository.save(estimate);
				log.info("Going to convert Estimate to Job");
				this.convertEstimateToJob(estimateId, optionId, principal, httpSession);
			} else if (requestType.equals("requestChange")) {
				estimate.setEstimateStatus(EstimateStatus.ESTIMATE_REQUEST_CHANGE);
				this.estimateRepository.save(estimate);
			}

		} catch (Exception e) {
			log.error("Exception occuring in EstimateServiceImpl.optionEstimateStatusChange() method");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EstimateService#getUserEstimates(java.security.
	 * Principal)
	 */
	@Override
	public CommonResponse<List<EstimateDTO>> getUserEstimates(Principal principal) {
		log.info("Enter inside EstimateServiceImpl.getEstimates() method.");
		CommonResponse<List<EstimateDTO>> response = new CommonResponse<>();
		List<Estimate> estimate = this.estimateRepository.findByCreatedByAndIsDeletedFalseOrderByCreatedAtDesc(principal.getName());
		List<EstimateDTO> estimateDTOs = new ArrayList<EstimateDTO>();
		estimate.forEach(e -> {
			EstimateDTO estimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(e);
			if (e.getCustomerId() != null) {
				CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
				customerDTO.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
				estimateDTO.setCustomerDTO(customerDTO);
				estimateDTOs.add(estimateDTO);
			}
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(estimateDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.EstimateService#getOrganizationEstimates(java.
	 * security.Principal)
	 */
	@Override
	public CommonResponse<List<EstimateDTO>> getOrganizationEstimates(Principal principal) {
		log.info("Enter inside EstimateServiceImpl.getOrganizationEstimates() methid.");
		CommonResponse<List<EstimateDTO>> response = new CommonResponse<List<EstimateDTO>>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		Optional<List<Estimate>> optUserOrgEstimates = this.estimateRepository
				.findByOrganizationAndIsDeletedFalseOrderByCreatedAtDesc(organization);
		List<EstimateDTO> estimateDTOs = new ArrayList<EstimateDTO>();
		if (!optUserOrgEstimates.isEmpty()) {
			List<Estimate> userEstimates = optUserOrgEstimates.get();
			userEstimates.stream().forEach(e -> {
				EstimateDTO estimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(e);
				if (e.getCustomerId() != null) {
					CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
					customerDTO
							.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
					estimateDTO.setCustomerDTO(customerDTO);
					estimateDTOs.add(estimateDTO);
				}
			});
		}
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(estimateDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#convertEstimateToJob(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public JobDTO convertEstimateToJob(Long estimateId, Long optionId, Principal principal, HttpSession httpSession) {
		log.info("Enter inside EstimateServiceImpl.convertEstimateToJob() method.");

		Optional<Estimate> estimateOpt = this.estimateRepository.findById(estimateId);
		Estimate estimate = estimateOpt.get();

//		EstimateDTO estimateDTO = EstimateMapper.INSTANCE.estimateToEstimateDTO(estimate);
		JobDTO jobDTO = new JobDTO();

		if (estimate.getCustomerId() != null) {
			Customer customer = this.customerRepository.findById(estimate.getCustomerId()).get();
			CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customer);
			jobDTO.setCustomerName(customerDTO.getCustomerName());
			jobDTO.setCustomerDTO(customerDTO);
		}
		jobDTO.setJobStatus(JobStatus.UNSCHEDULED);

		Optional<Option> optionOpt = this.optionRepository.findById(optionId);
		Option option = optionOpt.get();
		option.setJobTotal(option.getEstimateTotal());
		jobDTO.setOptions(Arrays.asList(option));

		this.jobService.saveJob(jobDTO, httpSession, principal);
		log.info("Job created successfully");

//		Customer customer = new Customer();
//		StoredServiceLocation storedServiceLocation = new StoredServiceLocation();
//		Option option = new Option();
//		Optional<Estimate> optEstimate = this.estimateRepository.findById(estimateId);
//		if (!optEstimate.isEmpty()) {
//			log.info("Estimate fetch successfully");
//			Estimate estimate = optEstimate.get();
//			jobDTO.setEstimate(estimate);
//			Optional<Customer> optCustomer = this.customerRepository.findById(estimate.getCustomerId());
//			if (!optCustomer.isEmpty()) {
//				customer = optCustomer.get();
//				log.info("Customer fetch successfully");
//				List<StoredServiceLocation> storedServiceLocations = customer.getStoredServiceLocations();
//				log.info("Service Location fetch successfully");
//				storedServiceLocation = storedServiceLocations.get(0);
//			}
//		}
//		Optional<Option> optOption = this.optionRepository.findById(optionId);
//		if (!optOption.isEmpty()) {
//			log.info("Option fetch successfully");
//			option = optOption.get();
//		}
//		jobDTO.setCustomer(customer);
//		jobDTO.setServiceLocation(storedServiceLocation);
//		jobDTO.setOption(option);
//		jobDTO.setJobStatus(JobStatus.UNSCHEDULED);
//		log.info("Going to create Job");
//		CommonResponse<JobDTO> createdJob = this.jobService.createJob(jobDTO);
//		log.info("Job created successfully");
//		InvoiceDTO generatedEstimateInvoice = this.generateEstimateInvoice(createdJob.getData());
//		JobDTO savedJobDTO = createdJob.getData();
//		savedJobDTO.setInvoice(InvoiceMapper.INSTANCE.invoiceDTOTOInvoice(generatedEstimateInvoice));
//		this.jobService.updateJob(savedJobDTO);
//		return createdJob.getData();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#generateEstimateInvoice(com.service.
	 * vix.dto.JobDTO)
	 */
	@Override
	public InvoiceDTO generateEstimateInvoice(JobDTO jobDTO) {
//		log.info("Enter inside EstimateServiceImpl.generateEstimateInvoice() method.");
//		InvoiceDTO invoiceDTO = new InvoiceDTO();
//		invoiceDTO.setJob(JobMapper.INSTANCE.jobDTOToJob(jobDTO));
//		invoiceDTO.setCustomer(jobDTO.getCustomer());
//		invoiceDTO.setServiceLocation(jobDTO.getServiceLocation());
//		invoiceDTO.setOption(jobDTO.getOption());
//		invoiceDTO.setEstimate(jobDTO.getEstimate());
//		invoiceDTO.setInvoiceStatus(InvoiceStatus.UNPAID);
//		log.info("Going to generate invoice.");
//		CommonResponse<InvoiceDTO> createdInvoice = this.invoiceService.createInvoice(invoiceDTO);
//		log.info("Invoice generated successfully.");
//		return createdInvoice.getData();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.EstimateService#optionDetailsByOptionId(java.lang.
	 * Long)
	 */
	@Override
	public Option optionDetailsByOptionId(Long optionId) {
		Option option = new Option();
		Optional<Option> optionOpt = this.optionRepository.findById(optionId);
		if (optionOpt.isPresent())
			option = optionOpt.get();
		return option;
	}
}
