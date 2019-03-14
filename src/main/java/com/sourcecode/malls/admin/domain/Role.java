package com.sourcecode.malls.admin.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.sourcecode.malls.admin.domain.base.BaseRole;
import com.sourcecode.malls.admin.dto.RoleDTO;

@Table(name = "role")
@Entity
public class Role extends BaseRole {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<Authority> authorities;

	public Set<GrantedAuthority> getGrantedAuthorities() {
		Set<GrantedAuthority> set = new HashSet<>();
		if (authorities != null) {
			for (Authority auth : authorities) {
				set.add(new SimpleGrantedAuthority(auth.getCode()));
			}
		}
		return set;
	}

	public void addAuthority(Authority auth) {
		if (authorities == null) {
			authorities = new HashSet<>();
		}
		authorities.add(auth);
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void addUser(User user) {
		if (users == null) {
			users = new HashSet<>();
		}
		users.add(user);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public RoleDTO asDTO() {
		return asDTO(false);
	}

	public RoleDTO asDTO(boolean withUserAndAuthority) {
		RoleDTO dto = new RoleDTO();
		BeanUtils.copyProperties(this, dto, "users", "authorities");
		if (withUserAndAuthority) {
			if (authorities != null) {
				dto.setAuthorities(authorities.stream().map(auth -> auth.asDTO()).collect(Collectors.toList()));
			}
			if (users != null) {
				dto.setUsers(users.stream().map(user -> user.asDTO()).collect(Collectors.toList()));
			}
		}
		return dto;
	}

}
