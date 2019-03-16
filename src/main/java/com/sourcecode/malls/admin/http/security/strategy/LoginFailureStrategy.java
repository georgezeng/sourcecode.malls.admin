package com.sourcecode.malls.admin.http.security.strategy;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.util.LogUtil;

@Component
public class LoginFailureStrategy implements RedirectStrategy {
	@Autowired
	private ObjectMapper mapper;

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		Exception ex = (Exception) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(mapper.writeValueAsString(new ResultBean<>(LogUtil.getTraceId(), ex.getMessage())));
		response.getWriter().flush();
		response.getWriter().close();
	}

}
