/* 
 * ===========================================================================
 * File Name RecieveInvoicePaymentService.java
 * 
 * Created on Dec 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Dec 19, 2023
*/
package com.service.vix.service;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.RecieveInvoicePaymentDTO;

@Component
public interface RecieveInvoicePaymentService {

	/**
	 * This method is used to generate received invoice payment
	 * 
	 * @author ritiks
	 * @date Dec 19, 2023
	 * @return CommonResponse<RecieveInvoicePayment>
	 * @param recieveInvoicePaymentDTO
	 * @return
	 * @exception Description
	 */
	CommonResponse<RecieveInvoicePaymentDTO> generateInvoicePayment(RecieveInvoicePaymentDTO recieveInvoicePaymentDTO);

	/**
	 * This method is used to get details of received invoice payment by id
	 * 
	 * @author ritiks
	 * @date Dec 19, 2023
	 * @return CommonResponse<RecieveInvoicePaymentDTO>
	 * @param id
	 * @return
	 * @exception Description
	 */
	CommonResponse<RecieveInvoicePaymentDTO> recievedInvoicePaymentDetails(Long id);

}
