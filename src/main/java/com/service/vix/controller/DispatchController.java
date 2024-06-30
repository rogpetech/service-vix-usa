/* 
 * ===========================================================================
 * File Name DispatchController.java
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
 @RequestMapping("/dispatch")
 public class DispatchController extends BaseController {

	@GetMapping("/dispatchList")
	public String jobs(Model model) {
		return "dispatch/dispatch-list";
	}
}
