package com.sourcecode.malls.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sourcecode.malls.domain.system.Authority;
import com.sourcecode.malls.repository.jpa.impl.system.AuthorityRepository;
import com.sourcecode.malls.web.controller.AuthorityDefinitions;

@Service
@Transactional
public class MallsAuthorityService {

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
