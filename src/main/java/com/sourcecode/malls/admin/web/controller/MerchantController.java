package com.sourcecode.malls.admin.web.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.domain.merchant.Merchant;
import com.sourcecode.malls.admin.dto.base.KeyDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.service.MerchantService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(value = "/merchant")
public class MerchantController {
	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<MerchantDTO>> list(@RequestBody QueryInfo<MerchantDTO> queryInfo) {
		Page<Merchant> pageResult = merchantService.findAll(queryInfo);
		PageResult<MerchantDTO> dtoResult = new PageResult<>();
		if (pageResult.hasContent()) {
			dtoResult = new PageResult<>(pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()),
					pageResult.getTotalElements());
		}
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/disable")
	public ResultBean<Void> disable(@RequestBody KeyDTO<Long> keys) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), "必须选择至少一条记录进行禁用");
		for (Long id : keys.getIds()) {
			Optional<Merchant> merchantOp = merchantService.findById(id);
			if (merchantOp.isPresent()) {
				Merchant merchant = merchantOp.get();
				AssertUtil.assertTrue(!merchantService.isSuperAdmin(merchant), "不能禁用超级管理员");
				merchant.setEnabled(false);
				merchantService.save(merchant);
			}
		}
		return new ResultBean<>();
	}

	@RequestMapping(value = "/enable")
	public ResultBean<Void> enable(@RequestBody KeyDTO<Long> keys) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), "必须选择至少一条记录进行启用");
		for (Long id : keys.getIds()) {
			Optional<Merchant> merchantOp = merchantService.findById(id);
			if (merchantOp.isPresent()) {
				Merchant merchant = merchantOp.get();
				merchant.setEnabled(true);
				merchantService.save(merchant);
			}
		}
		return new ResultBean<>();
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody MerchantDTO dto) {
		AssertUtil.notNull(dto.getId(), "查找不到记录ID");
		Optional<Merchant> dataOp = merchantService.findById(dto.getId());
		AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
		Merchant data = dataOp.get();
		BeanUtils.copyProperties(dto, data, "id", "username", "password", "roles");
		if (merchantService.isSuperAdmin(data)) {
			AssertUtil.assertTrue(data.isEnabled(), "不能禁用超级管理员");
		}
		merchantService.save(data);
		return new ResultBean<>();
	}

}
