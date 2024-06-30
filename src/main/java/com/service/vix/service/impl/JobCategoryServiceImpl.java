package com.service.vix.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.JobCategoryDTO;
import com.service.vix.dto.Message;
import com.service.vix.mapper.JobCategoryMapper;
import com.service.vix.models.JobCategory;
import com.service.vix.repositories.JobCategoryRepository;
import com.service.vix.service.JobCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobCategoryServiceImpl implements JobCategoryService {

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Autowired
	private Environment env;

	@Override
	public CommonResponse<JobCategoryDTO> addJobCategory(JobCategoryDTO jobCategoryDTO, HttpSession httpSession) {
		log.info("Enter inside JobCategoryServiceImpl.addJobCategory() method.");
		CommonResponse<JobCategoryDTO> response = new CommonResponse<JobCategoryDTO>();
		Optional<JobCategory> jobByCategoryName = this.jobCategoryRepository
				.findByJobCategoryName(jobCategoryDTO.getJobCategoryName());

		if (jobByCategoryName.isPresent()) {
			String msg = env.getProperty("job.category.already.exists");
			httpSession.setAttribute("message", new Message(msg, "error"));
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save job category");
			JobCategory jobCategory = JobCategoryMapper.INSTANCE.jobCategoryDTOToJobCategory(jobCategoryDTO);
			if (jobCategoryDTO.getParentJobCategoryId() != null && jobCategoryDTO.getParentJobCategoryId() > 0) {
				log.info("Parent product category id added by user.");
				Optional<JobCategory> givenParentJobCategory = this.jobCategoryRepository
						.findById(jobCategoryDTO.getParentJobCategoryId());
				if (givenParentJobCategory.isPresent()) {
					String msg = env.getProperty("parent.product.exists");
					log.info(msg);
					jobCategory.setParentjobCategory(givenParentJobCategory.get());
				}
			}
			JobCategory savedJobCategory = this.jobCategoryRepository.save(jobCategory);
			JobCategoryDTO savedJobCategoryDTO = JobCategoryMapper.INSTANCE
					.jobCategoryToJobCategoryDTO(savedJobCategory);
			String msg = env.getProperty("product.category.add.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(savedJobCategoryDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
	}

	@Override
	public CommonResponse<List<JobCategoryDTO>> getJobCategories() {
		log.info("Enter inside JobCategoryServiceImpl.getJobCategories() method.");
		CommonResponse<List<JobCategoryDTO>> response = new CommonResponse<>();
		List<JobCategory> jobCategories = this.jobCategoryRepository.findAll();
		List<JobCategoryDTO> jobCategoryDTOs = new ArrayList<JobCategoryDTO>();
		jobCategories.forEach(pc -> jobCategoryDTOs.add(JobCategoryMapper.INSTANCE.jobCategoryToJobCategoryDTO(pc)));
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(jobCategoryDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	@Override
	public CommonResponse<JobCategoryDTO> getJobCategoryById(Long JobCategoryId) {
		log.info("Enter inside JobCategoryServiceImpl.getJobCategoryById() method.");
		CommonResponse<JobCategoryDTO> response = new CommonResponse<>();
		if (JobCategoryId != null && JobCategoryId > 0) {
			JobCategoryDTO jobCategoryDTO = new JobCategoryDTO();
			Optional<JobCategory> jobCategoryOpt = this.jobCategoryRepository.findById(JobCategoryId);
			if (jobCategoryOpt.isPresent())
				jobCategoryDTO = JobCategoryMapper.INSTANCE.jobCategoryToJobCategoryDTO(jobCategoryOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(jobCategoryDTO);
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

	@Override
	public CommonResponse<Boolean> removeJobCategory(Long jobCategoryId) {
		log.info("Enter inside ProductCategoryServiceImpl.removeProductCategory() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		if (jobCategoryId != null && jobCategoryId > 0) {
			Optional<JobCategory> jobCategoryOpt = this.jobCategoryRepository.findById(jobCategoryId);
			String msg = "";
			if (jobCategoryOpt.isPresent()) {
				JobCategory jobCategory = jobCategoryOpt.get();
				this.jobCategoryRepository.delete(jobCategory);
				msg = env.getProperty("record.delete.success") + " 1.";
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

	@Override
	public CommonResponse<JobCategoryDTO> updateJobCategory(JobCategoryDTO jobCategoryDTO) {
	
		log.info("Enter inside ProductCategoryServiceImpl.updateProductCategory() method.");
		CommonResponse<JobCategoryDTO> response = new CommonResponse<>();
		Long jobCategoryId = jobCategoryDTO.getJobCategoryId();
		if (jobCategoryId != null && jobCategoryId > 0) {
			log.info("Product category found by given Product Category details.");
			Optional<JobCategory>jobCategoryOpt = this.jobCategoryRepository.findById(jobCategoryId);
			if (jobCategoryOpt.isPresent()) {

				JobCategory jobCategory=JobCategoryMapper.INSTANCE.jobCategoryDTOToJobCategory(jobCategoryDTO);

				Long DBJobCategoryId = jobCategoryOpt.get().getId();

				jobCategory.setId(DBJobCategoryId);

				JobCategory savedJobCategory = this.jobCategoryRepository.save(jobCategory);
				JobCategoryDTO savedJobCategoryDTO=JobCategoryMapper.INSTANCE.jobCategoryToJobCategoryDTO(savedJobCategory);

				String msg = env.getProperty("job.category.update.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedJobCategoryDTO);
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

	@Override
	public CommonResponse<JobCategoryDTO> getJobCategoryByName(String jobCategoryName) {
		log.info("Enter inside JobCategoryServiceImpl.getJobCategoryByName() method.");
		CommonResponse<JobCategoryDTO> response = new CommonResponse<>();
		if (jobCategoryName != null) {
			JobCategoryDTO jobCategoryDTO = new JobCategoryDTO();
			Optional<JobCategory> jobCategoryOpt = this.jobCategoryRepository.findByJobCategoryName(jobCategoryName);
			if (jobCategoryOpt.isPresent())
				jobCategoryDTO = JobCategoryMapper.INSTANCE.jobCategoryToJobCategoryDTO(jobCategoryOpt.get());
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(jobCategoryDTO);
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
