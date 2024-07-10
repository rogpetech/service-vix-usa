package com.service.vix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.vix.models.User;

/**
 * This class is used as user repository that handle all the methods of user
 * entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * This method is used to find user by username
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 1, 2023
	 * @return Optional<User>
	 * @param username
	 * @return
	 * @exception Description
	 */
	Optional<User> findByUsername(String username);

	/**
	 * This method is used to check user exists or not with given username
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 1, 2023
	 * @return Boolean
	 * @param username
	 * @return
	 * @exception Description
	 */
	Boolean existsByUsername(String username);

	/**
	 * This method is used to check user exists by email
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 1, 2023
	 * @return Boolean
	 * @param email
	 * @return
	 * @exception Description
	 */
	Boolean existsByEmail(String email);

	/**
	 * This method is used to check any user have super admin role
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 1, 2023
	 * @return List<User>
	 * @param role
	 * @return
	 * @exception Description
	 */
	@Query(value = "SELECT COUNT(u.id) FROM users u JOIN roles r ON u.role_id = r.id WHERE r.name = ?1", nativeQuery = true)
	int getCountByRole(String roleName);

	/**
	 * This method is used to get user by email
	 * 
	 * @author rodolfopeixoto
	 * @date Jun 2, 2023
	 * @return Optional<User>
	 * @param email
	 * @return
	 * @exception Description
	 */
	Optional<User> findByEmail(String email);

	/**
	 * This method is used to get associated user with given role
	 * 
	 * @author rodolfopeixoto
	 * @date Aug 19, 2023
	 * @param roleId
	 * @return
	 */
	Optional<User> findByRoleId(Long roleId);
	
	
	
	/**
	 * This method is used to find isDeletedFalse User
	 * @author hemantr
	 * @date Aug 23, 2023
	 * @return List<User> 
	 * @return
	 * @exception 
	 * Description
	 */
	List<User> findAllByIsDeletedFalse();
	

	/**This method is used to find the technician name for job
	 * @author hemantr
	 * @date Nov 28, 2023
	 * @return List<String> 
	 * @param userIds
	 * @return
	 * @exception 
	 * Description
	 */
	@Query("SELECT u.firstName FROM User u WHERE u.id IN :userIds")
	List<String>findUserNamesByIdIn(@Param("userIds")List<Long>userIds);

}
