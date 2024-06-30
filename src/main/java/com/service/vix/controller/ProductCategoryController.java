package com.service.vix.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
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
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.service.ProductCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as a controller that open all the pages related to Product
 * Category
 */
@Controller
@Slf4j
@RequestMapping("/product-category")
public class ProductCategoryController extends BaseController {

	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * This method is used to open add-product-category page
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return String
	 * @return
	 * @exception Description
	 */
	@GetMapping("/add")
	public String addProductCategory(Model model, Principal principal) {
		log.info("Enter inside ProductCategoryController.addProductCategory() method.");
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		return "product/add-product-category";
	}

	/**
	 * This method is used to process add product category
	 * 
	 * @author hemantr
	 * @date Jun 7, 2023
	 * @return String
	 * @param productCategoryDTO
	 * @return
	 * @exception Description
	 */
	@PostMapping("/process-add")
	public String processAddProductCategory(@ModelAttribute("productCategoryDTO") ProductCategoryDTO productCategoryDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession, Principal principal) {
		log.info("Enter inside ProductCategoryController.processAddProductCategory() method.");
		productCategoryDTO.setProductCategoryImage(categoryImage.getOriginalFilename());
		this.productCategoryService.addProductCategory(productCategoryDTO, httpSession, principal);
		return "redirect:/product/products";
	}

	/**
	 * Add method description here
	 * 
	 * @author ritiks
	 * @date Jun 7, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/edit-product-categories")
	public String editProductCategoryrForm(Model model) {
		return "/product/edit-product";
	}

	/**
	 * This method is used to remove product category
	 * 
	 * @author hemantr
	 * @date Jul 6, 2023
	 * @param productId
	 * @return
	 */
	@GetMapping("/remove/{productCategoryId}")
	public String deleteProduct(@PathVariable Long productCategoryId, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.deleteProduct() method.");
		CommonResponse<Boolean> removeProductCategory = this.productCategoryService
				.removeProductCategory(productCategoryId);
		if (removeProductCategory.getData() && removeProductCategory.getResult()) {
			httpSession.setAttribute("message", new Message(removeProductCategory.getMessage(), "success"));
		} else {
			httpSession.setAttribute("message", new Message(removeProductCategory.getMessage(), "danger"));
		}
		return "redirect:/product/products";
	}

	/**
	 * This method is used to open edit product category page
	 * 
	 * @author hemantr
	 * @date Jun 25, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{productCategoryId}")
	public String updateProductCategory(@PathVariable Long productCategoryId, Model model) {
		log.info("Enter inside ProductCategoryController.updateProductCategory() method.");
		CommonResponse<ProductCategoryDTO> productCategoryById = this.productCategoryService
				.getProductCategoryById(productCategoryId);
		ProductCategoryDTO data = productCategoryById.getData();
		model.addAttribute("productCategoryDTO", data);
		return "product/edit-productCategory";
	}

	/**
	 * This method is used to update product category
	 * 
	 * @author hemantr
	 * @date Jul 25, 2023
	 * @param productDTO
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-update")
	public String processUpdateProductCategory(@ModelAttribute ProductCategoryDTO productCategoryDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.processUpdateProduct() method.");
		// productCategoryDTO.setProductImage(categoryImage.getOriginalFilename()); //
		this.productCategoryService.updateProductCategory(productCategoryDTO);
		return "redirect:/product/products";
	}
}
