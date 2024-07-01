package com.service.vix.mapper;

import com.service.vix.dto.RoleDTO;
import com.service.vix.models.Permission;
import com.service.vix.models.Role;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-30T21:48:48-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDTO roleToRoleDTO(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setId( role.getId() );
        roleDTO.setCreatedAt( role.getCreatedAt() );
        roleDTO.setUpdatedAt( role.getUpdatedAt() );
        roleDTO.setCreatedBy( role.getCreatedBy() );
        roleDTO.setUpdatedBy( role.getUpdatedBy() );
        roleDTO.setName( role.getName() );
        List<Permission> list = role.getPermissions();
        if ( list != null ) {
            roleDTO.setPermissions( new ArrayList<Permission>( list ) );
        }
        roleDTO.setIsDeleted( role.getIsDeleted() );

        return roleDTO;
    }

    @Override
    public Role roleDTOToRole(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( roleDTO.getId() );
        role.setCreatedAt( roleDTO.getCreatedAt() );
        role.setUpdatedAt( roleDTO.getUpdatedAt() );
        role.setCreatedBy( roleDTO.getCreatedBy() );
        role.setUpdatedBy( roleDTO.getUpdatedBy() );
        role.setName( roleDTO.getName() );
        role.setIsDeleted( roleDTO.getIsDeleted() );
        List<Permission> list = roleDTO.getPermissions();
        if ( list != null ) {
            role.setPermissions( new ArrayList<Permission>( list ) );
        }

        return role;
    }
}
