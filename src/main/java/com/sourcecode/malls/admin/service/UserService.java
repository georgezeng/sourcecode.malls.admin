package com.sourcecode.malls.admin.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.sourcecode.malls.admin.constants.EnvConstant;
import com.sourcecode.malls.admin.domain.Role;
import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.dto.RoleDTO;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.properties.SuperAdminProperties;
import com.sourcecode.malls.admin.repository.jpa.impl.RoleRepository;
import com.sourcecode.malls.admin.repository.jpa.impl.UserRepository;
import com.sourcecode.malls.admin.service.base.JpaService;
import com.sourcecode.malls.admin.util.AssertUtil;

@Service
@Transactional
public class UserService implements UserDetailsService, JpaService<User, Long> {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SuperAdminProperties superAdminProperties;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Autowired
	private Environment env;

	public void prepareSuperAdmin() {
		Optional<User> superAdminOp = userRepository.findByUsername(superAdminProperties.getUsername());
		User user = null;
		if (!superAdminOp.isPresent()) {
			user = new User();
			user.setUsername(superAdminProperties.getUsername());
			user.setEnabled(true);
		} else {
			user = superAdminOp.get();
		}
		if (env.acceptsProfiles(Profiles.of(EnvConstant.PROD))) {
			user.setPassword(superAdminProperties.getPassword());
		} else {
			user.setPassword(passwordEncoder.encode(superAdminProperties.getPassword()));
		}
		user.setEmail(superAdminProperties.getEmail());
		userRepository.save(user);
		roleService.prepareSuperAdmin(user);
	}

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOp = userRepository.findByUsername(username);
		if (!userOp.isPresent()) {
			throw new UsernameNotFoundException("用户名或密码有误");
		}
		userOp.get().getAuthorities();
		return userOp.get();
	}

	@Transactional(readOnly = true)
	public Optional<User> findByIdWithAuthorities(Long userId) {
		Optional<User> user = findById(userId);
		if (user.isPresent()) {
			user.get().getAuthorities();
		}
		return user;
	}

	@Transactional(readOnly = true)
	public Optional<User> findByUsernameWithAuthorities(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			user.get().getAuthorities();
		}
		return user;
	}

	@Transactional(readOnly = true)
	public Page<User> findAll(QueryInfo<String> queryInfo) {
		String nameOrCode = queryInfo.getData();
		Page<User> pageReulst = null;
		if (!StringUtils.isEmpty(nameOrCode)) {
			String like = "%" + nameOrCode + "%";
			pageReulst = userRepository.findAllByUsernameLike(like, queryInfo.getPage().pageable());
		} else {
			pageReulst = userRepository.findAll(queryInfo.getPage().pageable());
		}
		return pageReulst;
	}

	@Transactional(readOnly = true)
	public boolean isSuperAdmin(User user) {
		return user.getUsername().equals(superAdminProperties.getUsername());
	}

	public void relateToRoles(User user, List<RoleDTO> roles) {
		save(user);
		user.setRoles(new HashSet<>());
		if (!CollectionUtils.isEmpty(roles)) {
			for (RoleDTO roleDTO : roles) {
				Optional<Role> roleOp = roleRepository.findById(roleDTO.getId());
				if (roleOp.isPresent()) {
					Role role = roleOp.get();
					boolean found = false;
					if (role.getUsers() != null) {
						for (User data : role.getUsers()) {
							if (data.getUsername().equals(user.getUsername())) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						role.addUser(user);
					}
					user.addRole(role);
				}
			}
		}
		if (isSuperAdmin(user)) {
			AssertUtil.assertTrue(user.getRoles().size() == 1, "超级管理员不需要添加其他角色");
		}
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(user.getRoles()), "请关联至少一条角色记录");
		roleRepository.saveAll(user.getRoles());
	}

	@Override
	public JpaRepository<User, Long> getRepository() {
		return userRepository;
	}
}
