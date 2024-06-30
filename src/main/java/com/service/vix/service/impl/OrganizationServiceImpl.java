package com.service.vix.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ImageUtility;
import com.service.vix.dto.Message;
import com.service.vix.dto.OrganizationDTO;
import com.service.vix.dto.OrganizationPlanDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.enums.ImageUploadDirectory;
import com.service.vix.mapper.OrganizationMapper;
import com.service.vix.mapper.OrganizationPlanMapper;
import com.service.vix.mapper.UserMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.OrganizationPlan;
import com.service.vix.models.Role;
import com.service.vix.models.User;
import com.service.vix.repositories.OrganizationRepository;
import com.service.vix.repositories.RoleRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.OrganizationService;
import com.service.vix.utility.SaveImage;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to define all the methods related to organization
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private Environment env;
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private SaveImage saveImage;

	@Value("${role.organization}")
	private String organizationRoleStr;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.OrganizationService#saveOrganization(com.service.vix.
	 * dto.OrganizationDTO)
	 */
	@Override
	public CommonResponse<UserDTO> saveOrganization(UserDTO userDTO) {

		log.info("Enter inside OrganizationServiceImpl.saveOrganization() method.");
		CommonResponse<UserDTO> response = new CommonResponse<UserDTO>();

		log.info("Going to check organization details is correct or not");
		Boolean existsByName = this.userRepository.existsByUsername(userDTO.getUsername());
		log.info("Organization Exists By Username | " + existsByName);
		Boolean existsByEmail = this.userRepository.existsByEmail(userDTO.getEmail());
		log.info("Organization Exists By Email | " + existsByEmail);

		// Check that given organization is register by given name or email
		if (existsByEmail || existsByName) {
			log.info("Going to filterd out organization");
			if (existsByEmail)
				response.setMessage(env.getProperty("org.email.already.exists"));
			if (existsByName)
				response.setMessage(env.getProperty("org.name.already.exists"));

			log.info("Organization havn't proper details for registeration  |  Error | " + response.getMessage());
			response.setData(userDTO);
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("User have proper details for registration.");
			User givenUser = UserMapper.INSTANCE.userDtoToUser(userDTO);
			givenUser.setPassword(encoder.encode(userDTO.getPassword()));

			OrganizationDTO organizationDTO = userDTO.getOrganizationDTO();
			Organization organization = OrganizationMapper.INSTANCE.organizationDtoToOrganization(organizationDTO);

			givenUser.setEmail(organization.getOrgEmail());
			givenUser.setMobileNum(organization.getOrgMobNum());

			OrganizationPlanDTO organizationPlanDTO = organizationDTO.getOrganizationPlanDTO();
			OrganizationPlan organizationPlan = OrganizationPlanMapper.INSTANCE
					.organizationPlanDtoToOrganizationPlan(organizationPlanDTO);

			organization.setOrganizationPlan(organizationPlan);
			givenUser.setOrganizationDetails(organization);

			// Give by default role to given organization (ROLE : ROLE_ORGANIZATION)
			Role organizationRole = roleRepository.findByName(organizationRoleStr).get();
			givenUser.setRole(organizationRole);

			log.info("Going to save image on server.");
			ImageUtility imageUtility = new ImageUtility();
			imageUtility.setFile(userDTO.getOrganizationLogoMultipart());
			imageUtility.setFileName(userDTO.getOrganizationLogoMultipart().getOriginalFilename());
			String orgLogoIdentifier = userDTO.getUsername() + "-" + LocalDateTime.now().toString().substring(0, 10)
					+ ".png";
			imageUtility.setUniqueIdentifier(orgLogoIdentifier);
			imageUtility.setImageUploadDirectory(ImageUploadDirectory.ORGANIZATION);
			CommonResponse<Boolean> savedImageResponse = this.saveImage.saveImage(imageUtility);
			log.info(savedImageResponse.getMessage());
			if (savedImageResponse.getData()) {
				userDTO.getOrganizationDTO().setOrgLogo(orgLogoIdentifier);
				givenUser.getOrganizationDetails().setOrgLogo(orgLogoIdentifier);
			}
			// Save organization
			User savedUser = null;
			try {
				savedUser = this.userRepository.save(givenUser);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Convert into DTO
			UserDTO savedUserDTO = UserMapper.INSTANCE.userToUserDTO(savedUser);

			response.setMessage(env.getProperty("org.success"));
			response.setData(savedUserDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.OrganizationService#getOrganizationByName(java.lang.
	 * String)
	 */
	@Override
	public CommonResponse<OrganizationDTO> getOrganizationByName(String organizationName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.OrganizationService#updateOrganization(com.service.
	 * vix.dto.UserDTO, jakarta.servlet.http.HttpSession)
	 */
	@Override
	public CommonResponse<UserDTO> updateOrganization(UserDTO userDTO, HttpSession httpSession) {
		log.info("Enter inside ProductCategoryServiceImpl.updateOrganization() method.");
		CommonResponse<UserDTO> response = new CommonResponse<>();
		Long dbProfiletId = userDTO.getId();
		Optional<User> profileOpt = this.userRepository.findById(dbProfiletId);
		if (profileOpt.isPresent()) {
			User givenSavedUser = profileOpt.get();
			User givenUser = UserMapper.INSTANCE.userDtoToUser(userDTO);
			if (userDTO.getOrganizationDTO() != null) {
				OrganizationDTO organizationDTO = userDTO.getOrganizationDTO();
				Organization organization = OrganizationMapper.INSTANCE.organizationDtoToOrganization(organizationDTO);
				OrganizationPlanDTO organizationPlanDTO = organizationDTO.getOrganizationPlanDTO();
				OrganizationPlan organizationPlan = OrganizationPlanMapper.INSTANCE
						.organizationPlanDtoToOrganizationPlan(organizationPlanDTO);
				organization.setOrganizationPlan(organizationPlan);
				givenUser.setOrganizationDetails(organization);
			}

			log.info("Going to save image on server.");

			// Save Organization Logo here
			if (userDTO.getOrganizationDTO() != null && userDTO.getOrganizationLogoMultipart() != null) {
				ImageUtility imageUtility = new ImageUtility();
				imageUtility.setFile(userDTO.getOrganizationLogoMultipart());
				imageUtility.setFileName(userDTO.getOrganizationLogoMultipart().getOriginalFilename());
				String orgLogoIdentifier = userDTO.getOrganizationDTO().getOrgName() + "-"
						+ LocalDateTime.now().toString().substring(0, 10) + ".png";
				imageUtility.setUniqueIdentifier(orgLogoIdentifier);
				imageUtility.setImageUploadDirectory(ImageUploadDirectory.ORGANIZATION);
				try {
					CommonResponse<Boolean> savedOrgLogoResponse = new CommonResponse<>();
					if (userDTO.getOrganizationLogoMultipart().getOriginalFilename().length() > 0) {
						savedOrgLogoResponse = this.saveImage.saveImage(imageUtility);
						if (savedOrgLogoResponse.getResult()) {
							userDTO.getOrganizationDTO().setOrgLogo(orgLogoIdentifier);
							givenUser.getOrganizationDetails().setOrgLogo(orgLogoIdentifier);
						}
						log.info(savedOrgLogoResponse.getMessage());
					} else {
						userDTO.getOrganizationDTO().setOrgLogo(givenSavedUser.getOrganizationDetails().getOrgLogo());
						givenUser.getOrganizationDetails()
								.setOrgLogo(givenSavedUser.getOrganizationDetails().getOrgLogo());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error(env.getProperty("image.saved.failed") + "  | ORGANIZATION ");
				}
			}

			// Save Profile Picture
			if (userDTO.getUserProfileMultipart() != null) {
				ImageUtility imageUtility = new ImageUtility();
				imageUtility.setFile(userDTO.getUserProfileMultipart());
				imageUtility.setFileName(userDTO.getUserProfileMultipart().getOriginalFilename());
				String userProfileIdentifier = userDTO.getUsername() + "-"
						+ LocalDateTime.now().toString().substring(0, 10) + ".png";
				imageUtility.setUniqueIdentifier(userProfileIdentifier);
				imageUtility.setImageUploadDirectory(ImageUploadDirectory.USER);
				try {
					CommonResponse<Boolean> savedUserProfilePictureResponse = new CommonResponse<>();
					if (userDTO.getUserProfileMultipart().getOriginalFilename().length() > 0) {
						savedUserProfilePictureResponse = this.saveImage.saveImage(imageUtility);
						if (savedUserProfilePictureResponse.getResult()) {
							userDTO.setUserProfilePictureURL(userProfileIdentifier);
							givenUser.setProfileURL(userProfileIdentifier);
						}
						log.info(savedUserProfilePictureResponse.getMessage());
					} else {
						userDTO.setUserProfilePictureURL(givenSavedUser.getProfileURL());
						givenUser.setProfileURL(givenSavedUser.getProfileURL());
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error(env.getProperty("image.saved.failed") + "  | USER ");
				}
			}
			log.info("Image(S) saved successfully on server.");

			givenUser.setId(userDTO.getId());
			givenUser.setPassword(givenSavedUser.getPassword());
			givenUser.setRole(givenSavedUser.getRole());
			if (userDTO.getOrganizationDTO() != null)
				givenUser.getOrganizationDetails().setId(userDTO.getOrganizationDTO().getId());
			User savedUser = null;
			try {
				savedUser = this.userRepository.save(givenUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			UserDTO savedUserDTO = UserMapper.INSTANCE.userToUserDTO(savedUser);
			String msg = env.getProperty("profile.update.success");
			response.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "success"));
			response.setData(savedUserDTO);
			response.setResult(true);
			response.setStatus(HttpStatus.OK.value());
			return response;
		}
		String msg = env.getProperty("profile.update.failed");
		httpSession.setAttribute("message", new Message(msg, "danger"));
		response.setMessage(msg);
		response.setResult(false);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.UserService#checkOrganizationWithOrganizationName(
	 * java.lang.String)
	 */
	@Override
	public Boolean checkOrganizationWithOrganizationName(String orgName) {
		if (orgName != null && !orgName.equals(""))
			return this.organizationRepository.existsByOrgName(orgName);

		return false;
	}

}
