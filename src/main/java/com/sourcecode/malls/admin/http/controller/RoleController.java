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

import com.sourcecode.malls.admin.domain.Role;
import com.sourcecode.malls.admin.dto.RoleDTO;
import com.sourcecode.malls.admin.dto.base.KeyDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.service.RoleService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<RoleDTO>> list(@RequestBody QueryInfo<String> queryInfo) {
		Page<Role> pageResult = roleService.findAll(queryInfo);
		PageResult<RoleDTO> dtoResult = new PageResult<>();
		if (pageResult.hasContent()) {
			dtoResult = new PageResult<>(pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()),
					pageResult.getTotalElements());
		}
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody RoleDTO dto) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(dto.getAuthorities()), "请关联至少一条权限记录");
		Role data = null;
		if (dto.getId() != null) {
			Optional<Role> dataOp = roleService.findById(dto.getId());
			AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
			data = dataOp.get();
			BeanUtils.copyProperties(dto, data, "id");
		} else {
			data = dto.asEntity();
		}
		roleService.relateToUsersAndAuthorities(data, dto.getUsers(), dto.getAuthorities());
		return new ResultBean<>();
	}

	@RequestMapping(value = "/one/{id}")
	public ResultBean<RoleDTO> findOne(@PathVariable("id") Long id) {
		Optional<Role> dataOp = roleService.findById(id);
		AssertUtil.assertTrue(dataOp.isPresent(), "查找不到相应的记录");
		return new ResultBean<>(dataOp.get().asDTO(true));
	}

	@RequestMapping(value = "/delete")
	public ResultBean<Void> delete(@RequestBody KeyDTO<Long> keys) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), "必须选择至少一条记录进行删除");
		for (Long id : keys.getIds()) {
			Optional<Role> roleOp = roleService.findById(id);
			if (roleOp.isPresent()) {
				Role role = roleOp.get();
				AssertUtil.assertTrue(CollectionUtils.isEmpty(role.getUsers()), "该角色存在用户数据，不能删除该角色");
				roleService.delete(role);
			}
		}
		return new ResultBean<>();
	}
}
