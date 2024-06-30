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
import com.service.vix.dto.ServiceCategoryDTO;
import com.service.vix.mapper.ServiceCategoryMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.ServiceCategory;
import com.service.vix.repositories.ServiceCategoryRepository;
import com.service.vix.repositories.ServiceRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.ServiceCategoryService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

	@Autowired
	private ServiceCategoryRepository serviceCategoryRepository;
	@Autowired
	private Environment env;
	@Autowired
	private ServiceRepository serviceRepository;
	
	@Autowired
	private CommonService commonService;

	@Override
	public CommonResponse<ServiceCategoryDTO> addServiceCategory(ServiceCategoryDTO serviceCategoryDTO,
			HttpSession httpSession, Principal principal) {
		httpSession.setAttribute("message", new Message("Service Categpry addedd successfully", "success"));
		log.info("ServiceCategoryServiceImpl.addServiceCategory() method.");
		CommonResponse<ServiceCategoryDTO> response = new CommonResponse<>();
		Optional<ServiceCategory> serviceByCategoryName = this.serviceCategoryRepository
				.findByServiceCategoryNameAndIsDeletedFalse(serviceCategoryDTO.getServiceCategoryName());				

		if (serviceByCategoryName.isPresent()) {
			String msg = env.getProperty("service.category.already.exists");
			httpSession.setAttribute("message", new Message(msg, "error"));
			log.info(msg);
			response.setMessage(msg);
			response.setData(null);
			response.setResult(true);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save service category");
			ServiceCategory serviceCategory = ServiceCategoryMapper.INSTANCE
					.serviceCategoryDTOTosSrviceCategory(serviceCategoryDTO);
			if (serviceCategoryDTO.getServiceCategoryId() != null
					&& serviceCategoryDTO.getParentServiceCategoryId() > 0) {
				log.info("Parent Service category id added by user.");
				Optional<ServiceCategory> givenParentServiceCategory = this.serviceCategoryRepository
						.findById(serviceCategoryDTO.getParentServiceCategoryId());
				if (givenParentServiceCategory.isPresent()) {
					String msg = env.getProperty("parent.service.exists");
					log.info(msg);
					serviceCategory.setParentServiceCategory(givenParentServiceCategory.get());
				} else {
					String msg = env.getProperty("parent.service.does.not.exists");
					httpSession.setAttribute("message", new Message(msg, "error"));
					log.info(msg);
					response.setMessage(msg);
					response.setData(null);
					response.setResult(true);
					response.setStatus(HttpStatus.NOT_FOUND.value());
					return response;
				}
			}
			
			Organization organization = this.commonService.getLoggedInUserOrganization(principal);
			serviceCategory.setOrganization(organization);
			ServiceCategory savedServiceCategory = this.serviceCategoryRepository.save(serviceCategory);
			ServiceCategoryDTO savedServiceCategoryDTO = ServiceCategoryMapper.INSTANCE
					.serviceCategoryToServiceCategoryDTO(savedServiceCategory);
			String msg = env.getProperty("service.category.add.success");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setMessage(msg);
			response.setData(savedServiceCategoryDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;

		}

	}
	@Override
	public CommonResponse<List<ServiceCategoryDTO>> getServiceCategory(Principal principal) {
		log.info("Enter inside ServiceCategoryServiceImpl.getServiceCategories() method.");
		CommonResponse<List<ServiceCategoryDTO>> response = new CommonResponse<>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		List<ServiceCategory> serviceCategories = this.serviceCategoryRepository.findAllByIsDeletedFalseAndOrganization(organization);
		List<ServiceCategoryDTO> serviceCategoryDTOs = new ArrayList<ServiceCategoryDTO>();
		serviceCategories.forEach(
				pc -> serviceCategoryDTOs.add(ServiceCategoryMapper.INSTANCE.serviceCategoryToServiceCategoryDTO(pc)));
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(serviceCategoryDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	@Override
	public CommonResponse<ServiceCategoryDTO> getServiceCategoryById(Long serviceCategoryId) {
		log.info("Enter inside ServiceCategoryServiceImpl.getServiceCategoryById() method.");
		CommonResponse<ServiceCategoryDTO> response = new CommonResponse<>();
		if (serviceCategoryId != null && serviceCategoryId > 0) {
			ServiceCategoryDTO serviceCategoryDTO = new ServiceCategoryDTO();
			Optional<ServiceCategory> serviceCategoryOpt = this.serviceCategoryRepository.findById(serviceCategoryId);
			if (serviceCategoryOpt.isPresent())
				serviceCategoryDTO = ServiceCategoryMapper.INSTANCE
						.serviceCategoryToServiceCategoryDTO(serviceCategoryOpt.get());

			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(serviceCategoryDTO);
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

	public CommonResponse<Boolean> removeServiceCategory(Long serviceCategoryId) {
		log.info("Enter inside ServiceCategoryServiceImpl.removeserviceCategory() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		if (serviceCategoryId != null && serviceCategoryId > 0) {
			Optional<ServiceCategory> serviceCategoryOpt = this.serviceCategoryRepository.findById(serviceCategoryId);
			String msg = "";
			if (serviceCategoryOpt.isPresent()) {
				ServiceCategory serviceCategory = serviceCategoryOpt.get();

				boolean isCategoryAssociatedWithService = this.serviceRepository
						.existsByServiceCategoryAndIsDeletedFalse(serviceCategory);

				if (!isCategoryAssociatedWithService) {
					serviceCategory.setIsDeleted(true);
					serviceCategoryRepository.save(serviceCategory);
					log.info("record sucessfully deleted");
					msg = env.getProperty("service.category.delete.success");
				} else {
					msg = env.getProperty("service.category.associated.service");
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

	public CommonResponse<ServiceCategoryDTO> updateServiceCategory(ServiceCategoryDTO serviceCategoryDTO) {
		log.info("Enter inside ServiceCategoryServiceImpl.updateServiceCategory() method.");
		CommonResponse<ServiceCategoryDTO> response = new CommonResponse<>();
		Long serviceCategoryId = serviceCategoryDTO.getServiceCategoryId();
		if (serviceCategoryId != null && serviceCategoryId > 0) {
			log.info("Service category found by given Service Category details.");
			Optional<ServiceCategory> serviceCategoryOpt = this.serviceCategoryRepository.findById(serviceCategoryId);
			if (serviceCategoryOpt.isPresent()) {

				ServiceCategory serviceCategory = ServiceCategoryMapper.INSTANCE
						.serviceCategoryDTOTosSrviceCategory(serviceCategoryDTO);

				Long DBServiceCategoryId = serviceCategoryOpt.get().getId();

				serviceCategory.setId(DBServiceCategoryId);

				ServiceCategory savedServiceCategory = this.serviceCategoryRepository.save(serviceCategory);

				ServiceCategoryDTO savedServiceCategoryDTO = ServiceCategoryMapper.INSTANCE
						.serviceCategoryToServiceCategoryDTO(savedServiceCategory);

				String msg = env.getProperty("product.category.add.success");
				log.info(msg);
				response.setMessage(msg);
				response.setData(savedServiceCategoryDTO);
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
