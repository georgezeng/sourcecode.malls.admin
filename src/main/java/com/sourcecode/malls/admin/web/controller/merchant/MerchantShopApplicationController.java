package com.sourcecode.malls.admin.web.controller.merchant;

import java.util.ArrayList;
import java.util.List;
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
import com.sourcecode.malls.admin.domain.merchant.MerchantShopApplication;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantShopApplicationDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.enums.VerificationStatus;
import com.sourcecode.malls.admin.service.impl.merchant.MerchantShopApplicationService;
import com.sourcecode.malls.admin.util.AssertUtil;
import com.sourcecode.malls.admin.web.controller.base.BaseController;

@RestController
@RequestMapping(path = "/merchant/shop/application")
public class MerchantShopApplicationController extends BaseController {
	@Autowired
	private MerchantShopApplicationService shopApplicationService;

	private String fileDir = "merchant/shop";

	@RequestMapping(value = "/list")
	public ResultBean<PageResult<MerchantShopApplicationDTO>> list(@RequestBody QueryInfo<MerchantShopApplicationDTO> queryInfo) {
		Page<MerchantShopApplication> pageResult = shopApplicationService.findAll(queryInfo);
		PageResult<MerchantShopApplicationDTO> dtoResult = new PageResult<>(
				pageResult.getContent().stream().map(data -> data.asDTO()).collect(Collectors.toList()), pageResult.getTotalElements());
		return new ResultBean<>(dtoResult);
	}

	@RequestMapping(value = "/load/params/{id}")
	public ResultBean<MerchantShopApplicationDTO> load(@PathVariable Long id) {
		Optional<MerchantShopApplication> data = shopApplicationService.findById(id);
		AssertUtil.assertTrue(data.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		return new ResultBean<>(data.get().asDTO());
	}

	@RequestMapping(value = "/save")
	public ResultBean<Void> save(@RequestBody MerchantShopApplicationDTO dto) {
		AssertUtil.notNull(dto.getId(), ExceptionMessageConstant.NO_SUCH_RECORD);
		Optional<MerchantShopApplication> dataOp = shopApplicationService.findById(dto.getId());
		AssertUtil.assertTrue(dataOp.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		MerchantShopApplication data = dataOp.get();
		AssertUtil.assertTrue(!(VerificationStatus.UnPassed.equals(data.getStatus()) || VerificationStatus.Passed.equals(data.getStatus())),
				ExceptionMessageConstant.HAS_VERIFIED);
		data.setStatus(dto.getStatus());
		if (VerificationStatus.UnPassed.equals(dto.getStatus())) {
			AssertUtil.assertNotEmpty(dto.getReason(), ExceptionMessageConstant.FAILED_REASON_CAN_NOT_BE_EMPTY);
			data.setReason(dto.getReason());
		}
		shopApplicationService.save(data);
		return new ResultBean<>();
	}

	@RequestMapping(value = "/deploy")
	public ResultBean<Void> deploy(@RequestBody MerchantShopApplicationDTO dto) {
		MerchantShopApplication data = checkIsPassed(dto.getId());
		data.setDeployed(true);
		List<String> tmpPaths = new ArrayList<>();
		List<String> newPaths = new ArrayList<>();
		if (dto.getAndroidUrl() != null && dto.getAndroidUrl().startsWith("temp")) {
			String newPath = fileDir + "/" + data.getMerchant().getId() + "/dist/android.apk";
			String tmpPath = dto.getAndroidUrl();
			newPaths.add(newPath);
			tmpPaths.add(tmpPath);
			data.setAndroidUrl(newPath);
		}
		if (dto.getIosUrl() != null && dto.getIosUrl().startsWith("temp")) {
			String newPath = fileDir + "/" + data.getMerchant().getId() + "/dist/ios.ipa";
			String tmpPath = dto.getIosUrl();
			newPaths.add(newPath);
			tmpPaths.add(tmpPath);
			data.setIosUrl(newPath);
		}
		shopApplicationService.save(data);
		transfer(true, tmpPaths, newPaths);
		return new ResultBean<>();
	}

	private MerchantShopApplication checkIsPassed(Long id) {
		Optional<MerchantShopApplication> data = shopApplicationService.findById(id);
		AssertUtil.assertTrue(data.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		AssertUtil.assertTrue(VerificationStatus.Passed.equals(data.get().getStatus()), "尚未审核通过，不能部署");
		return data.get();
	}

	@RequestMapping(value = "/file/load/params/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public Resource loadPhoto(@PathVariable Long id, @RequestParam String filePath) {
		Optional<MerchantShopApplication> dataOp = shopApplicationService.findById(id);
		AssertUtil.assertTrue(dataOp.isPresent(), ExceptionMessageConstant.NO_SUCH_RECORD);
		return load(dataOp.get().getMerchant().getId(), filePath, fileDir, true);
	}
}
