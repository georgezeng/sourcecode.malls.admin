package com.sourcecode.malls.admin.http.security.voter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.http.security.configattribute.AuthorityConfigAttribute;
import com.sourcecode.malls.admin.http.session.UserContext;
import com.sourcecode.malls.admin.service.UserService;

@Component
public class AuthorityVoter implements AccessDecisionVoter<FilterInvocation> {
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return AuthorityConfigAttribute.class.isAssignableFrom(attribute.getClass());
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Transactional(readOnly = true)
	@Override
	public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
		boolean isGranted = false;
		User user = UserContext.get();
		if (user == User.SystemUser) {
			return ACCESS_DENIED;
		}
		user = userService.findById(user.getId()).orElse(null);
		if (user == null || !userService.isSuperAdmin(user)) {
			for (ConfigAttribute configAttr : attributes) {
				if (AuthorityConfigAttribute.class.isAssignableFrom(configAttr.getClass())) {
					AuthorityConfigAttribute attr = (AuthorityConfigAttribute) configAttr;
					if (attr.getAuth() != null) {
						String method = attr.getAuth().getMethod();
						if (method != null && !method.equalsIgnoreCase(fi.getHttpRequest().getMethod())) {
							isGranted = false;
							continue;
						}
						String authority = attr.getAttribute();
						if (user == null || !user.isEnabled()
								|| !user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equalsIgnoreCase(authority))) {
							isGranted = false;
							continue;
						}
						isGranted = true;
						break;
					}
				}
			}
		}
		if (isGranted) {
			return ACCESS_GRANTED;
		} else {
			return ACCESS_DENIED;
		}
	}

}
