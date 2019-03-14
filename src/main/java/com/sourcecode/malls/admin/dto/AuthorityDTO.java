package com.sourcecode.malls.admin.dto;

import org.springframework.beans.BeanUtils;

import com.sourcecode.malls.admin.domain.Authority;
import com.sourcecode.malls.admin.domain.base.BaseAuthority;

public class AuthorityDTO extends BaseAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Authority asEntity() {
		Authority entity = new Authority();
		BeanUtils.copyProperties(this, entity);
		return entity;
	}
}
