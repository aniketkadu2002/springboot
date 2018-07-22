package com.app.usermanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public Role findRolebyRoleName(String roleName) {
		List<Role> roles = roleRepository.findByRoleName(roleName);
		return roles.size()>0?roles.get(0):null;
	}
	
	public Role findRoleById(int roleId) {
		List<Role> roles = roleRepository.findByRoleId(roleId);
		return roles.size()>0?roles.get(0):null;
	}
}
