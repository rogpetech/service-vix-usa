package com.service.vix.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as DTO for ProductCategory Entity
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductCategoryDTO {

	private Long productCategoryId;

	private String productCategoryName;

	private Long parentProductCategoryId;

	private String productCategoryImage;

	private String discription;

	private boolean activationStatus;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String createdBy;

	private String updatedBy;

}
