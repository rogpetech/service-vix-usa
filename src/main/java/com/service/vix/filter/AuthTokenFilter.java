package com.service.vix.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.vix.service.impl.UserDetailsServiceImpl;
import com.service.vix.utility.CookieHandler;
import com.service.vix.utility.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is used as a filter for security that check user is authenticated
 * user or not and also pointer pass from this class always when hit the request
 */
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private CookieHandler cookieHandler;

	/*
	 * (non-Javadoc) This method call always when user hit request and also validate
	 * user and JWT token
	 * 
	 * @see
	 * org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(jakarta.
	 * servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse,
	 * jakarta.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("Enter inside AuthTokenFilter.doFilterInternal() method");
		try {
//         HttpSession currentSession = request.getSession();
			log.info("Going to get token from cookie");
			// String sessionToken = (String) currentSession.getAttribute("token");
			log.info("Going to parse JWT");
			String jwt = parseJwt(request);
			String tokenFromCookie = this.cookieHandler.getTokenFromCookie(request);

			if (jwt == null && tokenFromCookie != null) {
				jwt = tokenFromCookie;
			}

			log.info("Going to validated JWT");
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				log.info("JWT validate successfully");
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("| EXCEPTION | Cannot set user authentication : Exception : " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * This method is used to parse JWT from upcoming request
	 * 
	 * @author rodolfopeixoto
	 * @date May 31, 2023
	 * @return String
	 * @param request
	 * @return
	 * @exception Description
	 */
	private String parseJwt(HttpServletRequest request) {
		log.info("Enter inside AuthTokenFilter.parseJwt() method");
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			log.info("Token coming from authorization");
			return headerAuth.substring(7);
		}

		return null;
	}
}
