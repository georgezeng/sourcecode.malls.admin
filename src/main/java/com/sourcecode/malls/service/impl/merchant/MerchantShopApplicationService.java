package com.sourcecode.malls.service.impl.merchant;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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
import com.sourcecode.malls.domain.merchant.MerchantShopApplication;
import com.sourcecode.malls.dto.merchant.MerchantShopApplicationDTO;
import com.sourcecode.malls.dto.query.QueryInfo;
import com.sourcecode.malls.enums.VerificationStatus;
import com.sourcecode.malls.properties.SuperAdminProperties;
import com.sourcecode.malls.repository.jpa.impl.merchant.MerchantShopApplicationRepository;
import com.sourcecode.malls.service.base.JpaService;

@Service
@Transactional
public class MerchantShopApplicationService implements JpaService<MerchantShopApplication, Long> {
	@Autowired
	private MerchantShopApplicationRepository shopApplicationRepository;

	@Autowired
	private SuperAdminProperties superAdminProperties;

	@Transactional(readOnly = true)
	public Page<MerchantShopApplication> findAll(QueryInfo<MerchantShopApplicationDTO> queryInfo) {
		MerchantShopApplicationDTO data = queryInfo.getData();
		Page<MerchantShopApplication> pageReulst = null;
		Specification<MerchantShopApplication> spec = new Specification<MerchantShopApplication>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<MerchantShopApplication> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<>();
				if (data != null) {
					Join<MerchantShopApplication, Merchant> joinMerchant = root.join("merchant");
					predicate.add(criteriaBuilder.notEqual(joinMerchant.get("username").as(String.class), superAdminProperties.getUsername()));
					if (!StringUtils.isEmpty(data.getSearchText())) {
						String like = "%" + data.getSearchText() + "%";
						predicate.add(criteriaBuilder.or(criteriaBuilder.like(joinMerchant.get("username").as(String.class), like),
								criteriaBuilder.like(root.get("name").as(String.class), like)));
					}
					if (!"all".equals(data.getStatusText())) {
						predicate.add(criteriaBuilder.equal(root.get("status").as(VerificationStatus.class),
								VerificationStatus.valueOf(data.getStatusText())));
					}
				}
				return query.where(predicate.toArray(new Predicate[] {})).getRestriction();
			}
		};
		pageReulst = shopApplicationRepository.findAll(spec, queryInfo.getPage().pageable());
		return pageReulst;
	}

	@Override
	public JpaRepository<MerchantShopApplication, Long> getRepository() {
		return shopApplicationRepository;
	}

}
