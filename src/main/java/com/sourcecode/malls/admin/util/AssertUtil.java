package com.sourcecode.malls.admin.util;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.sourcecode.malls.admin.exception.BusinessException;

public class AssertUtil extends Assert {
	public static void assertTrue(boolean isTrue, String msg) {
		if (!isTrue) {
			throw new BusinessException(msg);
		}
	}

	public static void assertNotNull(Object value, String msg) {
		if (value == null) {
			throw new BusinessException(msg);
		}
	}

	public static void assertNotEmpty(String value, String msg) {
		if (StringUtils.isEmpty(value)) {
			throw new BusinessException(msg);
		}
	}

	public static void assertIsNull(Object value, String msg) {
		if (value != null) {
			throw new BusinessException(msg);
		}
	}
}
