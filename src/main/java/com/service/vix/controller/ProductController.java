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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.dto.ProductDTO;
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.dto.ServiceDTO;
import com.service.vix.service.ProductCategoryService;
import com.service.vix.service.ProductService;
import com.service.vix.service.ServiceCategoryService;
import com.service.vix.service.ServicesService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as controller class that handle all the request for
 * product pages
 */
@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private ServicesService serviceService;

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	/**
	 * This method is used to open add product page
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public String addProduct(Model model, Principal principal) {
		log.info("Enter inside ProductController.addProduct() method.");
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		return "product/add-product";
	}

	/**
	 * This method is used to save product
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param productDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return @RequestParam("categoryImage") MultipartFile categoryImag6e,
	 */

	@PostMapping("/process-add")
	public String processAddProduct(@ModelAttribute ProductDTO productDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, Principal principal, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.processAddProductCategory() method.");
		productDTO.setProductImage(categoryImage.getOriginalFilename()); //
		this.productService.saveProduct(productDTO, categoryImage, principal);
		return "redirect:/product/products";
	}

	/**
	 * This method is used to save product
	 * 
	 * @author hemantr
	 * @date Jun 29, 2023
	 * @param productDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return
	 */
	@ResponseBody
	@PostMapping("/process-popup-addProduct")
	public ProductDTO processPopUpAddProduct(@ModelAttribute ProductDTO productDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, Principal principal, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.processAddProductCategory() method.");
		if (categoryImage != null)
			productDTO.setProductImage(categoryImage.getOriginalFilename()); //
		ProductDTO savedProductDTO = this.productService.saveProduct(productDTO, categoryImage, principal).getData();
		return savedProductDTO;
	}

	/**
	 * This method is used to show product listing page
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/products")
	public String showProducts(Model model, Principal principal) {
		log.info("Enter inside ProductCategoryController.showProducts() method.");
		List<ProductDTO> products = this.productService.getProducts(principal).getData();
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("products", products);
		List<ServiceDTO> services = this.serviceService.getServices(principal).getData();
		List<ServiceCategoryDTO> serviceCategories = this.serviceCategoryService.getServiceCategory(principal)
				.getData();
		model.addAttribute("services", services);
		model.addAttribute("serviceCategories", serviceCategories);
		return "product/products";
	}

	/**
	 * This method is used to show product category listing page
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param model
	 * @return
	 */
	@GetMapping("/productCategories")
	public String showProductsCategories(Model model, Principal principal) {
		log.info("Enter inside ProductCategoryController.showProductsCategories() method.");
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		return "product/product-categories";
	}

	/**
	 * This method is used to search Product by name
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 22, 2023
	 * @return List<ProductDTO>
	 * @param productName
	 * @param principal
	 * @return
	 * @exception Description
	 */
	@ResponseBody
	@GetMapping("/search/{productName}")
	public List<ProductDTO> searchProductByProductName(@PathVariable String productName, Principal principal) {
		log.info("Enter inside ProductController.searchProductByProductName() method.");
		return this.productService.searchProduct(productName, principal);
	}

	/**
	 * This method is used to open edit product page
	 * 
	 * @author hemantr
	 * @date Jul 5, 2023
	 * @param productId
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{productId}")
	public String updateProduct(@PathVariable Long productId, Model model, Principal principal) {
		log.info("Enter inside ProductCategoryController.searchProductByProductName() method.");
		CommonResponse<ProductDTO> productById = this.productService.getProductById(productId);
		ProductDTO data = new ProductDTO();
		if (productById.getResult() == true) {
			data = productById.getData();
			data.setProductCategoryId(data.getProductCategory().getId());
		}
		List<ProductCategoryDTO> productCategories = this.productCategoryService.getProductCategories(principal)
				.getData();
		model.addAttribute("productCategories", productCategories);
		model.addAttribute("productDto", data);
		return "product/edit-product";
	}

	/**
	 * This method is used to update products
	 * 
	 * @author hemantr
	 * @date Jul 5, 2023
	 * @param productDTO
	 * @param categoryImage
	 * @param httpSession
	 * @return
	 */
	@PostMapping("/process-update")
	public String processUpdateProduct(@ModelAttribute ProductDTO productDTO,
			@RequestParam("categoryImage") MultipartFile categoryImage, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.processUpdateProduct() method.");
		productDTO.setProductImage(categoryImage.getOriginalFilename()); //
		this.productService.updateProduct(productDTO);
		return "redirect:/product/products";
	}

	/**
	 * This method is used to remove product
	 * 
	 * @author hemantr
	 * @date Jul 6, 2023
	 * @param productId
	 * @return
	 */
	@GetMapping("/remove/{productId}")
	public String deleteProduct(@PathVariable Long productId, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryController.deleteProduct() method.");

		CommonResponse<Boolean> removeProduct = this.productService.removeProduct(productId);
		if (!removeProduct.getResult() && !removeProduct.getData()) {
			httpSession.setAttribute("message", new Message(removeProduct.getMessage(), "danger"));
		} else if (removeProduct.getData() && removeProduct.getResult()) {
			httpSession.setAttribute("message", new Message(removeProduct.getMessage(), "success"));
		}
		return "redirect:/product/products";
	}

}
