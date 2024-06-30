package com.service.vix.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.service.vix.models.ProductCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Product DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

	private Long productId;

	private Long productCategoryId;

	private String productName;

	private String discription;

	private Float regularPrice;

	private Float memberPrice;

	private Float averageCost;

	private String productImage;

	private MultipartFile productImages;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

	private ProductCategory productCategory;

}
