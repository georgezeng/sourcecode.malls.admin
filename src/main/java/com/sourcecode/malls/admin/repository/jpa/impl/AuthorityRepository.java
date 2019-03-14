package com.sourcecode.malls.admin.repository.jpa.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcecode.malls.admin.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Optional<Authority> findByCode(String code);
	
	Page<Authority> findAllByCodeLikeOrNameLike(String code, String name, Pageable pageable);
}
