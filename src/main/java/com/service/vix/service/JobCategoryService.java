package com.service.vix.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.JobCategoryDTO;

import jakarta.servlet.http.HttpSession;

/**
 * This class is used as service class that have all the method declaration for
 * Job Category Entity
 */
@Component
public interface JobCategoryService {

	/**
	 * This method is used to add Job Category
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param jobCategoryDTO
	 * @param httpSession
	 * @return
	 */
	CommonResponse<JobCategoryDTO> addJobCategory(JobCategoryDTO jobCategoryDTO, HttpSession httpSession);

	/**
	 * This method is used to get all the Job Categories
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @return
	 */
	CommonResponse<List<JobCategoryDTO>> getJobCategories();

	/**
	 * This method is used to get Job Category By Id
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param JobCategoryId
	 * @return
	 */
	CommonResponse<JobCategoryDTO> getJobCategoryById(Long JobCategoryId);

	/**
	 * This method is used to remove Job Category by id
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param jobCategoryId
	 * @return
	 */
	public CommonResponse<Boolean> removeJobCategory(Long jobCategoryId);

	/**
	 * This method is used to update Job Category
	 * 
	 * @author hemantr
	 * @date Jun 20, 2023
	 * @param jobCategoryDTO
	 * @return
	 */
	CommonResponse<JobCategoryDTO> updateJobCategory(JobCategoryDTO jobCategoryDTO);

	/**
	 * This method is used to get job category by name
	 * 
	 * @author hemantr
	 * @date Jun 24, 2023
	 * @param jobCategoryName
	 * @return
	 */
	CommonResponse<JobCategoryDTO> getJobCategoryByName(String jobCategoryName);

}
