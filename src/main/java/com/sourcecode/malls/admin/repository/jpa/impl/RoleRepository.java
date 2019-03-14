package com.sourcecode.malls.admin.repository.jpa.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcecode.malls.admin.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByCode(String code);

	Page<Role> findAllByCodeLikeOrNameLike(String code, String name, Pageable pageable);
}
