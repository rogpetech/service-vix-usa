package com.service.vix.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.Message;
import com.service.vix.dto.ProductCategoryDTO;
import com.service.vix.mapper.ProductCategoryMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.ProductCategory;
import com.service.vix.repositories.ProductCategoryRepository;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.ProductCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to define all the method related to Product Category
 * Service
 */
@Service
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private Environment env;
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CommonService commonService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductCategoryService#addProductCategory(com.service
	 * .vix.dto.ProductCategoryDTO)
	 */
	@Override
	public CommonResponse<ProductCategoryDTO> addProductCategory(ProductCategoryDTO productCategoryDTO,
			HttpSession httpSession, Principal principal) {
		log.info("Enter inside ProductCategoryServiceImpl.addProductCategory() method.");
		CommonResponse<ProductCategoryDTO> response = new CommonResponse<>();
		Optional<ProductCategory> productByCategoryName = this.productCategoryRepository
				.findByProductCategoryNameAndIsDeletedFalse(productCategoryDTO.getProductCategoryName());
		if (productByCategoryName.isPresent()) {
			String msg = env.getProperty("product.category.already.exists");
			httpSession.setAttribute("message", new Message(msg, "error"));
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save product category");
			ProductCategory productCategory = ProductCategoryMapper.INSTANCE
					.productCategoryDTOToProductCategory(productCategoryDTO);
			if (productCategoryDTO.getParentProductCategoryId() != null
					&& productCategoryDTO.getParentProductCategoryId() > 0) {
				log.info("Parent product category id added by user.");
				Optional<ProductCategory> givenParentProductCategory = this.productCategoryRepository
						.findById(productCategoryDTO.getParentProductCategoryId());
				if (givenParentProductCategory.isPresent()) {
					String msg = env.getProperty("parent.product.exists");
					log.info(msg);
					productCategory.setParentProductCategory(givenParentProductCategory.get());
				} else {
					String msg = env.getProperty("parent.product.does.not.exists");
					httpSession.setAttribute("message", new Message(msg, "error"));
					log.info(msg);
					response.setMessage(msg);
					response.setData(null);
					response.setResult(true);
					response.setStatus(HttpStatus.NOT_FOUND.value());
					return response;
				}
			}

			log.info("Going to fetch logged In User & Organization.");
			Organization organization = this.commonService.getLoggedInUserOrganization(principal);
			productCategory.setOrganization(organization);
			ProductCategory savedProductCategory = this.productCategoryRepository.save(productCategory);
			ProductCategoryDTO savedProductCategoryDTO = ProductCategoryMapper.INSTANCE
					.productCategoryToProductCategoryDTO(savedProductCategory);
			String msg = env.getProperty("product.category.add.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(savedProductCategoryDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductCategoryService#getProductCategories(java.
	 * security.Principal)
	 */
	@Override
	public CommonResponse<List<ProductCategoryDTO>> getProductCategories(Principal principal) {
		log.info("Enter inside ProductCategoryServiceImpl.getProductCategories() method.");
		CommonResponse<List<ProductCategoryDTO>> response = new CommonResponse<>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<ProductCategory> productCategories = this.productCategoryRepository
				.findAllByIsDeletedFalseAndOrganization(organization);
		List<ProductCategoryDTO> productCategoryDTOs = new ArrayList<ProductCategoryDTO>();
		productCategories.forEach(
				pc -> productCategoryDTOs.add(ProductCategoryMapper.INSTANCE.productCategoryToProductCategoryDTO(pc)));
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(productCategoryDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductCategoryService#getProductCategoryById(java.
	 * lang.Long)
	 */
	@Override
	public CommonResponse<ProductCategoryDTO> getProductCategoryById(Long productCategoryId) {
		log.info("Enter inside ProductCategoryServiceImpl.getProductCategoryById() method.");
		CommonResponse<ProductCategoryDTO> response = new CommonResponse<>();
		if (productCategoryId != null && productCategoryId > 0) {
			ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
			Optional<ProductCategory> productCategoryOpt = this.productCategoryRepository.findById(productCategoryId);
			if (productCategoryOpt.isPresent())
				productCategoryDTO = ProductCategoryMapper.INSTANCE
						.productCategoryToProductCategoryDTO(productCategoryOpt.get());

			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(productCategoryDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductCategoryService#removeProductCategory(java.
	 * lang.Long)
	 */
	@Override
	public CommonResponse<Boolean> removeProductCategory(Long productCategoryId) {
		log.info("Enter inside ProductCategoryServiceImpl.removeProductCategory() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		if (productCategoryId != null && productCategoryId > 0) {
			Optional<ProductCategory> productCategoryOpt = this.productCategoryRepository.findById(productCategoryId);
			String msg = "";
			if (productCategoryOpt.isPresent()) {
				ProductCategory productCategory = productCategoryOpt.get();

				boolean isCategoryAssociatedWithProduct = this.productRepository
						.existsByProductCategoryAndIsDeletedFalse(productCategory);

				if (!isCategoryAssociatedWithProduct) {
					productCategory.setIsDeleted(true);
					productCategoryRepository.save(productCategory);
					log.info("record sucessfully deleted");
					msg = env.getProperty("product.category.delete.success");
				} else {
					msg = env.getProperty("product.category.associated.product");
					response.setMessage(msg);
					response.setData(false);
					response.setResult(false);
					response.setStatus(HttpStatus.ALREADY_REPORTED.value());
					return response;
				}
			} else {
				msg = env.getProperty("record.delete.failed") + " 0.";
			}
			log.info(msg);
			response.setMessage(msg);
			response.setData(true);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(false);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductCategoryService#updateProductCategory(com.
	 * service.vix.dto.ProductCategoryDTO)
	 */
	@Override
	public CommonResponse<ProductCategoryDTO> updateProductCategory(ProductCategoryDTO productCategoryDTO) {
		log.info("Enter inside ProductCategoryServiceImpl.updateProductCategory() method.");
		CommonResponse<ProductCategoryDTO> response = new CommonResponse<>();
		Long productCategoryId = productCategoryDTO.getProductCategoryId();
		if (productCategoryId != null && productCategoryId > 0) {
			log.info("Product category found by given Product Category details.");
			Optional<ProductCategory> productCategoryOpt = this.productCategoryRepository.findById(productCategoryId);
			if (productCategoryOpt.isPresent()) {
				ProductCategory productCategory = ProductCategoryMapper.INSTANCE
						.productCategoryDTOToProductCategory(productCategoryDTO);
				ProductCategory DBProductCategory = productCategoryOpt.get();
				Long DBProductCategoryId = DBProductCategory.getId();
				productCategory.setId(DBProductCategoryId);
				productCategory.setOrganization(DBProductCategory.getOrganization());

				ProductCategory savedProductCategory = this.productCategoryRepository.save(productCategory);
				ProductCategoryDTO savedProductCategoryDTO = ProductCategoryMapper.INSTANCE
						.productCategoryToProductCategoryDTO(savedProductCategory);
				String msg = env.getProperty("product.category.update.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedProductCategoryDTO);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			} else {
				String msg = env.getProperty("record.not.found");
				log.info(msg);
				response.setMessage(msg);
				response.setData(null);
				response.setResult(true);
				response.setStatus(HttpStatus.NOT_FOUND.value());
				return response;
			}
		} else {
			String msg = env.getProperty("record.not.found");
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}
	}

}
