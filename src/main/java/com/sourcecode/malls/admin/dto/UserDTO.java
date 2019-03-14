package com.sourcecode.malls.admin.dto;

import org.springframework.beans.BeanUtils;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.domain.base.BaseUser;

public class UserDTO extends BaseUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User asEntity() {
		User entity = new User();
		BeanUtils.copyProperties(this, entity);
		return entity;
	}
}
