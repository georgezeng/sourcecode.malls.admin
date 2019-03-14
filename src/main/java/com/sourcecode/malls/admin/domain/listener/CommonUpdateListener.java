package com.sourcecode.malls.admin.domain.listener;

import java.sql.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.domain.base.BaseEntity;
import com.sourcecode.malls.admin.http.session.UserContext;
import com.sourcecode.malls.admin.util.DateUtil;

public class CommonUpdateListener {
	@PrePersist
	public void PrePersist(Object object) {
		BaseEntity entity = (BaseEntity) object;
		User currentUser = UserContext.get();

		entity.setCreateBy(currentUser.getUsername());
		entity.setUpdateBy(currentUser.getUsername());

		Date now = DateUtil.now();
		entity.setCreateTime(now);
		entity.setUpdateTime(now);
	}

	@PreUpdate
	public void preUpdate(Object object) {
		BaseEntity entity = (BaseEntity) object;
		User currentUser = UserContext.get();
		entity.setUpdateBy(currentUser.getUsername());
		entity.setUpdateTime(DateUtil.now());
	}
}
