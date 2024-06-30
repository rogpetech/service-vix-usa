package com.service.vix.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * This class is used as DTO for JobCategory Entity
 * 
 * @author hemantr
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobCategoryDTO {

	private String createdBy;

	private String updatedBy;

	private Long jobCategoryId;

	private String jobCategoryName;

	private Long parentJobCategoryId;

	private boolean activationStatus;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
