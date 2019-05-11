package com.sourcecode.malls.web.controller;

public enum AuthorityDefinitions {
	MERCHANT_USER_LIST_PAGE("商家管理-列表页面", "AUTH_MERCHANT_USER_LIST_PAGE", "/Merchant/User/List/Page", "GET"),
	MERCHANT_USER_LIST("商家管理-列表请求", "AUTH_MERCHANT_USER_LIST", "/merchant/user/list", "POST"),
	MERCHANT_USER_UPDATE_STATUS("商家管理-更新状态请求", "AUTH_MERCHANT_USER_UPDATE_STATUS", "/merchant/user/updateStatus", "POST"),
	
	MERCHANT_VERIFICATION_LIST_PAGE("实名审核-列表页面", "AUTH_MERCHANT_VERIFICATION_LIST_PAGE", "/Merchant/Verification/List/Page", "GET"),
	MERCHANT_VERIFICATION_EDIT_PAGE("实名审核-详情页面", "AUTH_MERCHANT_VERIFICATION_EDIT_PAGE", "/Merchant/Verification/Eist/Page", "POST"),
	MERCHANT_VERIFICATION_LIST("实名审核-列表请求", "AUTH_MERCHANT_VERIFICATION_LIST", "/merchant/verification/list", "POST"),
	MERCHANT_VERIFICATION_LOAD("实名审核-加载单个请求", "AUTH_MERCHANT_VERIFICATION_LOAD", "/merchant/verification/load", "GET"),
	MERCHANT_VERIFICATION_SAVE("实名审核-保存请求", "AUTH_MERCHANT_VERIFICATION_SAVE", "/merchant/verification/save", "POST"),
	MERCHANT_VERIFICATION_FILE_LOAD("实名审核-文件读取请求", "AUTH_MERCHANT_VERIFICATION_FILE_LOAD", "/merchant/verification/file/load", "GET"),
	
	MERCHANT_SHOP_APPLICATION_LIST_PAGE("店铺审核-列表页面", "AUTH_MERCHANT_SHOP_APPLICATIONN_LIST_PAGE", "/Merchant/Verification/List/Page", "GET"),
	MERCHANT_SHOP_APPLICATION_EDIT_PAGE("店铺审核-详情页面", "AUTH_MERCHANT_SHOP_APPLICATION_EDIT_PAGE", "/Merchant/Shop/Application/Edit", "GET"),
	MERCHANT_SHOP_APPLICATION_LIST("店铺审核-列表请求", "AUTH_MERCHANT_SHOP_APPLICATION_LIST", "/merchant/shop/application/list", "POST"),
	MERCHANT_SHOP_APPLICATION_LOAD("店铺审核-加载单个请求", "AUTH_MERCHANT_SHOP_APPLICATION_LOAD", "/merchant/shop/application/load", "GET"),
	MERCHANT_SHOP_APPLICATION_SAVE("店铺审核-保存请求", "AUTH_MERCHANT_SHOP_APPLICATION_SAVE", "/merchant/shop/application/save", "POST"),
	MERCHANT_SHOP_APPLICATION_DEPLOY("店铺审核-部署请求", "AUTH_MERCHANT_SHOP_APPLICATION_DEPLOY", "/merchant/shop/application/deploy", "POST"),
	MERCHANT_SHOP_APPLICATION_FILE_UPLOAD("店铺审核-文件上传请求", "AUTH_MERCHANT_SHOP_APPLICATION_FILE_UPLOAD", "/merchant/shop/application/file/upload", "POST"),
	MERCHANT_SHOP_APPLICATION_FILE_LOAD("店铺审核-文件读取请求", "AUTH_MERCHANT_SHOP_APPLICATION_FILE_LOAD", "/merchant/shop/application/file/load", "GET"),
	
	
	;
	
	private String name;
	private String code;
	private String link;
	private String method;

	private AuthorityDefinitions(String name, String code, String link, String method) {
		this.name = name;
		this.code = code;
		this.link = link;
		this.method = method;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getLink() {
		return link;
	}
	
	public String getMethod() {
		return method;
	}
	
}
