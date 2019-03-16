package com.sourcecode.malls.admin.http.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.properties.SessionAttributesProperties;

@Component
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private SessionAttributesProperties sessionProperties;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		request.getSession().setAttribute(sessionProperties.getUserId(), ((User) authentication.getPrincipal()).getId());
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
