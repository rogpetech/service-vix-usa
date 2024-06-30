package com.service.vix.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.models.JobCategory;

/**
 * 
 * This class is used as a repository to handle all the database methods related
 * to Job
 * 
 * @author hemantr
 *
 */
@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

	/**
	 * This method is used to get Job Category By name
	 * 
	 * @param jobCategoryName
	 * @return
	 */
	Optional<JobCategory> findByJobCategoryName(String jobCategoryName);
}
