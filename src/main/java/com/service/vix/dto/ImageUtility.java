package com.service.vix.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.service.vix.enums.ImageUploadDirectory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as Image Utility
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class ImageUtility {

	private String fileName;
	private String uniqueIdentifier;
	private MultipartFile file;
	private ImageUploadDirectory imageUploadDirectory;

}
