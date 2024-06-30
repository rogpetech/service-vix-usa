/* 
 * ===========================================================================
 * File Name StaffService.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.StaffDTO;

import jakarta.servlet.http.HttpSession;

@Component
public interface StaffService {

	/**
	 * This method is used to save staff for organization
	 * 
	 * @author ritiks
	 * @date Aug 22, 2023
	 * @return CommonResponse<StaffDTO>
	 * @param staffDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<StaffDTO> saveStaff(StaffDTO staffDTO, HttpSession httpSession);

	/**
	 * This method is used to get staff by id
	 * 
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return CommonResponse<StaffDTO>
	 * @param staffId
	 * @return
	 * @exception Description
	 */
	CommonResponse<StaffDTO> getStaffById(Long userId);

	/**
	 * This method is used to get all staff
	 * 
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return CommonResponse<List<StaffDTO>>
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<List<StaffDTO>> getAllStaff(Principal principal);

	/**
	 * This method is used to remove staff
	 * 
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return CommonResponse<Boolean>
	 * @param staffId
	 * @return
	 * @exception Description
	 */

	CommonResponse<Boolean> removeStaff(Long staffId, HttpSession httpSession);

	/**
	 * * This method is used to update staff
	 * 
	 * @author ritiks
	 * @date Aug 23, 2023
	 * @return CommonResponse<StaffDTO>
	 * @param staffDTO
	 * @param httpSession
	 * @return
	 * @exception Description
	 */
	CommonResponse<StaffDTO> updateStaff(StaffDTO staffDTO, HttpSession httpSession);

	/**
	 * @author hemantr
	 * @date Sep 18, 2023
	 * @return Boolean
	 * @param email
	 * @return
	 * @exception Description
	 */
	Map<Boolean, String> getUserByEmail(String email);

	/**
	 * @author ritiks
	 * @date Sep 20, 2023
	 * @return CommonResponse<StaffDTO> 
	 * @param staffEmailId
	 * @return
	 * @exception 
	 * Description
	 */
	CommonResponse<StaffDTO> getStaffByEmail(String staffEmailId);

	/**
	 * @author ritiks
	 * @date Sep 20, 2023
	 * @return String 
	 * @param staffEmail
	 * @param newPassword
	 * @return
	 * @exception 
	 * Description
	 */
	String createPassword(String staffEmail, String newPassword);

	/**
	 * @author ritiks
	 * @date Sep 20, 2023
	 * @return void 
	 * @param staffDTO
	 * @exception 
	 * Description
	 */
	void sendInvitionToStaff(StaffDTO staffDTO);

}
