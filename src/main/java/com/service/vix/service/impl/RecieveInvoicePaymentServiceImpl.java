/* 
 * ===========================================================================
 * File Name RecieveInvoicePaymentServiceImpl.java
 * 
 * Created on Dec 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Dec 19, 2023
*/
package com.service.vix.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.RecieveInvoicePaymentDTO;
import com.service.vix.mapper.RecieveInvoicePaymentMapper;
import com.service.vix.models.Customer;
import com.service.vix.models.Invoice;
import com.service.vix.models.RecieveInvoicePayment;
import com.service.vix.models.User;
import com.service.vix.repositories.CustomerRepository;
import com.service.vix.repositories.InvoiceRepository;
import com.service.vix.repositories.RecieveInvoicePaymentRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.RecieveInvoicePaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecieveInvoicePaymentServiceImpl implements RecieveInvoicePaymentService {

	@Autowired
	private RecieveInvoicePaymentRepository recieveInvoicePaymentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private Environment env;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.RecieveInvoicePaymentService#generateInvoicePayment(
	 * com.service.vix.dto.RecieveInvoicePaymentDTO)
	 */
	@Override
	public CommonResponse<RecieveInvoicePaymentDTO> generateInvoicePayment(
			RecieveInvoicePaymentDTO recieveInvoicePaymentDTO) {
		log.info("Enter inside RecieveInvoicePaymentServiceImpl.generateInvoicePayment() method.");
		CommonResponse<RecieveInvoicePaymentDTO> response = new CommonResponse<>();
		RecieveInvoicePayment recieveInvoicePayment = RecieveInvoicePaymentMapper.INSTANCE
				.recieveInvoicePaymentDTOToRecieveInvoicePayment(recieveInvoicePaymentDTO);
		if (recieveInvoicePaymentDTO.getRecievedById() != null) {
			Optional<User> recievedInvoicePaymentOpt = this.userRepository
					.findById(recieveInvoicePaymentDTO.getRecievedById());
			if (recievedInvoicePaymentOpt.isPresent())
				recieveInvoicePayment.setRecievedBy(recievedInvoicePaymentOpt.get());
		}
		if (recieveInvoicePaymentDTO.getFromCustomerId() != null) {
			Optional<Customer> fromCustomerOpt = this.customerRepository
					.findById(recieveInvoicePaymentDTO.getFromCustomerId());
			if (fromCustomerOpt.isPresent())
				recieveInvoicePayment.setFromCustomer(fromCustomerOpt.get());
		}
		List<Invoice> invoices = new ArrayList<Invoice>();
		for (String ii : recieveInvoicePaymentDTO.getInvoiceIdsStr().split(",")) {
			if (ii != null && !ii.equals("")) {
				Optional<Invoice> invoiceOpt = this.invoiceRepository.findById(Long.valueOf(ii));
				if (invoiceOpt.isPresent())
					invoices.add(invoiceOpt.get());
			}
		}
		recieveInvoicePayment.setInvoices(invoices);
		recieveInvoicePayment.setRecievedOn(LocalDateTime.now());
		try {
			if (recieveInvoicePayment.getFromCustomer() != null) {
				List<RecieveInvoicePayment> recieveInvoicePayments = this.recieveInvoicePaymentRepository
						.findByFromCustomer(recieveInvoicePayment.getFromCustomer());
				if (recieveInvoicePayments != null && recieveInvoicePayments.size() > 0) {
					RecieveInvoicePayment rp = recieveInvoicePayments.get(0);
					Long id = rp.getId();
					recieveInvoicePayment.setId(id);
					Float totalOutstanding = rp.getTotalOutstanding();
					recieveInvoicePayment
							.setTotalOutstanding(totalOutstanding + recieveInvoicePaymentDTO.getTotalOutstanding());
				}
			}
			RecieveInvoicePayment invoicePayment = this.recieveInvoicePaymentRepository.save(recieveInvoicePayment);
			RecieveInvoicePaymentDTO invoicePaymentDTO = RecieveInvoicePaymentMapper.INSTANCE
					.recieveInvoicePaymentToRecieveInvoicePaymentDTO(invoicePayment);
			response.setData(invoicePaymentDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			response.setMessage(env.getProperty("recieve.payment.invoice.save.success"));
		} catch (Exception e) {
			response.setData(null);
			response.setResult(false);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(env.getProperty("recieve.payment.invoice.save.fail"));
		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.RecieveInvoicePaymentService#
	 * recievedInvoicePaymentDetails(java.lang.Long)
	 */
	@Override
	public CommonResponse<RecieveInvoicePaymentDTO> recievedInvoicePaymentDetails(Long id) {
		log.info("Enter inside RecieveInvoicePaymentServiceImpl.recievedInvoicePaymentDetails() method.");
		CommonResponse<RecieveInvoicePaymentDTO> response = new CommonResponse<RecieveInvoicePaymentDTO>();
		if (id != null) {
			Optional<RecieveInvoicePayment> recieveInvoicePaymentOpt = this.recieveInvoicePaymentRepository
					.findById(id);
			if (recieveInvoicePaymentOpt.isPresent()) {
				RecieveInvoicePayment recieveInvoicePayment = recieveInvoicePaymentOpt.get();
				response.setData(RecieveInvoicePaymentMapper.INSTANCE
						.recieveInvoicePaymentToRecieveInvoicePaymentDTO(recieveInvoicePayment));
				response.setMessage(env.getProperty("record.found.success"));
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			}
		}
		response.setData(null);
		response.setMessage(env.getProperty("record.not.found"));
		response.setResult(true);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response;
	}

}
