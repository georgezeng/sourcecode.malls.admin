package com.sourcecode.malls.admin.bootstrap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sourcecode.malls.admin.service.impl.merchant.AuthorityService;

@Component
public class AuthorityInitializer {
	@Autowired
	private AuthorityService service;

	@PostConstruct
	public void init() {
		service.prepareAuthorities();
	}
}
