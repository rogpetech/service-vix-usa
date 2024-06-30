/* 
 * ===========================================================================
 * File Name InvoiceController.java
 * 
 * Created on Aug 14, 2023
 *
 * This code contains copyright information which is the proprietary property
 * of Chetu India Pvt. Ltd. No part of this code may be reproduced, stored or transmitted
 * in any form without the prior written permission of CHETU.
 *
 * Copyright (C) CHETU. 2023
 * All rights reserved.
 *
 * Modification history:
 * $Log: InvoiceController.java,v $
 * ===========================================================================
 */

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.InvoiceDTO;
import com.service.vix.dto.InvoiceEmailDTO;
import com.service.vix.dto.Message;
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.enums.EstimateStatus;
import com.service.vix.models.Option;
import com.service.vix.models.Product;
import com.service.vix.models.Services;
import com.service.vix.repositories.InvoiceRepository;
import com.service.vix.service.CustomerService;
import com.service.vix.service.InvoiceService;
import com.service.vix.service.ProductCategoryService;
import com.service.vix.service.ServiceCategoryService;
import com.service.vix.utility.Currency;
import com.service.vix.utility.EstimateEmailBody;
import com.service.vix.utility.Industry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller is used as invoice Controller
 */
@Controller
@Slf4j
@RequestMapping("/invoice")
public class InvoiceController extends BaseController {

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private Environment env;

	@Autowired
	private ProductCategoryService productCategoryService;
	@Autowired
	private ServiceCategoryService serviceCategoryService;

	@Value("${spring.mail.username}")
	private String fromEmailAddress;

	@Value("${invoice.subject}")
	private String estimateEmailSubject;

	/**
	 * This method is used to open create invoice page
	 * 
	 * @param model
	 * @param httpServletRequest
	 * @date Nov 2, 2023
	 * @param principal
	 * @return
	 */
	@GetMapping("/add")
	public String addInvoice(Model model, HttpServletRequest httpServletRequest, Principal principal) {
		log.info("Enter inside InvoiceController.addInvoice() mthods.");
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
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimate", new InvoiceDTO());
		return "invoice/add-invoice";
	}

	/**
	 * This method is used to create the new invoice
	 * 
	 * @author hemantr
	 * @date Nov 2, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add")
	public String processCreateInvoice(HttpSession httpSession, HttpServletRequest servletRequest,
			Principal principal) {
		log.info("Enter inside InvoiceController.processCreateInvoice() method.");
		Long lastId = this.invoiceRepository.findMaxInvoiceNumber();

		InvoiceDTO extractInvoiceFormObject = null;
		if (lastId != null) {
			extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
			extractInvoiceFormObject.setInvoiceNumber(lastId + 1);
		} else {
			extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
			extractInvoiceFormObject.setInvoiceNumber(1L);
		}
		this.invoiceService.saveInvoice(extractInvoiceFormObject, httpSession, principal).getData();

		return "redirect:/invoice/invoices";
	}

	/**
	 * This method is used to show list of all isActive invoices
	 * 
	 * @author ritiks
	 * @date Nov 8, 2023
	 * @return String
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/invoices")
	public String showInvoices(Model model, Principal principal) {
		log.info("Enter inside InvoiceController.showInvoices() method.");
		List<InvoiceDTO> invoices = this.invoiceService.getOrganizationInvoices(principal).getData();
		List<InvoiceDTO> userEstimates = this.invoiceService.getUserInvoices(principal).getData();
		model.addAttribute("invoices", invoices);
		model.addAttribute("userEstimates", userEstimates);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		return "invoice/invoices";
	}

	/**
	 * This method is used to open update invoice page.
	 * 
	 * @author hemantr
	 * @date Nov 10, 2023
	 * @return String
	 * @param invoiceId
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/update/{invoiceId}")
	public String updateInvoice(@PathVariable Long invoiceId, Model model, Principal principal) {
		log.info("Enter inside InvoiceController.updateInvoice() method.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		InvoiceDTO invoiceDTO = this.invoiceService.getInvoiceById(invoiceId).getData();
		// invoiceDTO.setInvoiceNumber(invoiceDTO.getId());
		Boolean requestForView = true;
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("invoice", invoiceDTO);
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimateStatus", EstimateStatus.values());
		model.addAttribute("requestForView", requestForView);
		model.addAttribute("fromEmailAddress", fromEmailAddress);
		model.addAttribute("estimateEmailSubject", estimateEmailSubject);
		model.addAttribute("bodyContent", EstimateEmailBody.getBody(invoiceDTO.getCustomerDTO().getCustomerName()));
		model.addAttribute("options", invoiceDTO.getOptions());
		model.addAttribute("currencies", Currency.currencies);
		model.addAttribute("industries", Industry.industries);
		model.addAttribute("customerNames", customerName);
		model.addAttribute("estimate", new InvoiceDTO());
		if (invoiceId != null && invoiceId > 0)
			model.addAttribute("estimate", invoiceId);
		return "invoice/edit-invoice";
	}

	/**
	 * This method is used to edit invoice
	 * 
	 * @author hemantr
	 * @date Oct 11, 2023
	 * @return String
	 * @param invoiceId
	 * @param model
	 * @return
	 * @exception Description
	 */
	@GetMapping("/edit/{invoiceId}")
	public String editInvoice(@PathVariable Long invoiceId, Model model) {
		log.info("Enter inside InvoiceController.editInvoice() method.");
		InvoiceDTO invoice = this.invoiceService.getInvoiceById(invoiceId).getData();
		model.addAttribute("invoice", invoice);
		return "invoice/edit-invoice";
	}

