package com.sourcecode.malls.admin.http.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/message")
public class MessageController {
	
	@RequestMapping(path="/count")
	public int count() {
		return 0;
	}
}
