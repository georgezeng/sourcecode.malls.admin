package com.sourcecode.malls.service.impl.merchant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sourcecode.malls.domain.merchant.Merchant;
import com.sourcecode.malls.dto.merchant.MerchantDTO;
import com.sourcecode.malls.dto.query.QueryInfo;
import com.sourcecode.malls.properties.SuperAdminProperties;
import com.sourcecode.malls.repository.jpa.impl.merchant.MerchantRepository;
import com.sourcecode.malls.service.base.JpaService;

@Service
@Transactional
public class MerchantService implements JpaService<Merchant, Long> {
	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private SuperAdminProperties superAdminProperties;

	@Transactional(readOnly = true)
	public Page<Merchant> findAll(QueryInfo<MerchantDTO> queryInfo) {
		MerchantDTO data = queryInfo.getData();
		Page<Merchant> pageResult = null;
		Specification<Merchant> spec = new Specification<Merchant>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Merchant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<>();
				if (data != null) {
					predicate.add(criteriaBuilder.notEqual(root.get("username").as(String.class), superAdminProperties.getUsername()));
					predicate.add(criteriaBuilder.isNull(root.get("parent")));
					if (!StringUtils.isEmpty(data.getSearchText())) {
						String like = "%" + data.getSearchText() + "%";
						predicate.add(criteriaBuilder.or(criteriaBuilder.like(root.get("username").as(String.class), like),
								criteriaBuilder.like(root.get("email").as(String.class), like)));
					}
					if (!"all".equals(data.getStatusText())) {
						predicate.add(criteriaBuilder.equal(root.get("enabled").as(boolean.class), Boolean.valueOf(data.getStatusText())));
					}
				}
				return query.where(predicate.toArray(new Predicate[] {})).getRestriction();
			}
		};
		pageResult = merchantRepository.findAll(spec, queryInfo.getPage().pageable());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public boolean isSuperAdmin(Merchant merchant) {
		return merchant.getUsername().equals(superAdminProperties.getUsername());
	}

	@Override
	public JpaRepository<Merchant, Long> getRepository() {
		return merchantRepository;
	}
}
