package com.service.vix.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.service.vix.dto.CommonResponse;
import com.service.vix.dto.ImageUtility;
import com.service.vix.enums.ImageUploadDirectory;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is used to save File in server directory
 * 
 * @author rodolfopeixoto
 *
 */
@Component
@Slf4j
public class SaveImage {

	@Autowired
	private Environment env;

	@Value("${project.product.upload-dir}")
	private String productImageSaveDirectory;

	@Value("${project.service.upload-dir}")
	private String serviceImageSaveDirectory;

	@Value("${project.organization.upload-dir}")
	private String organizationImageSaveDirectory;

	@Value("${project.user.upload-dir}")
	private String userImageSaveDirectory;

	@Value("${project.temp.upload-dir}")
	private String tempImageSaveDirectory;

	/**
	 * Add method description here
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 7, 2023
	 * @param imageUtility
	 * @return
	 */
	public CommonResponse<Boolean> saveImage(ImageUtility imageUtility) {
		log.info("Enter inside SaveImage.saveImage() Method.");
		CommonResponse<Boolean> result = new CommonResponse<>();
		if (imageUtility.getFile() != null && !imageUtility.getFile().isEmpty()) {

			File imageDirectory = null;

			if (imageUtility.getImageUploadDirectory().equals(ImageUploadDirectory.ORGANIZATION)) {
				// For Organization
				imageDirectory = new File(organizationImageSaveDirectory);
			} else if (imageUtility.getImageUploadDirectory().equals(ImageUploadDirectory.USER)) {
				// For User
				imageDirectory = new File(userImageSaveDirectory);
			} else if (imageUtility.getImageUploadDirectory().equals(ImageUploadDirectory.PRODUCT)) {
				// For products
				imageDirectory = new File(productImageSaveDirectory);
			} else if (imageUtility.getImageUploadDirectory().equals(ImageUploadDirectory.SERVICE)) {
				// For Service
				imageDirectory = new File(serviceImageSaveDirectory);
			} else {
				// For temporary directory
				imageDirectory = new File(tempImageSaveDirectory);
			}

			if (!imageDirectory.exists()) {
				if (imageDirectory.mkdirs()) {
					System.out.println("Created directory: " + imageDirectory.getAbsolutePath());
				} else {
					result.setData(false);
					result.setMessage(env.getProperty("Failed.to.create.the.directory"));
					result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					result.setResult(false);
					return result;
				}
			}

			try {
				String filename = imageUtility.getUniqueIdentifier();
				String filePath = imageDirectory.getAbsolutePath() + File.separator + filename;
				Path destination = Paths.get(filePath);
				Files.createDirectories(destination.getParent());
				imageUtility.getFile().transferTo(destination.toFile());

				result.setData(true);
				result.setMessage(env.getProperty("image.save.success"));
				result.setStatus(HttpStatus.OK.value());
				result.setResult(true);
			} catch (IllegalStateException | IOException e) {
				result.setData(false);
				result.setMessage(env.getProperty("image.save.failed"));
				result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
				result.setResult(false);
			}
		} else {
			result.setData(false);
			result.setMessage(env.getProperty("image.not.found"));
			result.setStatus(HttpStatus.BAD_REQUEST.value());
			result.setResult(false);
		}
		return result;
	}

}
