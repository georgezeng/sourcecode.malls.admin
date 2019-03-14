package com.sourcecode.malls.admin.util;

import java.util.Random;

import org.slf4j.MDC;

public class LogUtil {
	public static String getTraceId() {
		String traceId = MDC.get("X-B3-TraceId");
		if (traceId == null) {
			traceId = new Random().nextInt(999999) + "";
		}
		return traceId;
	}
}
