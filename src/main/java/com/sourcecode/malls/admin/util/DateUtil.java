package com.sourcecode.malls.admin.util;

import java.sql.Date;

public final class DateUtil {
	public static Date now() {
		return new Date(System.currentTimeMillis());
	}
}
