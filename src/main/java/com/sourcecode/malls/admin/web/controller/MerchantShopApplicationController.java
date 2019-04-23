package com.sourcecode.malls.admin.web.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.domain.merchant.MerchantShopApplication;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantShopApplicationDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.enums.VerificationStatus;
import com.sourcecode.malls.admin.service.FileOnlineSystemService;
import com.sourcecode.malls.admin.service.MerchantShopApplicationService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(path = "/merchant/shop/application")
public class MerchantShopApplicationController {
	@Autowired
	private MerchantShopApplicationService shopApplicationService;
	@Autowired
	private FileOnlineSystemService fileService;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<MerchantShopApplicationDTO>> list(@RequestBody QueryInfo<MerchantShopApplicationDTO> queryInfo) {
		Page<MerchantShopApplication> pageResult = shopApplicationService.findAll(queryInfo);
		PageResult<MerchantShopApplicationDTO> dtoResult = new PageResult<>(
				pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()), pageResult.getTotalElements());
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/one/params/{id}")
	public ResultBean<MerchantShopApplicationDTO> load(@PathVariable Long id) {
		Optional<MerchantShopApplication> data = shopApplicationService.findById(id);
		AssertUtil.assertTrue(data.isPresent(), "记录不存在");
		return new ResultBean<>(data.get().asDTO());
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody MerchantShopApplicationDTO dto) {
		AssertUtil.notNull(dto.getId(), "记录不存在");
		Optional<MerchantShopApplication> dataOp = shopApplicationService.findById(dto.getId());
		AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
		MerchantShopApplication data = dataOp.get();
		AssertUtil.assertTrue(!(VerificationStatus.UnPassed.equals(data.getStatus()) || VerificationStatus.Passed.equals(data.getStatus())), "已经审核过");
		data.setStatus(dto.getStatus());
		if (VerificationStatus.UnPassed.equals(dto.getStatus())) {
			AssertUtil.assertNotEmpty(dto.getReason(), "失败原因不能为空");
			data.setReason(dto.getReason());
		}
		shopApplicationService.save(data);
		if (VerificationStatus.Passed.equals(data.getStatus())) {
			shopApplicationService.createShop(data);
		}
		return new ResultBean<>();
	}

	@RequestMapping(value = "/img/load")
	public Resource loadImg(@RequestParam String filePath) {
		AssertUtil.assertTrue(filePath.startsWith("merchant/shop/application"), "图片路径不合法");
		return new ByteArrayResource(fileService.load(false, filePath));
	}

	@RequestMapping(value = "/deployed/params/{id}")
	public ResultBean<Void> deployed(@PathVariable Long id) {
		Optional<MerchantShopApplication> data = shopApplicationService.findById(id);
		AssertUtil.assertTrue(data.isPresent(), "记录不存在");
		AssertUtil.assertTrue(VerificationStatus.Passed.equals(data.get().getStatus()), "尚未审核通过，不能设置部署状态");
		data.get().setDeployed(true);
		shopApplicationService.save(data.get());
		return new ResultBean<>();
	}
}
