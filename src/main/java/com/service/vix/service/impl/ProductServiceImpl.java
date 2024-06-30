package com.service.vix.service.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ImageUtility;
import com.service.vix.dto.ProductDTO;
import com.service.vix.enums.ImageUploadDirectory;
import com.service.vix.mapper.ProductMapper;
import com.service.vix.models.Estimate;
import com.service.vix.models.Organization;
import com.service.vix.models.Product;
import com.service.vix.models.ProductCategory;
import com.service.vix.repositories.EstimateRepository;
import com.service.vix.repositories.ProductCategoryRepository;
import com.service.vix.repositories.ProductRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.ProductService;
import com.service.vix.utility.SaveImage;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to define all the method related to Product Service
 * 
 * @param <E>
 */

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	@Autowired
	private EstimateRepository estimateRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private Environment env;
	@Autowired
	private SaveImage saveImage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ProductService#saveProduct(com.service.vix.dto.
	 * ProductDTO, org.springframework.web.multipart.MultipartFile,
	 * java.security.Principal)
	 */
	@Override
	public CommonResponse<ProductDTO> saveProduct(ProductDTO productDto, MultipartFile file, Principal principal) {
		log.info("Enter inside ProductServiceImpl.saveProducts() method.");
		CommonResponse<ProductDTO> response = new CommonResponse<ProductDTO>();
		boolean existByProductName = productRepository.existsByProductName(productDto.getProductName());
		log.info("Product Exists By name | " + existByProductName);
		if (existByProductName) {
			response.setMessage(env.getProperty("product.name.already.exists"));
			response.setData(null);
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save product category");
			Optional<ProductCategory> productCategoryOpt = productCategoryRepository
					.findById(productDto.getProductCategoryId());

			Product product = ProductMapper.INSTANCE.productDTOToProduct(productDto);
			product.setProductCategory(productCategoryOpt.get());

			log.info("Going to fetch logged In User & Organization.");
			Organization organization = this.commonService.getLoggedInUserOrganization(principal);
			product.setOrganization(organization);

			ImageUtility imageUtility = new ImageUtility();
			imageUtility.setFile(productDto.getProductImages());
			imageUtility.setFileName(productDto.getProductName());
			String staffProductImage = productDto.getProductImage() + "-"
					+ LocalDateTime.now().toString().substring(0, 10) + ".png";
			imageUtility.setUniqueIdentifier(staffProductImage);
			imageUtility.setImageUploadDirectory(ImageUploadDirectory.PRODUCT);
			this.saveImage.saveImage(imageUtility);

			ProductMapper.INSTANCE.productToProductDTO(this.productRepository.save(product));

			response.setMessage(env.getProperty("product.add.success"));
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ProductService#getProducts()
	 */
	@Override
	public CommonResponse<List<ProductDTO>> getProducts(Principal principal) {
		log.info("Enter inside ProductServiceImpl.getProducts() method.");
		CommonResponse<List<ProductDTO>> response = new CommonResponse<>();

		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Product> products = this.productRepository.findAllByIsDeletedFalseAndOrganization(organization);
		List<ProductDTO> productDtos = new ArrayList<ProductDTO>();
		products.forEach(pc -> {
			ProductDTO productDTO = ProductMapper.INSTANCE.productToProductDTO(pc);
			productDtos.add(productDTO);
		});

		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(productDtos);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ProductService#getProductById(java.lang.Long)
	 */
	@Override
	public CommonResponse<ProductDTO> getProductById(Long productId) {
		log.info("Enter inside ProductServiceImpl.getProductById() method.");
		CommonResponse<ProductDTO> response = new CommonResponse<>();
		if (productId != null && productId > 0) {
			ProductDTO productDTO = new ProductDTO();
			Optional<Product> productOpt = this.productRepository.findById(productId);
			if (productOpt.isPresent())
				productDTO = ProductMapper.INSTANCE.productToProductDTO(productOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(productDTO);
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
	 * @see com.service.vix.service.ProductService#removeProduct(java.lang.Long)
	 */
	@Override
	@Transactional
	public CommonResponse<Boolean> removeProduct(Long productId) {
		log.info("Enter inside ProductServiceImpl.removeProduct() method.");
		CommonResponse<Boolean> response = new CommonResponse<Boolean>();
		if (productId != null && productId > 0) {
			String msg = "";
			Optional<Product> productOpt = this.productRepository.findById(productId);
			if (productOpt.isPresent()) {
				Product product = productOpt.get();
				Optional<Estimate> estimateOpt = this.estimateRepository.findByProductName(product.getProductName());
				if (estimateOpt.isEmpty()) {
					product.setIsDeleted(true);
					productRepository.save(product);
					msg = env.getProperty("product.delete.success");
				} else {
					msg = env.getProperty("product.associated.product");
					response.setMessage(msg);
					response.setData(false);
					response.setResult(false);
					response.setStatus(HttpStatus.ALREADY_REPORTED.value());
					return response;
				}
			} else {
				msg = env.getProperty("record.delete.success") + " 0.";
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
	 * com.service.vix.service.ProductService#updateProduct(com.service.vix.dto.
	 * ProductDTO)
	 */
	@Override
	public CommonResponse<ProductDTO> updateProduct(ProductDTO productDTO) {
		log.info("Enter inside ProductCategoryServiceImpl.updateProduct() method.");
		CommonResponse<ProductDTO> response = new CommonResponse<>();
		Long productId = productDTO.getProductId();
		if (productId != null && productId > 0) {
			log.info("Product  found by given Product  details.");
			Optional<Product> productOpt = this.productRepository.findById(productId);
			if (productOpt.isPresent()) {
				Optional<ProductCategory> productCategorie = this.productCategoryRepository
						.findById(productDTO.getProductCategoryId());
				Product product = ProductMapper.INSTANCE.productDTOToProduct(productDTO);
				if (productCategorie.isPresent()) {
					product.setProductCategory(productCategorie.get());
				}
				Product DBProduct = productOpt.get();
				Long DBProductId = DBProduct.getId();
				product.setId(DBProductId);
				product.setOrganization(DBProduct.getOrganization());
				Product savedProduct = this.productRepository.save(product);

				ProductDTO savedProductDTO = ProductMapper.INSTANCE.productToProductDTO(savedProduct);
				String msg = env.getProperty("product.update.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedProductDTO);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.ProductService#searchProduct(java.lang.String,
	 * java.security.Principal)
	 */
	@Override
	public List<ProductDTO> searchProduct(String productName, Principal principal) {
		log.info("Enter inside ProductServiceImpl.searchProduct() method.");
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<Product> searchProductList = this.productRepository
				.findByProductNameLikeAndIsDeletedFalseAndOrganization("%" + productName + "%", organization);
		List<ProductDTO> searchProductDTOs = new ArrayList<>();
		searchProductList.stream().forEach(p -> searchProductDTOs.add(ProductMapper.INSTANCE.productToProductDTO(p)));
		return searchProductDTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.ProductService#getProductByProductName(java.lang.
	 * String)
	 */
	@Override
	public CommonResponse<ProductDTO> getProductByProductName(String productName) {
		log.info("Enter inside ProductServiceImpl.getProductByProductName() method.");
		CommonResponse<ProductDTO> response = new CommonResponse<>();
		if (productName != null) {
			ProductDTO productDTO = new ProductDTO();
			Optional<Product> productOpt = this.productRepository.findByProductName(productName);
			if (productOpt.isPresent())
				productDTO = ProductMapper.INSTANCE.productToProductDTO(productOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(productDTO);
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
}
