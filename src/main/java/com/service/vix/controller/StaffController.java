/* 
 * ===========================================================================
 * File Name StaffController.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.controller;

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
import com.service.vix.dto.StaffDTO;
import com.service.vix.dto.UpdatePasswordDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.service.RoleService;
import com.service.vix.service.StaffService;
import com.service.vix.utility.USStates;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as Controller class for handle all the methods related to
 * Staff
 */
@Slf4j
@Controller
@RequestMapping("/staff")
public class StaffController extends BaseController {

	@Autowired
	private StaffService staffService;
	@Autowired
	private RoleService roleService;

	/**
	 * This method is used to save Staff
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 21, 2023
	 * @return String
	 * @param staffDTO
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add-staff")
	public String processAddStaff(@ModelAttribute StaffDTO staffDTO, HttpSession httpSession) {
		this.staffService.saveStaff(staffDTO, httpSession);
		return "redirect:/workforce/staffList";
	}

	/**
	 * This method is used to open update staff page
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 22, 2023
	 * @return String
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{userId}")
	public String updateStaff(@PathVariable Long userId, Model model) {
		log.info("Enter inside WorkForceController.updateStaff() method.");
		CommonResponse<StaffDTO> staffData = this.staffService.getStaffById(userId);
		StaffDTO staffDTO = staffData.getData();
		model.addAttribute("roles", this.roleService.getRoles().getData());
		model.addAttribute("staffDTO", staffDTO);
		model.addAttribute("usStates", USStates.usStates);
		return "workforce/edit-staff";
	}

	/**
	 * @author rodolfopeixoto
	 * @date Aug 23, 2023
	 * @return String
	 * @param staffDTO
	 * @param request
	 */
	@PostMapping("/process-update")
	public String processUpdateStaff(@ModelAttribute StaffDTO staffDTO, HttpSession httpSession) {
		log.info("Enter inside WorkForceController.processUpdateStaff() method.");
		this.staffService.updateStaff(staffDTO, httpSession);
		return "redirect:/workforce/staffList";
	}

	/**
	 * This method is used to Remove user
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 23, 2023
	 * @return String
	 * @param staffId
	 * @param httpSession
	 */
	@GetMapping("/deleteStaff/{staffId}")
	public String removeStaff(@PathVariable Long staffId, HttpSession httpSession) {
		log.info("Enter inside WorkForceController.removeStaff() method.");
		this.staffService.removeStaff(staffId, httpSession);
		return "redirect:/workforce/staffList";
	}

	/**
	 * This method is used to redirect on update user password page
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 18, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@PostMapping("/change-password")
	public String changePassword(HttpServletRequest request, Model model) {
		log.info("Enter inside StaffController.changePassword() method.");
		UserDTO staff = new UserDTO();
		Boolean result = false;
		String staffEmailId = request.getParameter("staffEmailId");
		if (staffEmailId != null) {
			result = true;
			staff = this.staffService.getStaffByEmail(staffEmailId).getData().getUserDTO();
			model.addAttribute("staffEmail", staff.getEmail());
			model.addAttribute("staffName", staff.getFirstName() + " " + staff.getLastName());
			model.addAttribute("staffId", staff.getId());
		} else {
			model.addAttribute("staffEmail", "");
			model.addAttribute("staffName", "");
			model.addAttribute("staffId", "");
		}
		model.addAttribute("result", result);
		return "/password/change_password";
	}

	/**
	 * This method is used to update password
	 * 
	 * @author rodolfopeixoto
	 * @date Sep 18, 2023
	 * @return String
	 * @param passwordDTO
	 * @return
	 * @exception Description
	 */
	@PostMapping("/update-password")
	public String updatePassword(@ModelAttribute UpdatePasswordDTO passwordDTO) {
		this.staffService.createPassword(passwordDTO.getEmail(), passwordDTO.getPassword());
		return "redirect:/login";
	}

	/**
	 * This method is used to find existing email.
	 * 
	 * @author hemantr
	 * @date Sep 21, 2023
	 * @return Map<Boolean,String>
	 * @param email
	 * @return
	 * @exception Description
	 */
	@GetMapping("/searchByEmail/{email}")
	@ResponseBody
	public Map<Boolean, String> searchUserByEmail(@PathVariable String email) {
		log.info("Enter inside WorkForceController.searchUserByEmail() method.");
		return this.staffService.getUserByEmail(email);
	}

}
