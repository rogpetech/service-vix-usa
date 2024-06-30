package com.service.vix.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ProductDTO;

/**
 * This class is used as service class that have all the method declaration for
 * Product Entity
 */
@Component
public interface ProductService {

	/**
	 * This method is used to save product
	 * 
	 * @author ritiks
	 * @date Jun 20, 2023
	 * @return CommonResponse<ProductDTO>
	 * @param product
	 * @param multipartFile
	 * @param principal
	 * @return
	 * @exception Description
	 */
	CommonResponse<ProductDTO> saveProduct(ProductDTO product, MultipartFile multipartFile, Principal principal);

	/**
	 * This method is used to get all the Products
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @return
	 */
	CommonResponse<List<ProductDTO>> getProducts(Principal principal);

	/**
	 * This method is used to get Product By Id
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param productId
	 * @return
	 */
	CommonResponse<ProductDTO> getProductById(Long productId);

	/**
	 * This method is used to remove Product by id
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param productId
	 * @return
	 */
	CommonResponse<Boolean> removeProduct(Long productId);

	/**
	 * This method is used to update Product
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param productDTO
	 * @return
	 */
	CommonResponse<ProductDTO> updateProduct(ProductDTO productDTO);

	/**
	 * This method is used to find product by given search alphabet
	 * 
	 * @author ritiks
	 * @date Jun 22, 2023
	 * @return List<ProductDTO>
	 * @param productName
	 * @param principal
	 * @return
	 * @exception Description
	 */
	List<ProductDTO> searchProduct(String productName, Principal principal);

	/**
	 * This method is used to get product details by product name
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param productName
	 * @return CommonResponse<ProductDTO>
	 */
	CommonResponse<ProductDTO> getProductByProductName(String productName);

}
