package com.sourcecode.malls.admin.http.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.domain.User;
import com.sourcecode.malls.admin.dto.UserDTO;
import com.sourcecode.malls.admin.dto.base.KeyDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.http.session.UserContext;
import com.sourcecode.malls.admin.service.UserService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/current")
	public ResultBean<UserDTO> currentUser() {
		return new ResultBean<>(UserContext.get().asDTO());
	}

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<UserDTO>> list(@RequestBody QueryInfo<String> queryInfo) {
		Page<User> pageResult = userService.findAll(queryInfo);
		PageResult<UserDTO> dtoResult = new PageResult<>();
		if (pageResult.hasContent()) {
			dtoResult = new PageResult<>(pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()),
					pageResult.getTotalElements());
		}
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody UserDTO dto) {
		User data = null;
		if (dto.getId() != null) {
			Optional<User> dataOp = userService.findById(dto.getId());
			AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
			data = dataOp.get();
			BeanUtils.copyProperties(dto, data, "id", "username");
		} else {
			data = dto.asEntity();
		}
		if (userService.isSuperAdmin(data)) {
			AssertUtil.assertTrue(data.isEnabled(), "不能禁用超级管理员");
		}
		userService.save(data);
		return new ResultBean<>();
	}

	@RequestMapping(value = "/one/{id}")
	public ResultBean<UserDTO> findOne(Long id) {
		Optional<User> dataOp = userService.findById(id);
		AssertUtil.assertTrue(dataOp.isPresent(), "查找不到相应的记录");
		return new ResultBean<>(dataOp.get().asDTO());
	}

	@RequestMapping(value = "/delete")
	public ResultBean<Void> delete(@RequestBody KeyDTO<Long> keys) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), "必须选择至少一条记录进行删除");
		for (Long id : keys.getIds()) {
			Optional<User> userOp = userService.findById(id);
			if (userOp.isPresent()) {
				User user = userOp.get();
				AssertUtil.assertTrue(!userService.isSuperAdmin(user), "不能删除超级管理员");
				userService.delete(user);
			}
		}
		return new ResultBean<>();
	}
}
