package com.service.vix.mapper;

import com.service.vix.dto.UserDTO;
import com.service.vix.models.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:49-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User userDtoToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setProfileURL( userDTO.getUserProfilePictureURL() );
        user.setId( userDTO.getId() );
        user.setCreatedAt( userDTO.getCreatedAt() );
        user.setUpdatedAt( userDTO.getUpdatedAt() );
        user.setCreatedBy( userDTO.getCreatedBy() );
        user.setUpdatedBy( userDTO.getUpdatedBy() );
        user.setUsername( userDTO.getUsername() );
        user.setEmail( userDTO.getEmail() );
        user.setPassword( userDTO.getPassword() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setMobileNum( userDTO.getMobileNum() );
        user.setGender( userDTO.getGender() );
        user.setIsActive( userDTO.getIsActive() );
        user.setRole( userDTO.getRole() );

        return user;
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setUserProfilePictureURL( user.getProfileURL() );
        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setMobileNum( user.getMobileNum() );
        userDTO.setGender( user.getGender() );
        userDTO.setRole( user.getRole() );
        userDTO.setIsActive( user.getIsActive() );
        userDTO.setCreatedAt( user.getCreatedAt() );
        userDTO.setUpdatedAt( user.getUpdatedAt() );
        userDTO.setCreatedBy( user.getCreatedBy() );
        userDTO.setUpdatedBy( user.getUpdatedBy() );

        return userDTO;
    }
}
