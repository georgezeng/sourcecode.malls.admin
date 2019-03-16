package com.sourcecode.malls.admin.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.sourcecode.malls.admin.domain.Authority;
import com.sourcecode.malls.admin.domain.Role;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.properties.SuperAdminProperties;
import com.sourcecode.malls.admin.repository.jpa.impl.AuthorityRepository;
import com.sourcecode.malls.admin.service.base.JpaService;

@Service
@Transactional
public class AuthorityService implements JpaService<Authority, Long> {
	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private SuperAdminProperties superAdminProperties;

	public void prepareSuperAdmin(Role role) {
		Optional<Authority> authorityOp = authorityRepository.findByCode(superAdminProperties.getAuthority());
		if (!authorityOp.isPresent()) {
			Authority auth = new Authority();
			auth.setCode(superAdminProperties.getAuthority());
			auth.setName("超级管理员权限");
			auth.setLink("/**");
			auth.addRole(role);
			authorityRepository.save(auth);
		}
	}

	@Transactional(readOnly = true)
	public Page<Authority> findAll(QueryInfo<String> queryInfo) {
		String nameOrCode = queryInfo.getData();
		Page<Authority> pageReulst = null;
		if (!StringUtils.isEmpty(nameOrCode)) {
			String like = "%" + nameOrCode + "%";
			pageReulst = authorityRepository.findAllByCodeLikeOrNameLike(like, like, queryInfo.getPage().pageable());
		} else {
			pageReulst = authorityRepository.findAll(queryInfo.getPage().pageable());
		}
		return pageReulst;
	}

	@Transactional(readOnly = true)
	public boolean isSuperAdmin(Authority authority) {
		return authority.getCode().equals(superAdminProperties.getAuthority());
	}

	@Transactional(readOnly = true)
	public Optional<Authority> findByLink(String link) {
		return authorityRepository.findByLink(link);
	}

	@Override
	public JpaRepository<Authority, Long> getRepository() {
		return authorityRepository;
	}
}
