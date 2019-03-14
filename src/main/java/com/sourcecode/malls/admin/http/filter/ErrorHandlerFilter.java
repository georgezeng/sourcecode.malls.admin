package com.sourcecode.malls.admin.http.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.exception.BusinessException;
import com.sourcecode.malls.admin.util.LogUtil;

@Component
public class ErrorHandlerFilter extends GenericFilterBean {
	@Autowired
	private ObjectMapper mapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			String traceId = LogUtil.getTraceId();
			if (!(IOException.class.isAssignableFrom(e.getClass()) && e.getMessage().toLowerCase().contains("reset by peer"))) {
				logger.error("[" + traceId + "]: " + e.getMessage(), e);
			}
			String msg = null;
			if (!response.isCommitted()) {
				if (BusinessException.class.isAssignableFrom(e.getClass())) {
					BusinessException ex = (BusinessException) e;
					msg = ex.getMessage();
				} else if (NestedServletException.class.isAssignableFrom(e.getClass())) {
					Throwable cause = ((NestedServletException) e).getRootCause();
					if (BusinessException.class.isAssignableFrom(cause.getClass())) {
						BusinessException ex = (BusinessException) cause;
						msg = ex.getMessage();
					}
				}
				if (msg == null) {
					msg = "系统错误";
				}
				String result = mapper.writeValueAsString(new ResultBean<>(traceId, msg));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().print(result);
				response.getWriter().flush();
				response.getWriter().close();
			}
		} finally {
			MDC.clear();
		}
	}

}
