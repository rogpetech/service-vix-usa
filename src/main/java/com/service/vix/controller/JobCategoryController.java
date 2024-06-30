package com.service.vix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.service.vix.dto.JobCategoryDTO;
import com.service.vix.service.JobCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as a controller that open all the pages related to Job
 * Category
 * @author hemantr
 *
 */
@Controller
@Slf4j
@RequestMapping("/job-category")
public class JobCategoryController extends BaseController{
  
	@Autowired
	private JobCategoryService jobCategoryService;

	
	/**
	 * This method is used to open add-job-category page
	 * @author hemantr 
	 * @date Jun 19, 2023 
	 * @param model
	 * @return 
	 */
	@GetMapping("/add")
	public String addJobCategory(Model model) {
		log.info("Enter inside JobCategoryController.addJobCategory() method.");
		List<JobCategoryDTO> jobCategories = this.jobCategoryService.getJobCategories().getData();
		model.addAttribute("jobCategories", jobCategories);
		return "job/add-job-category";

	}


	/**
	 * This method is used to process add job category
	 * @author hemantr 
	 * @date Jun 19, 2023 
	 * @param jobCategoryDTO
	 * @param httpSession
	 * @return 
	 */
	@PostMapping("/process-add")
	public String processAddJobCategory(@ModelAttribute("productCategoryDTO") JobCategoryDTO jobCategoryDTO,
			HttpSession httpSession) {
		log.info("Enter inside JobCategoryController.processAddJobCategory() method.");
		this.jobCategoryService.addJobCategory(jobCategoryDTO, httpSession);
		return "redirect:/job-category/add";
	}

}
