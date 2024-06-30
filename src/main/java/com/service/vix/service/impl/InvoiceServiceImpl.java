/* 
 * ===========================================================================
 * File Name InvoiceServiceImpl.java
 * 
 * Created on Aug 7, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: InvoiceServiceImpl.java,v $
 * ===========================================================================
 */
package com.service.vix.service.impl;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.InvoiceEmailDTO;
import com.service.vix.dto.Message;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.mapper.CustomerMapper;
import com.service.vix.mapper.EstimateMapper;
import com.service.vix.mapper.InvoiceMapper;
import com.service.vix.mapper.UserMapper;
import com.service.vix.models.ContactNumber;
import com.service.vix.models.Customer;
import com.service.vix.models.Email;
import com.service.vix.models.Estimate;
import com.service.vix.models.Invoice;
import com.service.vix.models.Option;
import com.service.vix.models.OptionProduct;
import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.RecieveInvoicePayment;
import com.service.vix.models.Services;
import com.service.vix.models.StoredServiceLocation;
import com.service.vix.models.User;
import com.service.vix.repositories.CustomerRepository;
import com.service.vix.repositories.EstimateRepository;
import com.service.vix.repositories.InvoiceRepository;
import com.service.vix.repositories.OptionRepository;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.repositories.RecieveInvoicePaymentRepository;
import com.service.vix.repositories.SalesPersonsRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.EmailService;
import com.service.vix.service.InvoiceService;
import com.service.vix.utility.PDFGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This met
 */
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private Environment env;

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private SalesPersonsRepository salesPersonsRepository;
	@Autowired
	private EstimateRepository estimateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RecieveInvoicePaymentRepository recieveInvoicePaymentRepository;

	@Autowired
	private CommonService commonService;
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

	@Autowired
	private EmailService emailService;

	@Autowired
	private OptionRepository optionRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.InvoiceService#createInvoice(com.service.vix.dto.
	 * InvoiceDTO)
	 */
	@Override
	public CommonResponse<InvoiceDTO> createInvoice(InvoiceDTO invoiceDTO) {
		log.info("Enter inside InvoiceServiceImpl.createInvoice() method.");
		CommonResponse<InvoiceDTO> result = new CommonResponse<InvoiceDTO>();
		try {
			log.info("Going to save Invoice.");
			Invoice savedInvoice = this.invoiceRepository.save(InvoiceMapper.INSTANCE.invoiceDTOTOInvoice(invoiceDTO));
			invoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(savedInvoice);
			String msg = env.getProperty("invoice.save.success");
			log.info(msg);
			result.setMessage(msg);
			result.setData(invoiceDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Invoice.");
			String msg = env.getProperty("invoice.save.fail") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			result.setMessage(msg);
			result.setData(invoiceDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.InvoiceService#getInvoices()
	 */
	@Override
	public CommonResponse<List<InvoiceDTO>> getInvoices() {
		log.info("Enter inside InvoiceServiceImpl.getInvoices() method.");
		CommonResponse<List<InvoiceDTO>> response = new CommonResponse<>();
		List<Invoice> invoices = this.invoiceRepository.findAllByIsDeletedFalseOrderByCreatedAtDesc();
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>();
		invoices.forEach(invoice -> invoiceDTOs.add(InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(invoice)));
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(invoiceDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.InvoiceService#getInvoiceById(java.lang.Long)
	 */
	/*
	 * @Override public CommonResponse<InvoiceDTO> getInvoiceById(Long invoiceId) {
	 * log.info("Enter inside InvoiceServiceImpl.getInvoiceById() method.");
	 * CommonResponse<InvoiceDTO> response = new CommonResponse<>(); if (invoiceId
	 * != null && invoiceId > 0) { InvoiceDTO invoiceDTO = new InvoiceDTO();
	 * Optional<Invoice> invoiceOpt = this.invoiceRepository.findById(invoiceId); if
	 * (invoiceOpt.isPresent()) {
	 * 
	 * Invoice invoice = invoiceOpt.get();
	 * 
	 * invoice.getOption().getOptionProducts().stream().forEach(opPro -> { // if
	 * (opPro.getProductId() != null) //
	 * opPro.setProduct(this.productRepository.findById(opPro.getProductId()).get())
	 * ; // if (opPro.getServiceId() != null) //
	 * opPro.setServices(this.serviceRepository.findById(opPro.getServiceId()).get()
	 * ); if (opPro.getProductName() != null && !opPro.getProductName().equals(""))
	 * { Product product = new Product();
	 * product.setProductName(opPro.getProductName());
	 * product.setAverageCost(opPro.getProductCost());
	 * product.setDiscription(opPro.getProductDiscription());
	 * product.setRegularPrice(opPro.getRate()); opPro.setProduct(product); } else {
	 * Services services = new Services();
	 * services.setServiceName(opPro.getServiceName());
	 * services.setInternalCost(opPro.getServiceCost());
	 * services.setDiscription(opPro.getServiceDiscription());
	 * services.setRegularPrice(opPro.getRate()); opPro.setServices(services); } });
	 * 
	 * invoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(invoiceOpt.get()); }
	 * String msg = env.getProperty("record.found.success"); log.info(msg);
	 * response.setMessage(msg); response.setData(invoiceDTO);
	 * response.setResult(true); response.setStatus(HttpStatus.OK.value()); return
	 * response; } else { String msg = env.getProperty("record.not.found");
	 * log.info(msg); response.setMessage(msg); response.setData(null);
	 * response.setResult(true); response.setStatus(HttpStatus.NOT_FOUND.value());
	 * return response; } }
	 */

	@Override
	public InvoiceDTO extractInvoiceFormObject(HttpServletRequest httpServletRequest) {
		log.info("Enter inside InvoiceServiceImpl.extractInvoiceFormObject() method.");
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		log.info("Going to get customer details.");
		String customerName = httpServletRequest.getParameter("customerName");
		String estimateId = httpServletRequest.getParameter("estimateId");

		if (estimateId != null && !estimateId.equals("")) {
			Optional<Estimate> estimateOpt = this.estimateRepository.findById(Long.valueOf(estimateId));
			if (estimateOpt.isPresent()) {
				invoiceDTO.setEstimateDTO(EstimateMapper.INSTANCE.estimateToEstimateDTO(estimateOpt.get()));
			}
		}

		List<Option> options = new ArrayList<Option>();

		int invoiceTableCounter = 1;
		int optProductCounter = 1;
		List<OptionProduct> optionProducts = new ArrayList<>();
		Option option = new Option();
		while (true) {
			String productName = httpServletRequest
					.getParameter("invoice-table" + invoiceTableCounter + "ProductName" + optProductCounter);
			String serviceName = httpServletRequest
					.getParameter("invoice-table" + invoiceTableCounter + "ServiceName" + optProductCounter);

			String invoiceTotalStr = httpServletRequest
					.getParameter("invoice-table" + invoiceTableCounter + "InvoiceTotal");
			invoiceTotalStr = invoiceTotalStr.replace("$", "");
			invoiceTotalStr = invoiceTotalStr.replace(",", "");
			Float invoiceTotal = 0f;
			if (invoiceTotalStr != null)
				invoiceTotal = Float.valueOf(invoiceTotalStr);
			option.setInvoiceTotal(invoiceTotal);

			if (productName != null && !productName.equals("")
					&& this.productRepository.findByProductName(productName).isPresent()) {
				OptionProduct optionProduct = new OptionProduct();
				String productQuantity = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Quantity" + optProductCounter);
				String productRate = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Rate" + optProductCounter);
				productRate = productRate.replace("$", "");
				productRate = productRate.replace(",", "");
				String productTotal = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Total" + optProductCounter);
				productTotal = productTotal.replace("$", "");
				productTotal = productTotal.replace(",", "");
				String productDiscription = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Description" + optProductCounter);
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
						.getParameter("invoice-table" + invoiceTableCounter + "Quantity" + optProductCounter);
				String serviceTotal = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Total" + optProductCounter);
				serviceTotal = serviceTotal.replace("$", "");
				serviceTotal = serviceTotal.replace(",", "");
				String serviceRate = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Rate" + optProductCounter);
				serviceRate = serviceRate.replace("$", "");
				serviceRate = serviceRate.replace(",", "");
				String serviceDiscription = httpServletRequest
						.getParameter("invoice-table" + invoiceTableCounter + "Description" + optProductCounter);
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

		invoiceDTO.setCustomerName(customerName);
		invoiceDTO.setOptions(options);

		return invoiceDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.InvoiceService#saveInvoice(com.service.vix.dto.
	 * InvoiceDTO, jakarta.servlet.http.HttpSession, java.security.Principal)
	 */
	@Override
	public CommonResponse<InvoiceDTO> saveInvoice(InvoiceDTO invoiceDTO, HttpSession httpSession, Principal principal) {
		log.info("Enter inside InvoiceServiceImpl.saveInvoice() method.");
		CommonResponse<InvoiceDTO> result = new CommonResponse<InvoiceDTO>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		log.info("Going to get customer object from invoice DTO object");
		Customer customer = this.customerRepository.findByCustomerName(invoiceDTO.getCustomerName()).get();
		log.info("Going to get invoice object from invoice DTO object");
		Invoice invoice = InvoiceMapper.INSTANCE.invoiceDTOTOInvoice(invoiceDTO);

//		invoice.setCustomerId(customer.getId());
		invoice.setCustomer(customer);

		invoice.setOrganization(organization);
		Invoice saveInvoice = new Invoice();
		InvoiceDTO saveinvoiceDTO = new InvoiceDTO();
		String msg = "";
		try {
			log.info("Going to save invoice.");
			Long findMaxInvoiceId = this.invoiceRepository.findMaxInvoiceId();

			if (findMaxInvoiceId != null) {
				invoice.setInvoiceNumber(this.invoiceRepository.findMaxInvoiceId() + 1);
			} else {
				invoice.setInvoiceNumber(1L);
			}
			saveInvoice = this.invoiceRepository.save(invoice);
			saveinvoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(saveInvoice);
			saveinvoiceDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			msg = env.getProperty("invoice.save.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(saveinvoiceDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Invoice.");
			msg = env.getProperty("invoice.save.failed") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(saveinvoiceDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	@Override
	public CommonResponse<InvoiceDTO> getInvoiceById(Long invoiceId) {
		log.info("Enter inside InvoiceServiceImpl.getInvoiceById() method.");
		CommonResponse<InvoiceDTO> response = new CommonResponse<InvoiceDTO>();
		if (invoiceId != null && invoiceId > 0) {
			InvoiceDTO invoiceDTO = new InvoiceDTO();
			Optional<Invoice> invoiceOpt = this.invoiceRepository.findById(invoiceId);
			if (invoiceOpt.isPresent()) {
				Invoice invoice = invoiceOpt.get();
				for (Option op : invoice.getOptions()) {
					for (OptionProduct opPro : op.getOptionProducts()) {
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
					}
				}
				invoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(invoice);
//				Customer customer = this.customerRepository.findById(invoice.getCustomerId()).get();
				Customer customer = invoice.getCustomer();
				CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customer);

				invoiceDTO.setCustomerDTO(customerDTO);
			}
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(invoiceDTO);
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

	@Override
	public CommonResponse<Boolean> removeInvoice(Long invoiceId, HttpSession httpSession) {
		log.info("Enter inside InvoiceServiceImpl.removeInvoice() method.");
		CommonResponse<Boolean> response = new CommonResponse<Boolean>();
		if (invoiceId != null && invoiceId > 0) {
			String msg = "";
			Optional<Invoice> invoiceOpt = this.invoiceRepository.findById(invoiceId);
			if (invoiceOpt.isPresent()) {
				Invoice invoice = invoiceOpt.get();

				try {
					invoice.setIsDeleted(true);
					invoiceRepository.save(invoice);
				} catch (Exception e) {
					String errorMessage = "Failed to delete invoice with ID: " + invoiceId;
					log.error(errorMessage, e);
				}
				msg = env.getProperty("invoice.delete.success");
			} else
				msg = env.getProperty("invoice.delete.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("invoice.delete.fail");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(false);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	@Override
	public CommonResponse<InvoiceDTO> updateInvoice(InvoiceDTO invoiceDTO, HttpSession httpSession) {
		log.info("Enter inside InvoiceServiceImpl.updateInvoice() method.");
		CommonResponse<InvoiceDTO> result = new CommonResponse<InvoiceDTO>();
		log.info("Going to get customer object from invoice DTO object");
		Customer customer = this.customerRepository.findByCustomerName(invoiceDTO.getCustomerName()).get();
		log.info("Going to get invoice object from invoice DTO object");
		Invoice invoice = InvoiceMapper.INSTANCE.invoiceDTOTOInvoice(invoiceDTO);
		Invoice DBInvoice = this.invoiceRepository.findById(invoiceDTO.getId()).get();
//		invoice.setCustomerId(customer.getId());
		invoice.setCustomer(customer);
		invoice.setId(invoiceDTO.getId());
		invoice.setOrganization(DBInvoice.getOrganization());
		Invoice savedInvoice = new Invoice();
		InvoiceDTO savedInvoiceDTO = new InvoiceDTO();
		try {
			log.info("Going to save invoice.");
			savedInvoice = this.invoiceRepository.save(invoice);
			savedInvoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(savedInvoice);
			savedInvoiceDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			String msg = env.getProperty("invoice.update.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(savedInvoiceDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Invoice.");
			String msg = env.getProperty("invoice.update.failed") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(savedInvoiceDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		return result;
	}

	@Override
	public CommonResponse<List<InvoiceDTO>> getOrganizationInvoices(Principal principal) {
		log.info("Enter inside InvoiceServiceImpl.getOrganizationInvoices() method.");
		CommonResponse<List<InvoiceDTO>> response = new CommonResponse<List<InvoiceDTO>>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		Optional<List<Invoice>> optUserOrgInvoices = this.invoiceRepository
				.findByOrganizationAndIsDeletedFalse(organization);
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>();
		if (!optUserOrgInvoices.isEmpty()) {
			List<Invoice> userinvoices = optUserOrgInvoices.get();
			userinvoices.stream().forEach(e -> {
				InvoiceDTO invoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(e);
				if (e.getCustomer() != null) {
//					CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
					CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(e.getCustomer());
					customerDTO
							.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
					invoiceDTO.setCustomerDTO(customerDTO);
					invoiceDTOs.add(invoiceDTO);
				}
			});
		}
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(invoiceDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	public CommonResponse<List<InvoiceDTO>> getUserInvoices(Principal principal) {
		log.info("Enter inside InvoiceServiceImpl.getUserInvoices() method.");
		CommonResponse<List<InvoiceDTO>> response = new CommonResponse<>();
		List<Invoice> invoice = this.invoiceRepository.findByCreatedByAndIsDeletedFalse(principal.getName());
		List<InvoiceDTO> invoiceDTOs = new ArrayList<InvoiceDTO>();
		invoice.forEach(e -> {

			InvoiceDTO invoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(e);

			if (e.getCustomer() != null) {
//				CustomerDTO customerDTO = this.customerService.getCustomerById(e.getCustomerId()).getData();
				CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(e.getCustomer());
				customerDTO.setSalesPerson(this.salesPersonsRepository.findbyId(customerDTO.getSalesPersonId()).get());
				invoiceDTO.setCustomerDTO(customerDTO);
				invoiceDTOs.add(invoiceDTO);
			}
		});
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(invoiceDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.InvoiceService#generateSelectedInvoice(java.lang.
	 * String[])
	 */
	@Override
	public CommonResponse<InvoiceDTO> generateSelectedInvoice(Map<String, List<String>> data, HttpSession httpSession,
			Principal principal) {
		log.info("Enter inside InvoiceServiceImpl.generateSelectedInvoice() method.");
		CommonResponse<InvoiceDTO> result = new CommonResponse<InvoiceDTO>();
		InvoiceDTO invoiceDTO = new InvoiceDTO();

		List<String> optionProductsIds = data.get("optionProductsIds");

		optionProductsIds = optionProductsIds.stream().filter(opId -> (opId != null && !opId.equals("")))
				.collect(Collectors.toList());

		List<String> optionProductProductNames = data.get("optionProductProductNames");
		List<String> optionProductDescs = data.get("optionProductDescs");
		List<String> optionProductRates = data.get("optionProductRates");
		List<String> optionProductTotals = data.get("optionProductTotals");
		List<String> optionProductQuantities = data.get("optionProductQuantities");
		String estimateId = data.get("estimateId").get(0);

		List<Option> options = new ArrayList<>();
		Option option = new Option();
		Float invoiceTotal = 0f;
		List<OptionProduct> optionProducts = new ArrayList<OptionProduct>();
		for (int i = 0; i < optionProductsIds.size(); i++) {
			String optionId = optionProductsIds.get(i);
			if (optionId != null && !optionId.equals("")) {
				OptionProduct optionProduct = new OptionProduct();

				String productService = optionProductProductNames.get(i);
				Optional<Product> productOpt = this.productRepository.findByProductName(productService);
				// If element is product
				if (productOpt.isPresent()) {
					optionProduct.setProductName(optionProductProductNames.get(i));
					optionProduct.setProductDiscription(optionProductDescs.get(i));
					String productRate = optionProductRates.get(i);
					productRate = productRate.replace("$", "");
					productRate = productRate.replace(",", "");
					Float rate = 0f;
					if (productRate != null)
						rate = Float.valueOf(productRate);
					optionProduct.setProductRegularPrice(rate);
					optionProduct.setRate(rate);
					invoiceTotal += rate;
				}
				// If element is service
				else {
					optionProduct.setServiceName(optionProductProductNames.get(i));
					optionProduct.setServiceDiscription(optionProductDescs.get(i));
					String productRate = optionProductRates.get(i);
					productRate = productRate.replace("$", "");
					productRate = productRate.replace(",", "");
					Float rate = 0f;
					if (productRate != null)
						rate = Float.valueOf(productRate);
					optionProduct.setServiceRegularPrice(rate);
					optionProduct.setRate(rate);
					invoiceTotal += rate;
				}

				String productQuantity = optionProductQuantities.get(i);
				Float quantity = 0f;
				if (productQuantity != null)
					quantity = Float.valueOf(productQuantity);
				optionProduct.setQuantity(quantity);

				String productTotal = optionProductTotals.get(i);
				productTotal = productTotal.replace("$", "");
				productTotal = productTotal.replace(",", "");
				Float total = 0f;
				if (productTotal != null)
					total = Float.valueOf(productTotal);
				optionProduct.setTotal(total);

				optionProducts.add(optionProduct);
			}
		}
		option.setOptionProducts(optionProducts);
		option.setInvoiceTotal(invoiceTotal);
		options.add(option);
		invoiceDTO.setOptions(options);

		Optional<Estimate> estimateOpt = this.estimateRepository.findById(Long.valueOf(estimateId));
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		Long customerId = estimateOpt.get().getCustomerId();
		Optional<Customer> customerOpt = this.customerRepository.findById(customerId);
		invoiceDTO.setCustomerName(customerOpt.get().getCustomerName());
		Customer customer = this.customerRepository.findByCustomerName(invoiceDTO.getCustomerName()).get();
		log.info("Going to get invoice object from invoice DTO object");
		Invoice invoice = InvoiceMapper.INSTANCE.invoiceDTOTOInvoice(invoiceDTO);
		invoice.setEstimate(estimateOpt.get());
//		invoice.setCustomerId(customer.getId());
		invoice.setCustomer(customer);
		invoice.setOrganization(organization);
		Invoice saveInvoice = new Invoice();
		InvoiceDTO saveinvoiceDTO = new InvoiceDTO();
		String msg = "";
		try {
			log.info("Going to save invoice.");
			invoice.setInvoiceNumber(this.invoiceRepository.findMaxInvoiceId() + 1);
			saveInvoice = this.invoiceRepository.save(invoice);
			saveinvoiceDTO = InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(saveInvoice);
			saveinvoiceDTO.setCustomerDTO(CustomerMapper.INSTANCE.customerToCustomerDTO(customer));
			msg = env.getProperty("invoice.save.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			result.setMessage(msg);
			result.setData(saveinvoiceDTO);
			result.setResult(true);
			result.setStatus(HttpStatus.OK.value());
		} catch (Exception e) {
			log.error("Exception occure when going to save Invoice.");
			msg = env.getProperty("invoice.save.failed") + " | " + env.getProperty("something.went.wrong");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			result.setMessage(msg);
			result.setData(saveinvoiceDTO);
			result.setResult(false);
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.InvoiceService#sendInvoiceEmail(com.service.vix.dto.
	 * EstimateEmailDTO, jakarta.servlet.http.HttpServletResponse,
	 * jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<Boolean> sendInvoiceEmail(InvoiceEmailDTO invoiceEmailDTO, HttpServletResponse response,
			HttpSession httpSession) {
		CommonResponse<Boolean> commonResponse = new CommonResponse<>();
		List<Option> options = new ArrayList<>();
		invoiceEmailDTO.getOptionIds().forEach(optId -> options.add(this.optionRepository.findById(optId).get()));
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
		Invoice invoice = this.invoiceRepository.findById(invoiceEmailDTO.getInvoiceId()).get();

		Invoice savedInvoice = this.invoiceRepository.save(invoice);
//		Customer customer = this.customerRepository.findById(invoice.getCustomerId()).get();
		Customer customer = savedInvoice.getCustomer();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			// Prepare data for the PDF template
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
			String orgName = invoice.getOrganization().getOrgName();
			String orgAddress = invoice.getOrganization().getOrgAddress();
			String orgEmail = invoice.getOrganization().getOrgEmail();
			String orgMobNum = invoice.getOrganization().getOrgMobNum();

			String[] orgLogoUploadDirArr = this.organizationLogoUploadDirectory.split("/");
			int orgLogoUploadDirArrLength = orgLogoUploadDirArr.length;

			String orgLogo = imageBasePath + orgLogoUploadDirArr[orgLogoUploadDirArrLength - 1] + "/"
					+ invoice.getOrganization().getOrgLogo();

			data.put("number", number);
			data.put("emailOrg", emailOrg);
			data.put("orgName", orgName);
			data.put("orgAddress", orgAddress);
			data.put("orgEmail", orgEmail);
			data.put("orgMobNum", orgMobNum);
			data.put("locationNickName", locationNickName);
			data.put("address", address);
			data.put("city", city);
			data.put("state", state);
			data.put("unit", unit);
			data.put("baseURL", baseURL);
			data.put("options", options);
			data.put("invoiceId", invoiceEmailDTO.getInvoiceId());
			data.put("invoiceNumber", invoice.getInvoiceNumber());
			data.put("invoiceDate", invoice.getCreatedAt().format(formatter));
			data.put("customerName", customer.getCustomerName());
			data.put("itemPrice", invoiceEmailDTO.getItemPrice());
			data.put("itemTotal", invoiceEmailDTO.getItemTotal());
			data.put("itemQuantity", invoiceEmailDTO.getItemQuantity());
			data.put("grandTotal", invoiceEmailDTO.getGrandTotal());
			byte[] pdfBytes = null;
			String body = "";
			// Generate the PDF
			try {
				Context context = new Context();
				if (data != null) {
					for (Map.Entry<String, Object> pair : data.entrySet())
						context.setVariable(pair.getKey(), pair.getValue());
				}
				context.setVariable("isEmailTemplate", false);
				pdfBytes = pdfGenerator.generatePDF("invoice_pdf_template", context);
				context.removeVariable("isEmailTemplate");
				context.setVariable("isEmailTemplate", true);
				context.setVariable("message", invoiceEmailDTO.getBody());
				body = templateEngine.process("invoice_pdf_template", context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pdfBytes != null)
				// Send the PDF as an email attachment
				emailService.sendEmailWithAttachment(invoiceEmailDTO.getToEmail(), invoiceEmailDTO.getSubject(), body,
						pdfBytes, "Invoice.pdf", orgLogo);

			commonResponse.setData(Boolean.TRUE);
			commonResponse.setResult(true);
			commonResponse.setStatus(HttpStatus.OK.value());
			String msg = env.getProperty("invoice.send.succes");
			commonResponse.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			return commonResponse;
		} catch (Exception e) {
			// Handle exceptions and show an error page or message
			e.printStackTrace();
			commonResponse.setData(Boolean.FALSE);
			commonResponse.setResult(false);
			commonResponse.setStatus(HttpStatus.BAD_REQUEST.value());
			String msg = env.getProperty("invoice.send.failed");
			commonResponse.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			return commonResponse;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.InvoiceService#existsByInvoiceNumber(java.lang.Long)
	 */
	@Override
	public Boolean existsByInvoiceNumber(Long invoiceId) {
		log.info("Enter inside InvoiceServiceImpl.existsByInvoiceNumber() method.");
		if (invoiceId != null && !invoiceId.equals(""))
			return this.invoiceRepository.existsByInvoiceNumber(invoiceId);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.InvoiceService#existsByEstimateId(java.lang.Long)
	 */
	@Override
	public Boolean existsByEstimateId(Long estimateId) {
		log.info("Enter inside InvoiceServiceImpl.existsByEstimateId() method.");
		if (estimateId != null && !estimateId.equals(""))
			return this.invoiceRepository.existsByEstimateId(estimateId);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.InvoiceService#recievePayment(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public CommonResponse<RecieveInvoicePaymentDTO> recievePayment(String selectedInvoices,
			String selectedInvoiceCustomerId, Principal principal) {
		log.info("Enter inside InvoiceServiceImpl.recievePayment() method.");
		CommonResponse<RecieveInvoicePaymentDTO> response = new CommonResponse<RecieveInvoicePaymentDTO>();
		RecieveInvoicePaymentDTO recieveInvoicePaymentDTO = new RecieveInvoicePaymentDTO();
		if (selectedInvoiceCustomerId != null) {
			Customer customer = this.customerRepository.findById(Long.valueOf(selectedInvoiceCustomerId)).get();
			CustomerDTO customerDTO = CustomerMapper.INSTANCE.customerToCustomerDTO(customer);
			recieveInvoicePaymentDTO.setCustomerDTO(customerDTO);

			Long customerId = customerDTO.getCustomerId();
			if (customerId != null) {
				List<RecieveInvoicePayment> rips = this.recieveInvoicePaymentRepository.findByFromCustomer(customer);
				if (rips != null && rips.size() > 0) {
					recieveInvoicePaymentDTO.setTotalOutstanding(rips.get(0).getTotalOutstanding());
				} else {
					recieveInvoicePaymentDTO.setTotalOutstanding(0f);
				}
			}
		}
		String name = principal.getName();
		Optional<User> userOpt = this.userRepository.findByUsername(name);
		if (userOpt.isPresent())
			recieveInvoicePaymentDTO.setLoggedInUser(UserMapper.INSTANCE.userToUserDTO(userOpt.get()));
		String[] selectedInvoicesIds = selectedInvoices.split(",");
		List<InvoiceDTO> invoices = new ArrayList<InvoiceDTO>();
		Float invoiceTotal = 0f;
		for (String sii : selectedInvoicesIds) {
			if (sii != null && !sii.equals("")) {
				Optional<Invoice> invoiceOpt = this.invoiceRepository.findById(Long.valueOf(sii));
				if (invoiceOpt.isPresent()) {
					Invoice invoice = invoiceOpt.get();
					List<Option> options = new ArrayList<Option>();
					for (Option op : invoice.getOptions()) {
						List<OptionProduct> optionProducts = new ArrayList<OptionProduct>();
						for (OptionProduct opPro : op.getOptionProducts()) {
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
							optionProducts.add(opPro);
						}
						op.setOptionProducts(optionProducts);
						options.add(op);
					}
					invoice.setOptions(options);
					invoices.add(InvoiceMapper.INSTANCE.invoiceToInvoiceDTO(invoice));
					invoiceTotal += invoice.getOptions().get(0).getInvoiceTotal();
				}
			}
		}
		recieveInvoicePaymentDTO.setInvoiceTotal(invoiceTotal);
		recieveInvoicePaymentDTO.setInvoices(invoices);
		recieveInvoicePaymentDTO.setInvoiceIdsStr(selectedInvoices);
		response.setData(recieveInvoicePaymentDTO);
		return response;
	}
}
