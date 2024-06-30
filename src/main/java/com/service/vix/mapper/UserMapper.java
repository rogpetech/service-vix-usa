package com.service.vix.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Description;

import com.service.vix.dto.UserDTO;
import com.service.vix.models.User;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	/**
	 * This method is used to map User DTO to User Entity
	 * 
	 * @author hemantr
	 * @date Jun 6, 2023
	 * @return User
	 * @param userDTO
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organizationDetails", ignore = true)
	@Mapping(target = "profileURL",source = "userProfilePictureURL")
	User userDtoToUser(UserDTO userDTO);

	/**
	 * This method is used to map User Entity to User DTO
	 * 
	 * @author hemantr
	 * @date Jun 6, 2023
	 * @return UserDTO
	 * @param user
	 * @return
	 * @exception Description
	 */
	@Mapping(target = "organizationDTO", ignore = true)
	@Mapping(target = "userProfilePictureURL",source = "profileURL")
	UserDTO userToUserDTO(User user);
}
