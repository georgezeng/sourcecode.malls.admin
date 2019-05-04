package com.sourcecode.malls.admin.web.controller.merchant;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sourcecode.malls.admin.constants.ExceptionMessageConstant;
import com.sourcecode.malls.admin.domain.merchant.MerchantVerification;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantVerificationDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.enums.VerificationStatus;
import com.sourcecode.malls.admin.repository.jpa.impl.merchant.MerchantVerificationRepository;
import com.sourcecode.malls.admin.service.impl.merchant.MerchantVerificationService;
import com.sourcecode.malls.admin.util.AssertUtil;
import com.sourcecode.malls.admin.web.controller.base.BaseController;

@RestController
@RequestMapping(path = "/merchant/verification")
public class MerchantVerificationController extends BaseController {
	@Autowired
	private MerchantVerificationService merchantVerificationService;
	@Autowired
	private MerchantVerificationRepository verificationRepository;

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<MerchantVerificationDTO>> list(@RequestBody QueryInfo<MerchantVerificationDTO> queryInfo) {
		Page<MerchantVerification> pageResult = merchantVerificationService.findAll(queryInfo);
		PageResult<MerchantVerificationDTO> dtoResult = new PageResult<>(
				pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()), pageResult.getTotalElements());
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/load/params/{id}")
	public ResultBean<MerchantVerificationDTO> load(@PathVariable Long id) {
		Optional<MerchantVerification> verification = merchantVerificationService.findById(id);
		AssertUtil.assertTrue(verification.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		return new ResultBean<>(verification.get().asDTO());
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody MerchantVerificationDTO dto) {
		AssertUtil.notNull(dto.getId(), ExceptionMessageConstant.NO_SUCH_RECORD);
		Optional<MerchantVerification> dataOp = merchantVerificationService.findById(dto.getId());
		AssertUtil.assertTrue(dataOp.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		MerchantVerification data = dataOp.get();
		AssertUtil.assertTrue(!(VerificationStatus.UnPassed.equals(data.getStatus()) || VerificationStatus.Passed.equals(data.getStatus())),
				ExceptionMessageConstant.HAS_VERIFIED);
		data.setStatus(dto.getStatus());
		if (VerificationStatus.UnPassed.equals(dto.getStatus())) {
			AssertUtil.assertNotEmpty(dto.getReason(), ExceptionMessageConstant.FAILED_REASON_CAN_NOT_BE_EMPTY);
			data.setReason(dto.getReason());
		}
		merchantVerificationService.save(data);
		return new ResultBean<>();
	}

	@RequestMapping(value = "/file/load/params/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public Resource loadPhoto(@PathVariable Long id, @RequestParam String filePath) {
		Optional<MerchantVerification> dataOp = verificationRepository.findById(id);
		AssertUtil.assertTrue(dataOp.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		return load(dataOp.get().getMerchant().getId(), filePath, "merchant/verification", false);
	}
}