	/**
	 * This method is used to open the view invoice page
	 * 
	 * @author hemantr
	 * @date Oct 13, 2023
	 * @return String
	 * @param invoiceId
	 * @param model
	 * @return
	 * @exception Description
	 */
	@GetMapping("/view/{invoiceId}")
	public String viewInvoice(@PathVariable Long invoiceId, Model model, Principal principal) {
		log.info("Enter inside InvoiceController.viewInvoice() method.");
		List<String> customerName = new ArrayList<String>();
		this.customerService.getCustomers(principal).getData().stream()
				.forEach(c -> customerName.add(c.getCustomerName()));
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		InvoiceDTO invoiceDTO = this.invoiceService.getInvoiceById(invoiceId).getData();
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("customerNames", customerName);
		model.addAttribute("options", invoiceDTO.getOptions());
		if (invoiceId != null && invoiceId > 0)
			model.addAttribute("invoice", invoiceDTO);
		return "invoice/view-invoice";
	}

	/**
	 * This method is used to populate option send email details on popup
	 * 
	 * @author hemantr
	 * @date Nov 06 , 2023
	 * @return Map<String,Object>
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/invoice-options/{invoiceId}")
	@ResponseBody
	public Map<String, Object> populateInvoiceDetailsOnSendMailPopup(@PathVariable Long invoiceId) {
		log.info("Enter inside InvoiceController.populateInvoiceDetailsOnSendMailPopup() method.");
		InvoiceDTO invoiceDTO = this.invoiceService.getInvoiceById(invoiceId).getData();
		Map<String, Object> data = new HashMap<>();
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(invoiceDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = invoiceDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", invoiceDTO.getCustomerDTO().getEmails().get(0).getEmail());
		data.put("invoiceId", invoiceDTO.getId());
		return data;
	}

	/**
	 * This method is used to get details related to given estimate and populate it
	 * on Estimate PDF HTML view
	 * 
	 * @author hemantr
	 * @date Nov 06, 2023
	 * @return Map<String,Object>
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/invoice-options-html-view/{invoiceId}")
	@ResponseBody
	public Map<String, Object> populateInvoiceDetailsOnInvoiceHTMLPDFView(@PathVariable Long invoiceId) {
		log.info("Enter inside InvoiceController.populateInvoiceDetailsOnInvoiceHTMLPDFView() method.");
		InvoiceDTO invoiceDTO = this.invoiceService.getInvoiceById(invoiceId).getData();

		Long invoiceNumber = invoiceDTO.getInvoiceNumber();

		System.out.println(invoiceNumber);

		Map<String, Object> data = new HashMap<>();
		data.put("invoiceId", invoiceDTO.getId());
		data.put("invoiceNumber", invoiceNumber);
		data.put("invoiceDTO", invoiceDTO);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		data.put("estimateDate", invoiceDTO.getCreatedAt().format(formatter));
		data.put("estimateCustomerName", invoiceDTO.getCustomerDTO().getCustomerName());
		List<Option> options = invoiceDTO.getOptions();
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
		data.put("option", options);
		data.put("showItemPrice", false);
		data.put("showItemTotal", false);
		data.put("showItemQuantity", false);
		data.put("showItemGrandTotal", true);
		return data;
	}

	/**
	 * This method is used to save and send invoice simultaneously
	 * 
	 * @author hemantr
	 * @date Nov 9, 2023
	 * @return Map<String,Object>
	 * @param httpSession
	 * @param servletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-save-send")
	@ResponseBody
	public Map<String, Object> processSaveAndSendInvoice(HttpSession httpSession, HttpServletRequest servletRequest,
			Principal principal) {
		log.info("Enter inside InvoiceController.processSaveAndSendInvoice() method.");
		InvoiceDTO extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		Long lastId = this.invoiceRepository.findMaxInvoiceNumber();
		if (extractInvoiceFormObject.getId() != null) {
			CommonResponse<InvoiceDTO> updateInvoice = this.invoiceService.updateInvoice(extractInvoiceFormObject,
					httpSession);
			invoiceDTO = updateInvoice.getData();
		} else if (lastId != null) {
			extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
			extractInvoiceFormObject.setInvoiceNumber(lastId + 1);
		} else {
			extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
			extractInvoiceFormObject.setInvoiceNumber(1L);
		}
		invoiceDTO = this.invoiceService.saveInvoice(extractInvoiceFormObject, httpSession, principal).getData();
		httpSession.setAttribute("message", new Message(env.getProperty("invoice.save.send"), "success"));
		Map<String, Object> data = new HashMap<>();
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(invoiceDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = invoiceDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", invoiceDTO.getCustomerDTO().getEmails().get(0).getEmail());
		data.put("invoiceId", invoiceDTO.getId());
		Long invoiceIid = invoiceDTO.getId();
		InvoiceDTO invoicedto = this.invoiceService.getInvoiceById(invoiceIid).getData();
		Long invoiceNumber = invoicedto.getInvoiceNumber();
		data.put("invoiceNumber", invoiceNumber);
		return data;
	}
	
	/**
	 * This method is used to update and send estimate simultaneously
	 * 
	 * @author hemantr
	 * @date Dec 05, 2023
	 * @return Map<String,Object>
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-update-send")
	@ResponseBody
	public Map<String, Object> processUpdateAndSendInvoice(HttpSession httpSession,
			HttpServletRequest servletRequest) {
		log.info("Enter inside InvoiceController.processUpdateAndSendInvoice() method.");
		InvoiceDTO extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
		extractInvoiceFormObject.setId(Long.valueOf(servletRequest.getParameter("invoiceId")));
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		CommonResponse<InvoiceDTO> updateInvoice = this.invoiceService.updateInvoice(extractInvoiceFormObject,
				httpSession);
		invoiceDTO = updateInvoice.getData();
		Map<String, Object> data = new HashMap<>();
		data.put("fromEmailAddress", fromEmailAddress);
		data.put("estimateEmailSubject", estimateEmailSubject);
		data.put("bodyContent", EstimateEmailBody.getBody(invoiceDTO.getCustomerDTO().getCustomerName()));
		List<Long> optionIds = invoiceDTO.getOptions().stream().map(op -> op.getId()).collect(Collectors.toList());
		data.put("options", optionIds);
		data.put("customerEmail", invoiceDTO.getCustomerDTO().getEmails().get(0).getEmail());
		data.put("invoiceId", invoiceDTO.getId());
		Long invoiceIid = invoiceDTO.getId();
		InvoiceDTO invoicedto = this.invoiceService.getInvoiceById(invoiceIid).getData();
		Long invoiceNumber = invoicedto.getInvoiceNumber();
		data.put("invoiceNumber", invoiceNumber);
		return data;
	}

	/**
	 * This method is used to generate the invoice from view estimate page
	 * 
	 * @author ritiks
	 * @date Nov 13, 2023
	 * @return String
	 * @param data
	 * @param httpSession
	 * @param servletRequest
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@PostMapping("/generate-invoice")
	public String generateSelectedProductServiceInvoice(@RequestBody Map<String, List<String>> data,
			HttpSession httpSession, HttpServletRequest servletRequest, Principal principal) {
		log.info("Enter inside InvoiceController.generateSelectedProductServiceInvoice() method.");
		this.invoiceService.generateSelectedInvoice(data, httpSession, principal);
		return "redirect:/invoice/invoices";
	}

	/**
	 * @author hemantr
	 * @date Nov 13, 2023
	 * @return String
	 * @param estimateEmailDTO
	 * @param httpServletResponse
	 * @param httpSession
	 * @return
	 * @exception Description
	 */

