package com.service.vix.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.LoginRequest;
import com.service.vix.dto.Message;
import com.service.vix.dto.SignupRequest;
import com.service.vix.enums.ERole;
import com.service.vix.models.Role;
import com.service.vix.models.User;
import com.service.vix.repositories.RoleRepository;
import com.service.vix.repositories.UserRepository;
import com.service.vix.service.EmailService;
import com.service.vix.service.UserService;
import com.service.vix.utility.CookieHandler;
import com.service.vix.utility.JwtUtils;
import com.service.vix.utility.TokenRevocationList;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as implementation class for UserService class that
 * implement/define all the methods of application UserService
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private Environment env;
	@Autowired
	private TokenRevocationList tokenRevocationList;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private CookieHandler cookieHandler;

	@Value("${role.organization}")
	private String organizationRoleStr;
	@Value("${super.admin.count}")
	private Byte superAdminCount;
	@Value("${project.organization.upload-dir}")
	private String organizationProfilePicUploadDirectory;
	@Value("${project.user.upload-dir}")
	private String userProfilePicUploadDirectory;
	@Value("${forgot-password-message.prefix}")
	private String forgotPasswordPrefixMsg;
	@Value("${forgot-password-message.suffix}")
	private String forgotPasswordSuffixMsg;

	@Autowired
	private EmailService emailService;

	/*
	 * (non-Javadoc)This method is used to register user
	 * 
	 * @see com.service.vix.service.UserService#registerUser(com.service.vix.dto.
	 * SignupRequest)
	 */
	@Override
	public CommonResponse<User> registerUser(SignupRequest signupRequest) {
		log.info("Entered inside UserServiceImpl.registerUser() method.");

		CommonResponse<User> response = new CommonResponse<User>();

		log.info("Going to check user details is correct or not");
		Boolean userExistsByUsername = this.userRepository.existsByUsername(signupRequest.getUsername());
		log.info("User Exists By Username | " + userExistsByUsername);
		Boolean userExistsByEmail = userRepository.existsByEmail(signupRequest.getEmail());
		log.info("User Exists By Email | " + userExistsByEmail);
		boolean userExistsBySuperAdminRole = this.existsBySuperAdminRole();
		log.info("Any User have already Super Admin role | " + userExistsBySuperAdminRole);

		// Check that given organization is register by given name or email
		if (userExistsByUsername || userExistsByEmail || userExistsBySuperAdminRole) {
			log.info("Going to filterd out user");

			if (userExistsByUsername)
				response.setMessage(env.getProperty("user.name.already.exists"));
			else if (userExistsByEmail)
				response.setMessage(env.getProperty("user.email.already.exists"));
			else if (userExistsBySuperAdminRole)
				response.setMessage(env.getProperty("user.super.admin.already.exists"));

			log.info("User havn't proper details for registeration  |  Error | " + response.getMessage());

			response.setData(null);
			response.setResult(false);
			response.setStatus(HttpStatus.ALREADY_REPORTED.value());
			return response;
		}

		log.info("User have proper details for registration.");

		// Create new user's account
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));

		String strRoles = signupRequest.getRole();
		Role role = new Role();

		if (strRoles == null) {
			log.info("User havn't entered any role");
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
			role = userRole;
		} else {
			log.info("User have enter role " + strRoles);

			switch (strRoles) {
			case "super admin":
				Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
						.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
				role = superAdminRole;
				break;
			case "admin":
				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
				role = adminRole;
				break;
			case "mod":
				Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
						.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
				role = modRole;
				break;
			case "organization":
				Role orgRole = roleRepository.findByName(ERole.ROLE_ORGNAIZATION)
						.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
				role = orgRole;
				break;
			default:
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException(env.getProperty("role.not.found")));
				role = userRole;
			}
		}
		user.setRole(role);

		log.info("Going to save user");
		User savedUser = userRepository.save(user);
		log.info("User saved successfully | USER :: " + savedUser);

		response.setMessage(env.getProperty("user.register.success"));
		response.setData(null);
		response.setResult(true);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/*
	 * (non-Javadoc)This method is used to login user
	 * 
	 * @see com.service.vix.service.UserService#loginUser(com.service.vix.dto.
	 * LoginRequest)
	 */
	@Override
	public CommonResponse<String> loginUser(LoginRequest loginRequest, HttpSession httpSession,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
		log.info("Enter inside UserServiceImpl.loginUser() method");
		CommonResponse<String> response = new CommonResponse<String>();
		// Get previous token
		String tokenFromCookie = this.cookieHandler.getTokenFromCookie(httpServletRequest);
		tokenRevocationList.revokeToken(tokenFromCookie);
		this.cookieHandler.removeTokenFromCookie(httpServletResponse);
		log.info("Last JWT token has been removed from session");
		// Get current session and remove previous token from session and error messages
		httpSession.removeAttribute("token");
		httpSession.removeAttribute("errorMessage");
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			httpSession.setAttribute("isUserInvalid", false);
			log.info("User is an authneticated user");
		} catch (Exception e) {
			String errorMsg = "";
			errorMsg = env.getProperty("user.invalid.username.password");
			httpSession.setAttribute("message", new Message(errorMsg, "danger"));
			httpSession.setAttribute("isUserInvalid", true);
			httpSession.setAttribute("emailUsername", loginRequest.getUsername());
			httpSession.setAttribute("password", loginRequest.getPassword());

			response.setMessage(errorMsg);
			response.setData("redirect:/login");
			response.setResult(false);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			log.error("User is an un-authneticated user. " + errorMsg);
			return response;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info("Going to generate JWT token for user.");
		String jwt = jwtUtils.generateJwtToken(authentication);
		log.info("JWT token generated successfully for user.  JWT | " + jwt);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		// Get current session and set token
		this.cookieHandler.addTokenToCookie(jwt, httpServletResponse);
		log.info("User have " + roles + " role(s).");
		log.info("Going to rendered user on " + response.getData() + " dashboard/URL");
		// Automatic render on dashboard on-behalf of role
		String renderedURL = getUrlBasedOnUserRole(roles);
		response.setData(renderedURL);
		response.setMessage(env.getProperty("user.login.success"));
		response.setResult(false);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	/**
	 * This method is used to check that any user exists by Super Admin role or not
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return Boolean
	 * @return
	 * @exception Description
	 */
	private Boolean existsBySuperAdminRole() {
		log.info("Entered inside UserServiceImpl.existsBySuperAdminRole() method");
		int dbSuperAdminCount = this.userRepository.getCountByRole("ROLE_SUPER_ADMIN");
		if (dbSuperAdminCount >= superAdminCount)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.UserService#getUrlBasedOnUserRole(java.lang.String)
	 */
	@Override
	public String getUrlBasedOnUserRole(List<String> roles) {
		log.info("Enter inside UserServiceImpl.getUrlBasedOnUserRole() method");
		if (roles.stream().anyMatch(roleName -> roleName.equals(ERole.ROLE_SUPER_ADMIN.toString()))) {
			log.info("User have SUPER_ADMIN role");
			return "redirect:/dashboard/super";
		} else if (roles.stream().anyMatch(roleName -> roleName.equals(ERole.ROLE_MODERATOR.toString()))) {
			log.info("User have MODERATE_ROLE");
			return "redirect:/dashboard/mod";
		} else if (roles.stream().anyMatch(roleName -> roleName.equals(ERole.ROLE_ADMIN.toString()))) {
			log.info("User have ADMIN_ROLE");
			return "redirect:/dashboard/admin";
		} else if (roles.stream().anyMatch(roleName -> roleName.equals(ERole.ROLE_ORGNAIZATION.toString()))
				|| roles.contains(organizationRoleStr)) {
			log.info("User have ORGANIZATION_ROLE ");
			return "redirect:/dashboard/organization";
		} else {
			log.info("User have USER_ROLE ");
			return "redirect:/dashboard/organization";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.service.vix.service.UserService#renderUserIfAlreadyLoggedIn(java.security
	 * .Principal)
	 */
	@Override
	public String renderUserIfAlreadyLoggedIn(Principal principal, HttpSession httpSession) {
		log.info("Enter inside UserServiceImpl.renderUserIfAlreadyLoggedIn() method");
		if (principal != null) {
			log.info("User already logged in.");
			Authentication authentication = (Authentication) principal;
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			List<String> roles = new ArrayList<String>();
			authorities.forEach(auth -> roles.add(auth.getAuthority()));
			log.info("User have " + roles + " role.");
			httpSession.setAttribute("message", new Message(env.getProperty("user.already.logged.in"), "danger"));
			return this.getUrlBasedOnUserRole(roles);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.UserService#getUserByUserName(java.lang.String)
	 */
	@Override
	public Map<Boolean, String> getUserByUserName(String username) {
		log.info("Enter inside UserServiceImpl.getUserByUserName() method");
		HashMap<Boolean, String> result = new HashMap<>();
		this.userRepository.existsByUsername(username);
		Boolean existsByUsername = userRepository.existsByUsername(username);
		if (existsByUsername)
			result.put(true, "username already exists");
		else
			result.put(false, "username available");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.service.vix.service.UserService#forgotPassword(jakarta.servlet.http.
	 * HttpServletRequest, org.springframework.ui.Model)
	 */
	@Override
	public CommonResponse<Boolean> forgotPassword(HttpServletRequest httpServletRequest, HttpSession httpSession)
			throws MessagingException {
		log.info("Enter inside UserServiceImpl.forgotPassword() method.");
		CommonResponse<Boolean> result = new CommonResponse<>();
		String userEmail = httpServletRequest.getParameter("userEmail");
		String msg = "";
		if (userEmail != null) {
			Optional<User> optUser = this.userRepository.findByEmail(userEmail);
			if (optUser.isPresent()) {
				String message = null;
				String generatedPassword = this.generatePassword(8).toString();
				message = forgotPasswordPrefixMsg + generatedPassword + forgotPasswordSuffixMsg;
				boolean emailResponse = this.emailService.sendEmailWithoutAttachment(userEmail, "Forgot Passoword",
						message);
				if (emailResponse) {
					User user = optUser.get();
					user.setPassword(this.encoder.encode(generatedPassword));
					try {
						this.userRepository.save(user);
					} catch (Exception e) {
						msg = env.getProperty("something.went.wrong");
						log.error("Exception occuring inside UserServiceImpl.forgotPassword() method. Exception is : "
								+ e.getMessage());
						result.setData(false);
						result.setResult(true);
						result.setStatus(HttpStatus.NOT_FOUND.value());
						result.setMessage(msg);
						httpSession.setAttribute("message", new Message(msg, "danger"));
						return result;
					}
				}
				msg = env.getProperty("password.generate.success");
				result.setData(true);
				result.setResult(true);
				result.setStatus(HttpStatus.OK.value());
				result.setMessage(msg);
				httpSession.setAttribute("message", new Message(msg, "success"));
				return result;
			} else {
				msg = env.getProperty("password.generate.fail");
				result.setData(true);
				result.setResult(true);
				result.setStatus(HttpStatus.OK.value());
				result.setMessage(msg);
				httpSession.setAttribute("message", new Message(msg, "danger"));
				return result;
			}
		} else {
			msg = env.getProperty("something.went.wrong");
			result.setData(false);
			result.setResult(true);
			result.setStatus(HttpStatus.NOT_FOUND.value());
			result.setMessage(msg);
			httpSession.setAttribute("message", new Message(msg, "danger"));
			return result;
		}
	}

	/**
	 * This method is used to generate password for user
	 * 
	 * @author ritiks
	 * @date Nov 1, 2023
	 * @return char[]
	 * @param length
	 * @return
	 * @exception Description
	 */
	private char[] generatePassword(int length) {
		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "!@#$";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = new Random();
		char[] password = new char[length];

		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password[3] = numbers.charAt(random.nextInt(numbers.length()));

		for (int i = 4; i < length; i++) {
			password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return password;
	}

}
