package com.service.vix.models;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Description;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is used as parent class for all entity classes
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@MappedSuperclass
public class AbstractModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	/**
	 * This method is used to set createdAt by default
	 * 
	 * @author ritiks
	 * @date Jun 2, 2023
	 * @return void
	 * @exception Description
	 */
	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.setCreatedAt(now);
		this.setUpdatedAt(now);
		this.setCreatedBy(getCurrentLoggedInUser());
		this.setUpdatedBy(getCurrentLoggedInUser());
	}

	/**
	 * This method is used to set updatedAt by default
	 * 
	 * @author ritiks
	 * @date Jun 2, 2023
	 * @return void
	 * @exception Description
	 */
	@PreUpdate
	public void preUpdate() {
		this.setUpdatedAt(LocalDateTime.now());
		this.setUpdatedBy(getCurrentLoggedInUser());
	}

	/**
	 * This method is used to get Current user
	 * 
	 * @author ritiks
	 * @date Aug 4, 2023
	 * @return
	 */
	private String getCurrentLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return authentication.getName();
		}
		return null;
	}

}
