package com.service.vix.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.models.SalesPerson;

/**
 * This interface is used as sales person service
 */
@Component
public interface SalesPersonService {

	/**
	 * This method is used to get all Sales Persons
	 * 
	 * @author hemantr
	 * @date Jun 14, 2023
	 * @return CommonResponse<List<SalesPerson>>
	 * @return
	 * @exception Description
	 */
	public CommonResponse<List<SalesPerson>> salesPersons();

	/**
	 * This method is used to get Sales Person by Id
	 * 
	 * @author hemantr
	 * @date Jun 14, 2023
	 * @return CommonResponse<SalesPerson>
	 * @param salesPersonId
	 * @return
	 * @exception Description
	 */
	public CommonResponse<SalesPerson> getSalesPersonById(Long salesPersonId);

}
