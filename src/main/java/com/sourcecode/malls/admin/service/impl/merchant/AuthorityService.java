package com.sourcecode.malls.admin.service.impl.merchant;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcecode.malls.admin.domain.system.setting.Authority;
import com.sourcecode.malls.admin.repository.jpa.impl.system.AuthorityRepository;
import com.sourcecode.malls.admin.web.controller.AuthorityDefinitions;

@Service("MallsAuthorityService")
@Transactional
public class AuthorityService {

	@Autowired
	private AuthorityRepository authRepository;

	public void prepareAuthorities() {
		for (AuthorityDefinitions definition : AuthorityDefinitions.values()) {
			prepareAuthority(definition);
		}
	}

	private void prepareAuthority(AuthorityDefinitions definition) {
		Optional<Authority> authOp = authRepository.findByCode(definition.getCode());
		Authority auth = null;
		if (!authOp.isPresent()) {
			auth = new Authority();
			auth.setCode(definition.getCode());
		} else {
			auth = authOp.get();
		}
		auth.setName(definition.getName());
		auth.setLink(definition.getLink());
		auth.setMethod(definition.getMethod());
		authRepository.save(auth);
	}

}
