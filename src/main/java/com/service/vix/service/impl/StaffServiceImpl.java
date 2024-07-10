/* 
 * ===========================================================================
 * File Name StaffServiceImpl.java
 * 
 * Created on Aug 21, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author rodolfopeixoto
* @version 1.2 - Aug 21, 2023
*/
package com.service.vix.service.impl;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ImageUtility;
import com.service.vix.dto.Message;
import com.service.vix.dto.StaffDTO;
import com.service.vix.dto.UserDTO;
import com.service.vix.enums.ImageUploadDirectory;
import com.service.vix.mapper.StaffMapper;
import com.service.vix.mapper.UserMapper;
import com.service.vix.models.Organization;
import com.service.vix.models.Role;
import com.service.vix.models.Staff;
import com.service.vix.models.User;
import com.service.vix.repositories.RoleRepository;
import com.service.vix.repositories.StaffRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.CommonService;
import com.service.vix.service.EmailService;
import com.service.vix.service.StaffService;
import com.service.vix.utility.SaveImage;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EmailService emailService;

	@Autowired
	private Environment env;
	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private SaveImage saveImage;
	@Autowired
	private PasswordEncoder encoder;
	@Value("${staff.password}")
	private String staffDefaultPassword;
	@Value("${admin.baseURL}")
	private String baseURL;
	@Autowired
	private StaffRepository staffRepository;
	@Value("${project.user.upload-dir}")
	private String userProfilePicUploadDirectory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.StaffService#saveStaff(com.service.vix.dto.StaffDTO)
	 */
	@Override
	public CommonResponse<StaffDTO> saveStaff(StaffDTO staffDTO, HttpSession httpSession) {
		log.info("Enter inside StaffServiceImpl.saveStaff().");
		String msg = "";
		CommonResponse<StaffDTO> response = new CommonResponse<StaffDTO>();
		staffDTO.getUserDTO().setUsername(staffDTO.getUserDTO().getEmail() + UUID.randomUUID());

		String dateOfBirthStr = staffDTO.getDateOfBirthStr();
		String releaseDateStr = staffDTO.getReleaseDateStr();
		String hireDateStr = staffDTO.getHireDateStr();
		String drivingLicienceExpiryStr = staffDTO.getDrivingLicienceExpiryStr();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate dateOfBirth = LocalDate.now();
		LocalDate releaseDate = LocalDate.now();
		LocalDate hireDate = LocalDate.now();
		LocalDate drivingLicienceExpiry = LocalDate.now();

		try {
			if (dateOfBirthStr != null && !dateOfBirthStr.equals(""))
				dateOfBirth = LocalDate.parse(dateOfBirthStr, dateTimeFormatter);
			if (releaseDateStr != null && !releaseDateStr.equals(""))
				releaseDate = LocalDate.parse(releaseDateStr, dateTimeFormatter);
			if (hireDateStr != null && !hireDateStr.equals(""))
				hireDate = LocalDate.parse(hireDateStr, dateTimeFormatter);
			if (drivingLicienceExpiryStr != null && !drivingLicienceExpiryStr.equals(""))
				drivingLicienceExpiry = LocalDate.parse(drivingLicienceExpiryStr, dateTimeFormatter);
		} catch (Exception e) {
			msg = env.getProperty("something.went.wrong");
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setMessage(msg);
			response.setData(staffDTO);
			response.setResult(false);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return response;
		}

		Boolean existsByName = this.userRepository.existsByUsername(staffDTO.getUserDTO().getUsername());
		Boolean existsByEmail = this.userRepository.existsByEmail(staffDTO.getUserDTO().getEmail());
		if (existsByEmail || existsByName) {
			log.info("Going to filterd out staff");
			if (existsByEmail)
				response.setMessage(env.getProperty("staff.email.already.exists"));
			if (existsByName)
				response.setMessage(env.getProperty("staff.name.already.exists"));
			log.info("staff havn't proper details for registeration  |  Error | " + response.getMessage());
			response.setData(null);
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		} else {
			log.info("Going to save staff");
			Staff staff = StaffMapper.INSTANCE.staffDtoTOStaff(staffDTO);
			staff.setReleaseDate(releaseDate);
			staff.setDateOfBirth(dateOfBirth);
			staff.setHireDate(hireDate);
			staff.setDrivingLicienceExpiry(drivingLicienceExpiry);

			UserDTO userDTO = staffDTO.getUserDTO();

			User givenUser = UserMapper.INSTANCE.userDtoToUser(userDTO);
			givenUser.setPassword(encoder.encode(staffDefaultPassword));

			if (staffDTO.getRoleId() != null) {
				log.info("Going to set Role to staff");
				Role role = this.roleRepository.findById(staffDTO.getRoleId()).get();
				givenUser.setRole(role);
			}

			if (userDTO.getUserProfileMultipart() != null) {
				ImageUtility imageUtility = new ImageUtility();
				imageUtility.setFile(userDTO.getUserProfileMultipart());
				imageUtility.setFileName(userDTO.getUserProfileMultipart().getOriginalFilename());
				String staffProfilePictureStr = staffDTO.getUserDTO().getEmail() + "-"
						+ LocalDateTime.now().toString().substring(0, 10) + ".png";
				imageUtility.setUniqueIdentifier(staffProfilePictureStr);
				imageUtility.setImageUploadDirectory(ImageUploadDirectory.USER);
				CommonResponse<Boolean> savedImageResponse = this.saveImage.saveImage(imageUtility);
				log.info(savedImageResponse.getMessage());

				if (savedImageResponse.getResult()) {
					staff.getUser().setProfileURL(staffProfilePictureStr);
				}
			}
			Long staffOrgId = staffDTO.getStaffOrgId();
			if (staffOrgId != null) {
				Organization organizationDetails = this.userRepository.findById(staffOrgId).get()
						.getOrganizationDetails();
				staff.setOrganization(organizationDetails);
			}

			givenUser.setStaff(staff);

			User savedUser = null;
			try {
				savedUser = this.userRepository.save(givenUser);
				sendInvitionToStaff(staffDTO);
				msg = env.getProperty("staff.save.success");
				httpSession.setAttribute("message", new Message(msg, "success"));
				staffDTO.setUserDTO(UserMapper.INSTANCE.userToUserDTO(savedUser));
				response.setMessage(msg);
				response.setData(staffDTO);
				response.setResult(false);
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return response;
			} catch (Exception e) {
				msg = env.getProperty("something.went.wrong") + " & " + env.getProperty("straff.save.failed");
				httpSession.setAttribute("message", new Message(msg, "danger"));
				response.setMessage(msg);
				response.setData(staffDTO);
				response.setResult(false);
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				return response;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.StaffService#getAllStaff(java.security.Principal)
	 */
	@Override
	public CommonResponse<List<StaffDTO>> getAllStaff(Principal principal) {
		log.info("Enter inside StaffServiceImpl.getAllStaff() method.");
		CommonResponse<List<StaffDTO>> response = new CommonResponse<>();
		List<StaffDTO> staffDTOs = new ArrayList<StaffDTO>();
		Organization organization = this.commonService.getLoggedInUserOrganization(principal);
		if (organization != null) {
			List<Staff> staff = this.staffRepository
					.findAllByIsDeletedFalseAndOrganizationOrderByCreatedAtDesc(organization);
			staff.forEach(c -> {
				StaffDTO staffDTO = StaffMapper.INSTANCE.staffToStaffDTO(c);
				staffDTO.setUserDTO(UserMapper.INSTANCE.userToUserDTO(c.getUser()));
				staffDTOs.add(staffDTO);
			});
		}
		String msg = env.getProperty("record.found.success");
		log.info(msg);
		response.setMessage(msg);
		response.setData(staffDTOs);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#removeStaff(java.lang.Long)
	 */
	@Override
	public CommonResponse<Boolean> removeStaff(Long userId, HttpSession httpSession) {
		log.info("Enter inside StaffServiceImpl.removeStaff() method.");
		CommonResponse<Boolean> response = new CommonResponse<>();
		String msg = "";
		if (userId != null && userId > 0) {
			Optional<User> userOpt = this.userRepository.findById(userId);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				Staff staff = user.getStaff();
				if (staff != null) {
					Long id = staff.getId();
					Optional<Staff> findById = this.staffRepository.findById(id);
					Staff st = findById.get();
					st.setIsDeleted(true);
					staffRepository.save(st);
				}
				msg = env.getProperty("user.delete.success");
				httpSession.setAttribute("message", new Message(msg, "success"));
				log.info(msg);
				response.setMessage(msg);
				response.setData(true);
				response.setResult(true);
				response.setStatus(HttpStatus.OK.value());
				return response;
			}
		} else {
			msg = env.getProperty("user.delete.failed");
			log.info(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setMessage(msg);
			response.setData(false);
			response.setResult(true);
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return response;
		}

		msg = env.getProperty("record.not.found");
		log.info(msg);
		response.setMessage(msg);
		response.setData(false);
		response.setResult(true);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#updateStaff(com.service.vix.dto.
	 * StaffDTO, jakarta.servlet.http.HttpSession)
	 */
	public CommonResponse<StaffDTO> updateStaff(StaffDTO staffDTO, HttpSession httpSession) {
		log.info("Enter inside staffServiceImpl.updateStaff() method.");
		String msg = "";
		CommonResponse<StaffDTO> response = new CommonResponse<StaffDTO>();

		User DBuser = this.userRepository.findById(staffDTO.getUserDTO().getId()).get();
		Staff DBstaff = this.staffRepository.findById(staffDTO.getId()).get();

		String dateOfBirthStr = staffDTO.getDateOfBirthStr();
		String releaseDateStr = staffDTO.getReleaseDateStr();
		String hireDateStr = staffDTO.getHireDateStr();
		String drivingLicienceExpiryStr = staffDTO.getDrivingLicienceExpiryStr();

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate dateOfBirth = LocalDate.now();
		LocalDate releaseDate = LocalDate.now();
		LocalDate hireDate = LocalDate.now();
		LocalDate drivingLicienceExpiry = LocalDate.now();

		try {
			if (dateOfBirthStr != null && !dateOfBirthStr.equals(""))
				dateOfBirth = LocalDate.parse(dateOfBirthStr, dateTimeFormatter);
			if (releaseDateStr != null && !releaseDateStr.equals(""))
				releaseDate = LocalDate.parse(releaseDateStr, dateTimeFormatter);
			if (hireDateStr != null && !hireDateStr.equals(""))
				hireDate = LocalDate.parse(hireDateStr, dateTimeFormatter);
			if (drivingLicienceExpiryStr != null && !drivingLicienceExpiryStr.equals(""))
				drivingLicienceExpiry = LocalDate.parse(drivingLicienceExpiryStr, dateTimeFormatter);
		} catch (Exception e) {
			msg = env.getProperty("something.went.wrong");
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setMessage(msg);
			response.setData(staffDTO);
			response.setResult(false);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return response;
		}
		log.info("Going to save staff");
		Staff staff = StaffMapper.INSTANCE.staffDtoTOStaff(staffDTO);

		UserDTO userDTO = staffDTO.getUserDTO();
		User givenUser = UserMapper.INSTANCE.userDtoToUser(userDTO);

		if (staffDTO.getUserDTO().getUserProfileMultipart() != null) {
			ImageUtility imageUtility = new ImageUtility();
			imageUtility.setFile(staffDTO.getUserDTO().getUserProfileMultipart());
			imageUtility.setFileName(staffDTO.getUserDTO().getUserProfileMultipart().getOriginalFilename());
			String staffProfilePictureStr = DBstaff.getUser().getEmail() + "-"
					+ LocalDateTime.now().toString().substring(0, 10) + ".png";
			imageUtility.setUniqueIdentifier(staffProfilePictureStr);
			imageUtility.setImageUploadDirectory(ImageUploadDirectory.USER);
			CommonResponse<Boolean> savedImageResponse = this.saveImage.saveImage(imageUtility);
			log.info(savedImageResponse.getMessage());

			if (savedImageResponse.getResult()) {
				givenUser.setProfileURL(staffProfilePictureStr);
			}
		} else {
			staff.getUser().setProfileURL(DBuser.getProfileURL());
		}

		staff.setReleaseDate(releaseDate);
		staff.setDateOfBirth(dateOfBirth);
		staff.setHireDate(hireDate);
		staff.setDrivingLicienceExpiry(drivingLicienceExpiry);
		staff.setOrganization(DBstaff.getOrganization());

		if (staffDTO.getRoleId() != null) {
			log.info("Going to set Role to staff");
			Role role = this.roleRepository.findById(staffDTO.getRoleId()).get();
			givenUser.setRole(role);
		}

		givenUser.setPassword(DBuser.getPassword());
		givenUser.setUsername(DBuser.getUsername());
		givenUser.setStaff(staff);

		User savedUser = null;
		try {
			savedUser = this.userRepository.save(givenUser);
			msg = env.getProperty("staff.update.success");
			httpSession.setAttribute("message", new Message(msg, "success"));
			staffDTO.setUserDTO(UserMapper.INSTANCE.userToUserDTO(savedUser));
			response.setMessage(msg);
			response.setData(staffDTO);
			response.setResult(false);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return response;
		} catch (Exception e) {
			msg = env.getProperty("something.went.wrong") + " & " + env.getProperty("straff.update.failed");
			httpSession.setAttribute("message", new Message(msg, "danger"));
			response.setMessage(msg);
			response.setData(staffDTO);
			response.setResult(false);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return response;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#getStaffById(java.lang.Long)
	 */
	@Override
	public CommonResponse<StaffDTO> getStaffById(Long userId) {
		log.info("Enter inside staffServiceImpl.getStaffById() method.");
		CommonResponse<StaffDTO> response = new CommonResponse<>();
		if (userId != null && userId > 0) {
			Optional<User> userOpt = this.userRepository.findById(userId);
			User user = userOpt.get();
			UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
			if (userDTO.getUserProfilePictureURL() != null) {
				String[] userProfileUploadDirArr = this.userProfilePicUploadDirectory.split("/");
				int userProfileUploadDirArrLength = userProfileUploadDirArr.length;
				userDTO.setUserProfilePictureURL(userProfileUploadDirArr[userProfileUploadDirArrLength - 1] + "/"
						+ userDTO.getUserProfilePictureURL());
			}
			Staff staff = user.getStaff();
			StaffDTO staffDTO = StaffMapper.INSTANCE.staffToStaffDTO(staff);
			staffDTO.setUserDTO(userDTO);

			LocalDate drivingLicienceExpiry = staff.getDrivingLicienceExpiry();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String dateAsString = drivingLicienceExpiry.format(dateTimeFormatter);
			staffDTO.setDrivingLicienceExpiryStr(dateAsString);

			LocalDate releaseDate = staff.getReleaseDate();
			String releaseDateSt = releaseDate.format(dateTimeFormatter);
			staffDTO.setReleaseDateStr(releaseDateSt);

			LocalDate hireDate = staff.getHireDate();
			String hireDateSt = hireDate.format(dateTimeFormatter);
			staffDTO.setHireDateStr(hireDateSt);

			LocalDate dateOfBirth = staff.getDateOfBirth();
			String dateOfBirthSt = dateOfBirth.format(dateTimeFormatter);
			staffDTO.setDateOfBirthStr(dateOfBirthSt);

			userDTO.setStaffDTO(staffDTO);
			String msg = env.getProperty("record.found.success");
			log.info(msg);
			response.setMessage(msg);
			response.setData(staffDTO);
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
	 * @see
	 * com.service.vix.service.StaffService#sendInvitionToStaff(com.service.vix.dto.
	 * StaffDTO)
	 */
	@Override
	public void sendInvitionToStaff(StaffDTO staffDTO) {
		log.info("Enter inside StaffServiceImpl.sendInvitionToStaff() method");
		try {
			// Prepare data for the PDF template
			Map<String, Object> data = new HashMap<>();
			data.put("invitationLink", baseURL + "/staff/change-password");
			data.put("staffEmailId", staffDTO.getUserDTO().getEmail());
			byte[] pdfBytes = null;
			String body = "";
			try {
				Context context = new Context();
				if (data != null) {
					for (Map.Entry<String, Object> pair : data.entrySet())
						context.setVariable(pair.getKey(), pair.getValue());
				}
				body = this.templateEngine.process("staffInvitation", context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.emailService.sendEmailWithAttachment(staffDTO.getUserDTO().getEmail(), "Change Password", body,
					pdfBytes, "", "");
		} catch (Exception e) {
			// Handle exceptions and show an error page or message
			log.error("Exception occure inside StaffServiceImpl.sendInvitionToStaff() method");
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#createPassword(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String createPassword(String staffEmail, String newPassword) {
		if (staffEmail != null && newPassword != null) {
			User user = this.userRepository.findByEmail(staffEmail).get();
			user.setPassword(encoder.encode(newPassword));
			Staff staff = user.getStaff();
			staff.setIsPasswordChange(true);
			user.setStaff(staff);
			this.userRepository.save(user);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#getStaffByEmail(java.lang.String)
	 */
	@Override
	public CommonResponse<StaffDTO> getStaffByEmail(String staffEmailId) {
		CommonResponse<StaffDTO> response = new CommonResponse<>();
		if (staffEmailId != null) {
			Long staffId = this.userRepository.findByEmail(staffEmailId).get().getId();
			return this.getStaffById(staffId);
		}
		String msg = env.getProperty("record.not.found");
		log.info(msg);
		response.setMessage(msg);
		response.setData(null);
		response.setResult(true);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.StaffService#getUserByEmail(java.lang.String)
	 */
	@Override
	public Map<Boolean, String> getUserByEmail(String email) {

		HashMap<Boolean, String> result = new HashMap<>();

		Boolean existsByEmail = this.userRepository.existsByEmail(email);

		if (existsByEmail)
			result.put(true, "Email already exists");
		else
			result.put(false, "Email available");
		return result;
	}

}
