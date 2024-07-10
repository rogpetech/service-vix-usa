/* 
 * ===========================================================================
 * File Name InvoicePaymentController.java
 * 
 * Created on Nov 27, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Nov 27, 2023
*/
package com.service.vix.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.service.vix.dto.CustomerDTO;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.service.CustomerService;
import com.service.vix.service.InvoiceService;

/**
 * This controller is used to handle all the controllers for Invoice Payment
 */
@Controller
@RequestMapping("/invoice/payment")
public class InvoicePaymentController extends BaseController {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private InvoiceService invoiceService;

	/**
	 * This method is used to open receive payment page
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 27, 2023
	 * @return String
	 * @param model
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@GetMapping("/recieve")
	public String recievePayment(Model model, Principal principal) {
		List<CustomerDTO> customers = this.customerService.getOrganisationCustomersWithInvoices(principal).getData();
		model.addAttribute("customers", customers);
		return "/payment/recieve-payment";
	}

	/**
	 * This method is used to open generate receive payment page for particular
	 * selected invoice
	 * 
	 * @author rodolfopeixoto
	 * @date Nov 28, 2023
	 * @return String
	 * @param data
	 * @return
	 * @exception Description
	 */
	@GetMapping("/generatePaymentTrasaction")
	public String generateRecievePayment(@RequestParam String selectedInvoices,
			@RequestParam String selectedInvoiceCustomerId, Model model, Principal principal) {
		RecieveInvoicePaymentDTO recieveInvoicePaymentDTO = this.invoiceService
				.recievePayment(selectedInvoices, selectedInvoiceCustomerId, principal).getData();
		model.addAttribute("recieveInvoicePaymentDTO", recieveInvoicePaymentDTO);
		List<CustomerDTO> customers = this.customerService.getOrganisationCustomersWithInvoices(principal).getData();
		model.addAttribute("customers", customers);
		return "/payment/payment-transaction";
	}

	/**
	 * This method is used to open edit payment page
	 * 
	 * @author rodolfopeixoto
	 * @date Dec 11, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/edit")
	public String editPayment() {
		return "/payment/edit-payment";
	}

	/**
	 * This method is used to open view payment page
	 * 
	 * @author rodolfopeixoto
	 * @date Dec 11, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/view")
	public String viewPayment() {
		return "/payment/view-payment";
	}

}
