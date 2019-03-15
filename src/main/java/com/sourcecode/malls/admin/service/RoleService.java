package com.sourcecode.malls.admin.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.sourcecode.malls.admin.domain.Authority;
import com.sourcecode.malls.admin.domain.Role;
import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.dto.AuthorityDTO;
import com.sourcecode.malls.admin.dto.RoleDTO;
import com.sourcecode.malls.admin.dto.UserDTO;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.properties.SuperAdminProperties;
import com.sourcecode.malls.admin.repository.jpa.impl.RoleRepository;
import com.sourcecode.malls.admin.repository.jpa.impl.UserRepository;
import com.sourcecode.malls.admin.service.base.JpaService;
import com.sourcecode.malls.admin.util.AssertUtil;

@Service
@Transactional
public class RoleService implements JpaService<Role, Long> {
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private SuperAdminProperties superAdminProperties;

	public void prepareSuperAdmin(User superAdmin) {
		Optional<Role> roleOp = roleRepository.findByCode(superAdminProperties.getCode());
		Role role = null;
		if (!roleOp.isPresent()) {
			role = new Role();
			role.setCode(superAdminProperties.getCode());
			role.setName("超级管理员");
			role.addUser(superAdmin);
			roleRepository.save(role);
		} else {
			role = roleOp.get();
		}
		authorityService.prepareSuperAdmin(role);
	}

	@Transactional(readOnly = true)
	public Page<Role> findAll(QueryInfo<String> queryInfo) {
		String nameOrCode = queryInfo.getData();
		Page<Role> pageReulst = null;
		if (!StringUtils.isEmpty(nameOrCode)) {
			String like = "%" + nameOrCode + "%";
			pageReulst = roleRepository.findAllByCodeLikeOrNameLike(like, like, queryInfo.getPage().pageable());
		} else {
			pageReulst = roleRepository.findAll(queryInfo.getPage().pageable());
		}
		return pageReulst;
	}

	public void relateToUsersAndAuthorities(Role role, List<UserDTO> users, List<AuthorityDTO> authorities) {
		role.setUsers(new HashSet<>());
		if (!CollectionUtils.isEmpty(users)) {
			for (UserDTO user : users) {
				Optional<User> userOp = userRepository.findById(user.getId());
				if (userOp.isPresent()) {
					role.addUser(userOp.get());
				}
			}
		}
		role.setAuthorities(new HashSet<>());
		if (!CollectionUtils.isEmpty(authorities)) {
			for (AuthorityDTO authority : authorities) {
				Optional<Authority> authOp = authorityService.findById(authority.getId());
				if (authOp.isPresent()) {
					role.addAuthority(authOp.get());
				}
			}
		}
		if (isSuperAdmin(role)) {
			AssertUtil.assertTrue(role.getUsers().size() == 1, "超级管理员不能没有用户");
			boolean isSpecificUser = role.getUsers().stream().anyMatch(user -> superAdminProperties.getUsername().equals(user.getUsername()));
			AssertUtil.assertTrue(isSpecificUser, "不能修改超级管理员的所属用户");
		}
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(role.getAuthorities()), "请关联至少一条权限记录");
		roleRepository.save(role);
	}

	public boolean isSuperAdmin(RoleDTO role) {
		return isSuperAdmin(findById(role.getId()).get());
	}

	public boolean isSuperAdmin(Role role) {
		return role.getAuthorities().stream().anyMatch(auth -> superAdminProperties.getAuthority().equals(auth.getCode()));
	}

	@Override
	public void delete(Role entity) {
		AssertUtil.assertTrue(entity.getUsers() == null || entity.getUsers().isEmpty(), "该角色下存在用户的关联信息，不能删除");
		getRepository().delete(entity);
	}

	@Override
	public JpaRepository<Role, Long> getRepository() {
		return roleRepository;
	}
}