	@PostMapping("/send")
	public String sendInvoiceEmail(@ModelAttribute InvoiceEmailDTO invoiceEmailDTO,
			HttpServletResponse httpServletResponse, HttpSession httpSession) {
		log.info("Enter inside InvoiceController.sendInvoiceEmail() method.");
		CommonResponse<Boolean> sendInvoiceEmail = this.invoiceService.sendInvoiceEmail(invoiceEmailDTO,
				httpServletResponse, httpSession);
		if (!sendInvoiceEmail.getResult().booleanValue() == true)
			return "redirect:/invoice/invoices";
		return "redirect:/invoice/invoices";
	}

	/**
	 * This method is used to update the invoice number
	 * 
	 * @author hemantr
	 * @date Nov 22, 2023
	 * @return String
	 * @param httpSession
	 * @param servletRequest
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-update")
	public String updateInvoiceNumber(HttpSession httpSession, HttpServletRequest servletRequest) {
		log.info("Enter inside InvoiceController.updateInvoiceNumber() method.");
		InvoiceDTO extractInvoiceFormObject = this.invoiceService.extractInvoiceFormObject(servletRequest);
		extractInvoiceFormObject.setId(Long.valueOf(servletRequest.getParameter("invoiceId")));
		extractInvoiceFormObject.setInvoiceNumber(Long.valueOf(servletRequest.getParameter("invoiceNumber")));
		this.invoiceService.updateInvoice(extractInvoiceFormObject, httpSession);
		return "redirect:/invoice/invoices";
	}

	/**
	 * This method is used to check the invoice number in the database.
	 * 
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return Boolean
	 * @param invoiceId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/searchByinvoiceNumber/{invoiceId}")
	@ResponseBody
	public Boolean searchInvoiceNumber(@PathVariable Long invoiceId) {
		log.info("Enter inside InvoiceController.searchInvoiceNumber() method.");
		return this.invoiceService.existsByInvoiceNumber(invoiceId);
	}

	/**
	 * This method is used to delete the existing invoice
	 * 
	 * @author hemantr
	 * @date Nov 21, 2023
	 * @return String
	 * @param invoiceId
	 * @param model
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	@GetMapping("/remove/{invoiceId}")
	public String deleteInvoice(@PathVariable Long invoiceId, Model model, HttpSession httpSession) {
		log.info("Enter inside InvoiceController.deleteInvoice() method.");
		this.invoiceService.removeInvoice(invoiceId, httpSession);
		return "redirect:/invoice/invoices";
	}
	
	/**
	 * This method is used to check the estimate id in the database.
	 * 
	 * @author hemantr
	 * @date Nov 27, 2023
	 * @return Boolean
	 * @param estimateId
	 * @return
	 * @exception Description
	 */
	@GetMapping("/searchByEstimateId/{estimateId}")
	@ResponseBody
	public Boolean searchEstimateId(@PathVariable Long estimateId) {
		log.info("Enter inside InvoiceController.searchEstimateId() method.");
		return this.invoiceService.existsByEstimateId(estimateId);
	}
	
}
