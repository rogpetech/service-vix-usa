/* 
 * ===========================================================================
 * File Name PermissionRepository.java
 * 
 * Created on Aug 19, 2023
 * ===========================================================================
 */
/**
* Class Information
* @author ritiks
* @version 1.2 - Aug 19, 2023
*/
package com.service.vix.repositories;

import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import com.service.vix.models.Permission;
import com.service.vix.models.Role;

/**
 * This class is used as Repository for Permission
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	/**
	 * Get Permissions by Role
	 * 
	 * @author ritiks
	 * @date Aug 21, 2023
	 * @return List<Permission>
	 * @param role
	 * @return
	 * @exception Description
	 */
	List<Permission> findByRole(Role role);

}
