/* 
 * ===========================================================================
 * File Name RecieveInvoicePaymentController.java
 * 
 * Created on Dec 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Dec 19, 2023
*/
package com.service.vix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.service.RecieveInvoicePaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recieve-payment")
public class RecieveInvoicePaymentController extends BaseController {

	@Autowired
	private RecieveInvoicePaymentService recieveInvoicePaymentService;

	@PostMapping("/generate")
	public String generateRecievePayment(@ModelAttribute RecieveInvoicePaymentDTO recieveInvoicePaymentDTO,
			HttpSession httpSession) {
		CommonResponse<RecieveInvoicePaymentDTO> generateInvoicePayment = this.recieveInvoicePaymentService
				.generateInvoicePayment(recieveInvoicePaymentDTO);
		if (generateInvoicePayment.getResult()) {
			httpSession.setAttribute("message", new Message(generateInvoicePayment.getMessage(), "success"));
		} else {
			httpSession.setAttribute("message", new Message(generateInvoicePayment.getMessage(), "danger"));
		}
		return "redirect:/invoice/payment/recieve";
	}

}
