package com.sourcecode.malls.admin.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import com.sourcecode.malls.admin.domain.base.BaseAuthority;
import com.sourcecode.malls.admin.dto.AuthorityDTO;

@Table(name = "authority")
@Entity
public class Authority extends BaseAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_authority", joinColumns = @JoinColumn(name = "authority_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public void addRole(Role role) {
		if (roles == null) {
			roles = new HashSet<>();
		}
		roles.add(role);
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public AuthorityDTO asDTO() {
		AuthorityDTO dto = new AuthorityDTO();
		BeanUtils.copyProperties(this, dto, "roles");
		return dto;
	}

}
