package com.sourcecode.malls.admin.web.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.sourcecode.malls.admin.domain.merchant.MerchantShopApplication;
import com.sourcecode.malls.admin.dto.base.ResultBean;
import com.sourcecode.malls.admin.dto.merchant.MerchantShopApplicationDTO;
import com.sourcecode.malls.admin.dto.query.PageResult;
import com.sourcecode.malls.admin.dto.query.QueryInfo;
import com.sourcecode.malls.admin.enums.VerificationStatus;
import com.sourcecode.malls.admin.exception.BusinessException;
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
		return new ResultBean<>();
	}

	@RequestMapping(value = "/img/load")
	public Resource loadImg(@RequestParam String filePath) {
		AssertUtil.assertTrue(filePath.startsWith("merchant/shop/application"), "图片路径不合法");
		return new ByteArrayResource(fileService.load(false, filePath));
	}

	@RequestMapping(value = "/upload/params/{id}/{type}")
	public ResultBean<String> upload(@PathVariable Long id, @PathVariable String type, @RequestParam("file") MultipartFile file) throws Exception {
		MerchantShopApplication data = checkIsPassed(id);
		if ("android".equals(type)) {
			AssertUtil.assertTrue(data.isAndroidType(), "不能上传安卓应用,商家没有设置");
		} else if ("ios".equals(type)) {
			AssertUtil.assertTrue(data.isIosType(), "不能上传苹果应用,商家没有设置");
		} else {
			throw new BusinessException("上传失败，未知类型");
		}
		String filePath = "temp/merchant/shop/application/" + id + "/" + type + System.nanoTime() + ".png";
		fileService.upload(false, filePath, file.getInputStream());
		return new ResultBean<>(filePath);
	}

	@RequestMapping(value = "/deploy")
	public ResultBean<Void> deploy(@RequestBody MerchantShopApplicationDTO dto) {
		MerchantShopApplication data = checkIsPassed(dto.getId());
		data.setDeployed(true);
		List<String> tmpPaths = new ArrayList<>();
		List<String> newPaths = new ArrayList<>();
		if (dto.getAndroidUrl() != null && dto.getAndroidUrl().startsWith("temp")) {
			String extendName = dto.getAndroidUrl().substring(dto.getAndroidUrl().lastIndexOf("."));
			String newPath = "merchant/shop/application/" + data.getMerchant().getId() + "/android" + extendName;
			String tmpPath = dto.getAndroidUrl();
			newPaths.add(newPath);
			tmpPaths.add(tmpPath);
			data.setAndroidUrl(newPath);
		}
		if (dto.getIosUrl() != null && dto.getIosUrl().startsWith("temp")) {
			String extendName = dto.getIosUrl().substring(dto.getIosUrl().lastIndexOf("."));
			String newPath = "merchant/shop/application/" + data.getMerchant().getId() + "/android" + extendName;
			String tmpPath = dto.getIosUrl();
			newPaths.add(newPath);
			tmpPaths.add(tmpPath);
			data.setIosUrl(newPath);
		}
		shopApplicationService.save(data);
		for (int i = 0; i < newPaths.size(); i++) {
			String newPath = newPaths.get(i);
			String tmpPath = tmpPaths.get(i);
			byte[] buf = fileService.load(false, tmpPath);
			fileService.upload(true, newPath, new ByteArrayInputStream(buf));
		}
		return new ResultBean<>();
	}

	private MerchantShopApplication checkIsPassed(Long id) {
		Optional<MerchantShopApplication> data = shopApplicationService.findById(id);
		AssertUtil.assertTrue(data.isPresent(), "记录不存在");
		AssertUtil.assertTrue(VerificationStatus.Passed.equals(data.get().getStatus()), "尚未审核通过，不能部署");
		AssertUtil.assertTrue(!data.get().isDeployed(), "已经部署过，不能再次部署");
		return data.get();
	}
}
