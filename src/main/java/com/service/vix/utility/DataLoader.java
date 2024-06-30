package com.service.vix.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.service.vix.enums.Permissions;
import com.service.vix.models.Permission;
import com.service.vix.models.Role;
import com.service.vix.repositories.PermissionRepository;
import com.service.vix.repositories.RoleRepository;

/**
 * This class is used to save roles in role table.That class will be triggered
 * at every time when application is run but add data when, then there is no
 * data in roles table.
 */
@Component
public class DataLoader implements CommandLineRunner {

	@Value("${role.organization}")
	private String organizationRole;

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	public DataLoader(RoleRepository roleRepository, PermissionRepository permissionRepository) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
	}

	@Override
	public void run(String... args) throws Exception {

		// Check if the permission table is empty or has no all permissions
		if (permissionRepository.count() < Permissions.values().length) {
			// Delete All permissions
			this.permissionRepository.deleteAll();
			Role orgRole = new Role();
			orgRole.setId(1l);
			orgRole.setName(organizationRole);
			// Add Permissions
			List<Permission> permissions = new ArrayList<Permission>();
			Arrays.stream(Permissions.values()).forEach(
					ePermission -> permissions.add(new Permission(ePermission, true, true, true, true, true, orgRole)));

			List<Role> roles = new ArrayList<>();
			orgRole.setPermissions(permissions);
			roles.add(orgRole);
			this.roleRepository.saveAll(roles);
		}
	}
}
