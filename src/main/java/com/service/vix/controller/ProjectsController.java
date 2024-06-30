/* 
 * ===========================================================================
 * File Name ProjectsController.java
 * 
 * Created on Aug 29, 2023
 * ===========================================================================
 */
 /**
 * Class Information
 * @author ritiks
 * @version 1.2 - Aug 29, 2023
 */
 package com.service.vix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
 @RequestMapping("/project")
 public class ProjectsController extends BaseController{

	/**
	 * This method is used to open project list page
	 * @author ritiks
	 * @date Aug 29, 2023
	 * @return String 
	 * @param model
	 * @return
	 * @exception 
	 * Description
	 */
	@GetMapping("/projectList")
	public String jobs(Model model) {
		return "project/project-list";
	}

}
