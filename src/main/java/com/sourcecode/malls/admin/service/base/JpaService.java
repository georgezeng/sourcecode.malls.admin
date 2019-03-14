package com.sourcecode.malls.admin.service.base;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sourcecode.malls.admin.domain.base.BaseEntity;

public interface JpaService<E extends BaseEntity, K extends Serializable> {
	JpaRepository<E, K> getRepository();

	default Optional<E> findById(K id) {
		return getRepository().findById(id);
	}

	default void save(E entity) {
		getRepository().save(entity);
	}

	default void delete(E entity) {
		getRepository().delete(entity);
	}

	default void deleteById(K id) {
		Optional<E> entity = getRepository().findById(id);
		if (entity.isPresent()) {
			delete(entity.get());
		}
	}

	default void deleteByKeys(Iterable<K> ids) {
		for (K id : ids) {
			deleteById(id);
		}
	}

	default void delete(Iterable<E> entities) {
		getRepository().deleteAll(entities);
	}

}
