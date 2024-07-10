package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.vix.enums.ERole;
import com.service.vix.models.Role;

/**
 * This class is used as role repository that handle all the methods of role
 * entity
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	/**
	 * This method is used to get Role by role name(Enum)
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 18, 2023
	 * @param name
	 * @return
	 */
	Optional<Role> findByName(ERole name);

	/**
	 * This method is used to get Role by role name(String)
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 18, 2023
	 * @param name
	 * @return
	 */
	Optional<Role> findByName(String name);

	/**
	 * This method is used to check already exist role
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 18, 2023
	 * @param name
	 * @return
	 */
	Boolean existsByName(String name);

	/**
	 * Add method description here
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @return
	 */
	List<Role> findByIsDeletedFalse();

}
