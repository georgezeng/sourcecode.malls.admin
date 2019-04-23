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
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.domain.merchant.MerchantVerification;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantVerificationDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.enums.VerificationStatus;
import com.sourcecode.malls.admin.repository.jpa.impl.MerchantVerificationRepository;
import com.sourcecode.malls.admin.service.FileOnlineSystemService;
import com.sourcecode.malls.admin.service.MerchantVerificationService;
import com.sourcecode.malls.admin.util.AssertUtil;

@RestController
@RequestMapping(path = "/merchant/verification")
public class MerchantVerificationController {
	@Autowired
	private MerchantVerificationService merchantVerificationService;
	@Autowired
	private MerchantVerificationRepository verificationRepository;
	@Autowired
	private FileOnlineSystemService fileService;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<MerchantVerificationDTO>> list(@RequestBody QueryInfo<MerchantVerificationDTO> queryInfo) {
		Page<MerchantVerification> pageResult = merchantVerificationService.findAll(queryInfo);
		PageResult<MerchantVerificationDTO> dtoResult = new PageResult<>(
				pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()), pageResult.getTotalElements());
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/one/params/{id}")
	public ResultBean<MerchantVerificationDTO> load(@PathVariable Long id) {
		Optional<MerchantVerification> verification = merchantVerificationService.findById(id);
		AssertUtil.assertTrue(verification.isPresent(), "记录不存在");
		return new ResultBean<>(verification.get().asDTO());
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody MerchantVerificationDTO dto) {
		AssertUtil.notNull(dto.getId(), "记录不存在");
		Optional<MerchantVerification> dataOp = merchantVerificationService.findById(dto.getId());
		AssertUtil.assertTrue(dataOp.isPresent(), "记录不存在");
		MerchantVerification data = dataOp.get();
		AssertUtil.assertTrue(!(VerificationStatus.UnPassed.equals(data.getStatus()) || VerificationStatus.Passed.equals(data.getStatus())), "已经审核过");
		data.setStatus(dto.getStatus());
		if (VerificationStatus.UnPassed.equals(dto.getStatus())) {
			AssertUtil.assertNotEmpty(dto.getReason(), "失败原因不能为空");
			data.setReason(dto.getReason());
		}
		merchantVerificationService.save(data);
		return new ResultBean<>();
	}

	@RequestMapping(value = "/photo/load/params/{id}")
	public Resource loadPhoto(@PathVariable Long id) {
		Optional<MerchantVerification> dataOp = verificationRepository.findByMerchantId(id);
		return new ByteArrayResource(fileService.load(false, dataOp.get().getPhoto()));
	}
}
