package com.sourcecode.malls.admin.http.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.domain.Authority;
import com.sourcecode.malls.admin.dto.AuthorityDTO;
import com.sourcecode.malls.admin.dto.base.KeyDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.service.AuthorityService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(value = "/authority")
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<AuthorityDTO>> list(@RequestBody QueryInfo<String> queryInfo) {
		Page<Authority> pageResult = authorityService.findAll(queryInfo);
		PageResult<AuthorityDTO> dtoResult = new PageResult<>();
		if (pageResult.hasContent()) {
			dtoResult = new PageResult<>(pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()),
					pageResult.getTotalElements());
		}
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody AuthorityDTO dto) {
		Authority data = null;
		if (dto.getId() != null) {
			Optional<Authority> dataOp = authorityService.findById(dto.getId());
			AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
			data = dataOp.get();
			AssertUtil.assertTrue(!authorityService.isSuperAdmin(data), "不能修改超级管理员权限");
			BeanUtils.copyProperties(dto, data, "id");
		} else {
			data = dto.asEntity();
		}
		authorityService.save(data);
		return new ResultBean<>();
	}

	@RequestMapping(value = "/one/{id}")
	public ResultBean<AuthorityDTO> findOne(@PathVariable Long id) {
		Optional<Authority> dataOp = authorityService.findById(id);
		AssertUtil.assertTrue(dataOp.isPresent(), "查找不到相应的记录");
		return new ResultBean<>(dataOp.get().asDTO());
	}

	@RequestMapping(value = "/delete")
	public ResultBean<Void> delete(@RequestBody KeyDTO<Long> keys) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), "必须选择至少一条记录进行删除");
		authorityService.deleteByKeys(keys.getIds());
		return new ResultBean<>();
	}
}
