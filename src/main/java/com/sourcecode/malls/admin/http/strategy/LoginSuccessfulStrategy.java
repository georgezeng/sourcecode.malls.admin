package com.sourcecode.malls.admin.http.strategy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcecode.malls.admin.dto.LoginSuccessDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;

@Component
public class LoginSuccessfulStrategy implements RedirectStrategy {
	@Autowired
	private ObjectMapper mapper;

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(mapper.writeValueAsString(new ResultBean<>(new LoginSuccessDTO())));
		response.getWriter().flush();
		response.getWriter().close();
	}

}
