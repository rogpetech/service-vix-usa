/* 
 * ===========================================================================
 * File Name ResourceConfig.java
 * 
 * Created on Sep 1, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author @rodolfopeixoto
* @version 1.2 - Sep 1, 2023
*/
package com.service.vix.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

	@Value("${resource.image.path}")
	private String path;

	/**
	 * @author @rodolfopeixoto
	 * @date April 8, 2021 Initializes resource handler method for project image
	 *       from from the path mention.
	 */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/project_image/**").addResourceLocations(path);
	}
}
