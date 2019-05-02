package com.sourcecode.malls.admin.web.controller.merchant;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.constants.ExceptionMessageConstant;
import com.sourcecode.malls.admin.domain.merchant.Merchant;
import com.sourcecode.malls.admin.dto.base.KeyDTO;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.service.impl.merchant.MerchantService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(value = "/merchant/user")
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

	@RequestMapping(value = "/updateStatus/params/{status}")
	public ResultBean<Void> updateStatus(@RequestBody KeyDTO<Long> keys, @PathVariable Boolean status) {
		AssertUtil.assertTrue(!CollectionUtils.isEmpty(keys.getIds()), ExceptionMessageConstant.SELECT_AT_LEAST_ONE_TO_DISABLE);
		for (Long id : keys.getIds()) {
			Optional<Merchant> merchantOp = merchantService.findById(id);
			if (merchantOp.isPresent()) {
				Merchant merchant = merchantOp.get();
				AssertUtil.assertTrue(!merchantService.isSuperAdmin(merchant), ExceptionMessageConstant.CAN_NOT_DISABLE_ADMIN);
				merchant.setEnabled(status);
				if (!status && !CollectionUtils.isEmpty(merchant.getSubList())) {
					for (Merchant sub : merchant.getSubList()) {
						sub.setEnabled(status);
					}
				}
				merchantService.save(merchant);
			}
		}
		return new ResultBean<>();
	}

}
