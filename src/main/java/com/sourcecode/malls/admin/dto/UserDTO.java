package com.sourcecode.malls.admin.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.domain.base.BaseUser;

public class UserDTO extends BaseUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<RoleDTO> roles;

	public User asEntity() {
		User entity = new User();
		BeanUtils.copyProperties(this, entity);
		return entity;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

}
