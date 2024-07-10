package com.service.vix.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.Message;
import com.service.vix.service.CustomerService;
import com.service.vix.service.SalesPersonService;
import com.service.vix.utility.Currency;
import com.service.vix.utility.Industry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as controller class that handle all the request for
 * customer pages
 */
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private SalesPersonService salesPersonService;

	/**
	 * This method is used to open add-customer page
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 8, 2023
	 * @return String
	 * @param model
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add")
	public String addCustomerForm(Model model) {
		log.info("Enter inside CustomerController.addCustomerForm() mthods.");
		model.addAttribute("salesPersons", this.salesPersonService.salesPersons().getData());
		model.addAttribute("industries", Industry.industries);
		model.addAttribute("currencies", Currency.currencies);
		return "customer/add-customer";
	}

	/**
	 * This method is used to save customer
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 8, 2023
	 * @return String
	 * @param customerDTO
	 * @param request
	 * @param httpSession
	 * @return
	 * @exception Description
	 */

	@PostMapping("/process-add")
	public String processAddCustomer(@ModelAttribute CustomerDTO customerDTO, HttpServletRequest request,
			HttpSession httpSession, Principal principal) {
		log.info("Enter inside CustomerController.processAddCustomer() method.");
		CustomerDTO calculatedCustomerDTOObj = this.customerService.extractAddCustomerFormObject(customerDTO, request);
		log.info("Add customer form object extracte");
		this.customerService.addCustomer(calculatedCustomerDTOObj, httpSession, principal);
		log.info("Add customer form object extracte");
		return "redirect:/customer/customers";
	}

	/**
	 * Add method description here
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 22, 2023
	 * @param customerDTO
	 * @param request
	 * @param httpSession
	 * @return
	 */
	@ResponseBody
	@PostMapping("/process-popup-add")
	public Boolean processPopUpAddCustomer(@ModelAttribute CustomerDTO customerDTO, HttpServletRequest request,
			HttpSession httpSession, Principal principal) {
		log.info("Enter inside CustomerController.processAddCustomer() method.");
		CustomerDTO calculatedCustomerDTOObj = this.customerService.extractAddCustomerFormObject(customerDTO, request);
		log.info("Add customer form object extracted");
		this.customerService.addCustomer(calculatedCustomerDTOObj, httpSession, principal);
		log.info("Add customer form object extracte");
		return true;
	}

	/**
	 * This method is used to show customer listing page
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 13, 2023
	 * @return String
	 * @param model
	 * @return
	 * @exception Description
	 */
	@GetMapping("/customers")
	public String showCustomers(Model model, Principal principal) {
		List<CustomerDTO> customers = this.customerService.getOrganisationCustomers(principal).getData();
		model.addAttribute("customers", customers);
		return "customer/customers";
	}

	/**
	 * This method is used to open edit customer page
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 25, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{customerId}")
	public String updateCustomer(@PathVariable Long customerId, Model model) {
		log.info("Enter inside CustomerController.updateCustomer() method.");
		CommonResponse<CustomerDTO> customerById = this.customerService.getCustomerById(customerId);
		CustomerDTO data = customerById.getData();
		model.addAttribute("customerDto", data);
		model.addAttribute("industries", Industry.industries);
		model.addAttribute("currencies", Currency.currencies);
		model.addAttribute("salesPersons", salesPersonService.salesPersons().getData());
		return "customer/edit-customer";
	}

	/**
	 * This method is used to update customers
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 25, 2023
	 * @param productDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-update")
	public String processUpdateCustomer(@ModelAttribute CustomerDTO customerDTO, HttpServletRequest request) {
		log.info("Enter inside CustomerController.processUpdateCustomer() method.");
		CustomerDTO calculatedCustomerDTOObj = this.customerService.extractAddCustomerFormObject(customerDTO, request);
		this.customerService.updateCustomer(calculatedCustomerDTOObj);
		return "redirect:/customer/customers";
	}

	/**
	 * This method is used to search customer
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 14, 2023
	 * @param customerName
	 * @return
	 */
	@GetMapping("/searchByName/{customerName}")
	@ResponseBody
	public List<CustomerDTO> searchCustomer(@PathVariable String customerName, Principal principal) {
		return this.customerService.searchCustomer(customerName, principal).getData();
	}

	/**
	 * This method is used to remove customer
	 * 
	 * @author rodolfopeixoto
	 * @date Jul 26, 2023
	 * @param customerId
	 * @param model
	 * @return
	 */
	@GetMapping("/delete/{customerId}")
	public String deleteCustomer(@PathVariable Long customerId, Model model, HttpSession httpSession) {
		log.info("Enter inside CustomerController.deleteCustomer() method.");
		CommonResponse<Boolean> removeCustomer = this.customerService.removeCustomer(customerId);
		if (!removeCustomer.getResult() && !removeCustomer.getData()) {
			httpSession.setAttribute("message", new Message(removeCustomer.getMessage(), "danger"));
		} else if (removeCustomer.getData() && removeCustomer.getResult()) {
			httpSession.setAttribute("message", new Message(removeCustomer.getMessage(), "success"));
		}
		return "redirect:/customer/customers";
	}

	/**
	 * This method is used to find existing register customer email
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 21, 2023
	 * @return Boolean
	 * @param email
	 * @return
	 * @exception Description
	 */
	@GetMapping("/searchByEmail/{email}")
	@ResponseBody
	public Map<Boolean, String> searchCustomerEmail(@PathVariable String email) {
		log.info("Enter inside CustomerController.searchCustomerEmail() method.");
		return this.customerService.serachCustomerByEmail(email);
	}

}
