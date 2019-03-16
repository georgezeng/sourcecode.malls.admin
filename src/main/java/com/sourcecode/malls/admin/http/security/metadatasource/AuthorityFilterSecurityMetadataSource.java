package com.sourcecode.malls.admin.http.security.metadatasource;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import com.sourcecode.malls.admin.domain.Authority;
import com.sourcecode.malls.admin.http.security.configattribute.AuthorityConfigAttribute;
import com.sourcecode.malls.admin.service.AuthorityService;

@Component
public class AuthorityFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private AuthorityService authorityService;

	private FilterInvocationSecurityMetadataSource originSource;

	public void setOriginSource(FilterInvocationSecurityMetadataSource originSource) {
		this.originSource = originSource;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		Collection<ConfigAttribute> attrs = originSource.getAttributes(object);
		HttpServletRequest request = ((FilterInvocation) object).getRequest();
		String link = request.getRequestURI().split("\\/p\\/")[0];
		boolean found = false;
		for (ConfigAttribute attr : attrs) {
			if (AuthorityConfigAttribute.class.isAssignableFrom(attr.getClass())) {
				AuthorityConfigAttribute configAttr = (AuthorityConfigAttribute) attr;
				if (configAttr.getAuth() != null && link.equals(configAttr.getAuth().getLink()) && configAttr.getAuth().getMethod() != null
						&& request.getMethod().equalsIgnoreCase(configAttr.getAuth().getMethod())) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			Optional<Authority> authOp = authorityService.findByLink(link);
			attrs.add(new AuthorityConfigAttribute(authOp.orElse(null)));
		}
		return attrs;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return originSource.getAllConfigAttributes();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}