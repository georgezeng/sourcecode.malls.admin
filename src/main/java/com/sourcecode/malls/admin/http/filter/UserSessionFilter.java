package com.sourcecode.malls.admin.http.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.exception.BusinessException;
import com.sourcecode.malls.admin.http.session.UserContext;
import com.sourcecode.malls.admin.properties.SessionAttributesProperties;
import com.sourcecode.malls.admin.repository.jpa.impl.UserRepository;

@Component
public class UserSessionFilter extends GenericFilterBean {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionAttributesProperties sessionProperties;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpSession session = ((HttpServletRequest) request).getSession();
			Long userId = (Long) session.getAttribute(sessionProperties.getUserId());
			if (userId != null) {
				Optional<User> user = userRepository.findById(userId);
				if (user.isPresent()) {
					UserContext.set(user.get());
				}
			} else if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
				Authentication token = SecurityContextHolder.getContext().getAuthentication();
				if (RememberMeAuthenticationToken.class.isAssignableFrom(token.getClass())) {
					RememberMeAuthenticationToken rToken = (RememberMeAuthenticationToken) token;
					UserDetails details = (UserDetails) rToken.getPrincipal();
					Optional<User> user = userRepository.findByUsername(details.getUsername());
					if (user.isPresent()) {
						UserContext.set(user.get());
						session.setAttribute(sessionProperties.getUserId(), user.get().getId());
					}
				}
			} else {
				throw new BusinessException("用户登录状态有误");
			}
			chain.doFilter(request, response);
		} finally {
			UserContext.set(null);
		}
	}

}
