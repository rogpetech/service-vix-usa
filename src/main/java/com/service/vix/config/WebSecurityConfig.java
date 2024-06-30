package com.service.vix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.service.vix.filter.AuthEntryPointJwt;
import com.service.vix.filter.AuthTokenFilter;
import com.service.vix.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private CustomLogoutSuccessHandler logoutSuccessHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	/**
	 * This method is used to set that we have used DB for authentication
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return DaoAuthenticationProvider
	 * @return
	 * @exception Description
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	/**
	 * This method is used to create bean for AuthenticationManager
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return AuthenticationManager
	 * @param authConfig
	 * @return
	 * @throws Exception
	 * @exception Description
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * This method is used to create bean for passwordEncoder and also set that we
	 * have used BCryptPasswordEncoder
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return PasswordEncoder
	 * @return
	 * @exception Description
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * This method handle all the filters that we have used for implement security
	 * like set authenticated URLs unauthenticated URLs and also decide that we have
	 * used form base login and custom login logout functionality. That decides also
	 * that we have use UsernamePasswordAuthenticationFilter
	 * 
	 * @author ritiks
	 * @date May 31, 2023
	 * @return SecurityFilterChain
	 * @param http
	 * @return
	 * @throws Exception
	 * @exception Description
	 */
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/auth/**", "/uauth/**",
								"D:/Service_Vix_Docs/project_image/organization/**")
						.permitAll()
						.requestMatchers("/api/test/**", "/organization/add-organization",
								"/organization/process-add-organization", "/organization/searchByName/**",
								"/organization/orgNameExists/**", "/staff/change-password/**",
								"/forgot-password/**")
						.permitAll().requestMatchers("/css/**", "/js/**", "/assest/**", "/fonts/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").failureUrl("/login?error=true").permitAll()).logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
				.logoutSuccessHandler(logoutSuccessHandler).invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.deleteCookies("jwtToken");

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
