package com.service.vix.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.service.ServiceCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as controller class that handle all the request for
 * service category pages
 */
@Controller
@Slf4j
@RequestMapping("/service-category")
public class ServiceCategoryController extends BaseController {

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	/**
	 * This method is used to open add-service-category page
	 * 
	 * @author hemantr
	 * @date Jun 16, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String addServiceCategory(Model model,Principal principal) {
		log.info("Enter inside ServiceCategoryController.addServiceCategory() method.");
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal).getData();
		model.addAttribute("serviceCategories", serviceCategories);
		return "service/add-service-category";
	}

	/**
	 * This method is used to save service-category
	 * 
	 * @author hemantr
	 * @date Jun 16, 2023
	 * @param serviceCategoryDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-add")
	public String processAddServiceCategory(@ModelAttribute("serviceCategoryDTO") ServiceCategoryDTO serviceCategoryDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession, Principal principal) {
		log.info("Enter inside ServiceCategoryController.processAddServiceCategory() method.");
		serviceCategoryDTO.setServiceCategoryImage(categoryImage.getOriginalFilename());
		this.serviceCategoryService.addServiceCategory(serviceCategoryDTO, httpSession,principal);
		return "redirect:/product/products";
	}

	/**
	 * this method is used to open service-category listing page
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/serviceCategories")
	public String showServiceCategories(Model model,Principal principal) {
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal).getData();
		model.addAttribute("serviceCategories", serviceCategories);
		return "service/all-service-categories";
	}

	/**
	 * This method is used to remove service category
	 * 
	 * @author hemantr
	 * @date Jul 26, 2023
	 * @param productId
	 * @return
	 */
	@GetMapping("/remove/{serviceCategoryId}")
	public String deleteServiceCategory(@PathVariable Long serviceCategoryId, HttpSession httpSession) {
		log.info("Enter inside ServiceCategoryController.deleteServiceCategory() method.");
		CommonResponse<Boolean> removeServiceCategory = this.serviceCategoryService
				.removeServiceCategory(serviceCategoryId);
		if (removeServiceCategory.getData() && removeServiceCategory.getResult()) {
			httpSession.setAttribute("message", new Message(removeServiceCategory.getMessage(), "success"));
		} else {
			httpSession.setAttribute("message", new Message(removeServiceCategory.getMessage(), "danger"));
		}
		return "redirect:/product/products";
	}

	/**
	 * This method is used to open edit service category page
	 * 
	 * @author hemantr
	 * @date Jun 25, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{serviceCategoryId}")
	public String updateServiceCategory(@PathVariable Long serviceCategoryId, Model model) {
		log.info("Enter inside ServiceCategoryController.updateServiceCategory() method.");
		CommonResponse<ServiceCategoryDTO> serviceCategoryById = this.serviceCategoryService
				.getServiceCategoryById(serviceCategoryId);
		ServiceCategoryDTO data = serviceCategoryById.getData();
		model.addAttribute("serviceCategoryDTO", data);
		return "service/edit-service-category";
	}

	/**
	 * This method is used to update service category
	 * 
	 * @author hemantr
	 * @date Jul 25, 2023
	 * @param productDTO
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-update")
	public String processUpdateServiceCategory(@ModelAttribute ServiceCategoryDTO serviceCategoryDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession) {
		log.info("Enter inside ServiceCategoryController.processUpdateProduct() method.");
		this.serviceCategoryService.updateServiceCategory(serviceCategoryDTO);
		return "redirect:/product/products";
	}
}
