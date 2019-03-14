package com.sourcecode.malls.admin.repository.jpa.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcecode.malls.admin.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Page<User> findAllByUsernameLike(String username, Pageable pageable);
}
