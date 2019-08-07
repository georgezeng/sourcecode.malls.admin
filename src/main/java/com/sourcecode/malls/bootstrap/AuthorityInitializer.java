package com.sourcecode.malls.bootstrap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sourcecode.malls.service.impl.AuthorityService;

@Component
public class AuthorityInitializer {
	@Autowired
	@Qualifier("MallsAuthorityService")
	private AuthorityService service;

	@PostConstruct
	public void init() {
		service.prepareAuthorities();
	}
}
