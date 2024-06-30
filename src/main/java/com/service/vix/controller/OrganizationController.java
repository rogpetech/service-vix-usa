package com.service.vix.controller;

import java.security.Principal;
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
import com.service.vix.dto.LoginRequest;
import com.service.vix.dto.Message;
import com.service.vix.dto.OrganizationDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.enums.ERole;
import com.service.vix.service.OrganizationService;
import com.service.vix.service.ProfileService;
import com.service.vix.service.UserService;
import com.service.vix.utility.Countries;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as a controller that open all the pages related to
 * organization
 */
@Controller
@Slf4j
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	/**
	 * This method is used to open organization registration page
	 * 
	 * @author hemantr
	 * @date Jun 1, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add-organization")
	public String addOrganization(Model model) {
		log.info("Enter inside OrganizationController.addOrganization() method");
		model.addAttribute("countries", Countries.countries);
		UserDTO userDTO = new UserDTO();
		userDTO.setOrganizationDTO(new OrganizationDTO());
		userDTO.getOrganizationDTO().setOrgName("");
		model.addAttribute("userDTO", userDTO);
		return "organization/add-organization";
	}

	/**
	 * This method is used to save organization when we click the submit button of
	 * add organization
	 * 
	 * @author ritiks
	 * @date Jun 1, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add-organization")
	public String processAddOrganization(@ModelAttribute("organizationDTO") UserDTO userDTO, Model model,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		log.info("Enter inside OrganizationController.processAddOrganization() method");
		CommonResponse<UserDTO> organization = this.organizationService.saveOrganization(userDTO);
		HttpSession httpSession = httpServletRequest.getSession();
		log.info("Got the response for saved organization");
		if (organization.getResult()) {
			log.info("Organization saved successfully");
			httpSession.setAttribute("message", new Message(organization.getMessage(), "success"));
			LoginRequest loginRequest = new LoginRequest(organization.getData().getEmail(), userDTO.getPassword(), false);
			CommonResponse<String> loginUserResponse = this.userService.loginUser(loginRequest, httpSession,
					httpServletRequest, httpServletResponse, model);
			return loginUserResponse.getData();
		} else {
			log.info("Error while try to save organization");
			httpSession.setAttribute("message", new Message(organization.getMessage(), "error"));
			model.addAttribute("userDTO", organization.getData());
		}
		return "redirect:/organization/add-organization";
	}

	/**
	 * This method is used to redirect organization on to login page
	 * 
	 * @author hemantr
	 * @date Jun 2, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/login")
	public String loginOrganization() {
		log.info("Enter inside OrganizationController.loginOrganization() method");
		return "login";
	}

	/**
	 * This method is used to open update Profile Page
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param profileId
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("/update/{profileId}")
	public String updateProfile(@PathVariable Long profileId, Model model, Principal principal) {
		log.info("Enter inside OrganizationController.updateProfile() method.");
		CommonResponse<UserDTO> loggedInUserDetails = this.profileService.loggedInUserDetails(principal);
		model.addAttribute("user", loggedInUserDetails.getData());
		model.addAttribute("countries", Countries.countries);
		model.addAttribute("roles", ERole.values());
		return "profile/edit-profile";
	}

	/**
	 * This method is used to process update profile form
	 * 
	 * @author ritiks
	 * @date Aug 18, 2023
	 * @param UserDTO
	 * @param httpSession
	 * @param model
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */
	@PostMapping("/process-update-profile")
	public String processUpdateProfile(@ModelAttribute("organizationDTO") UserDTO UserDTO, HttpSession httpSession,
			Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Principal principal) {
		log.info("Enter inside OrganizationController.processUpdateProfile() method.");
		this.organizationService.updateOrganization(UserDTO, httpSession);
		CommonResponse<UserDTO> loggedInUserDetails = this.profileService.loggedInUserDetails(principal);
		model.addAttribute("user", loggedInUserDetails.getData());
		model.addAttribute("countries", Countries.countries);
		model.addAttribute("roles", ERole.values());
		return "redirect:/profile/details";
	}

	/**
	 * This method is used to check organization username exists or not
	 * 
	 * @author ritiks
	 * @date Sep 19, 2023
	 * @return Map<Boolean,String>
	 * @param username
	 * @return
	 * @exception Description
	 */
	@GetMapping("/searchByName/{username}")
	@ResponseBody
	public Map<Boolean, String> searchUserByUserName(@PathVariable String username) {
		log.info("Enter inside OrganizationController.searchUserByUserName() method.");
		return this.userService.getUserByUserName(username);
	}

	/**
	 * This method is used to check organization with organization name
	 * 
	 * @author ritiks
	 * @date Sep 19, 2023
	 * @return Map<Boolean,String>
	 * @param orgName
	 * @return
	 * @exception Description
	 */
	@GetMapping("/orgNameExists/{orgName}")
	@ResponseBody
	public Boolean searchOrganizationByOrgName(@PathVariable String orgName) {
		log.info("Enter inside OrganizationController.searchOrganizationByOrgName() method.");
		return this.organizationService.checkOrganizationWithOrganizationName(orgName);
	}
}
