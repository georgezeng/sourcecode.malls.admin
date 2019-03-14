package com.sourcecode.malls.admin.dto.base;

import java.util.Arrays;
import java.util.List;

public class ResultBean<T> {
	private static final int CODE_SUCCESS = 0;
	private static final int CODE_FAILED = 1;

	private List<String> msgs;

	private String traceId;

	private T data;

	private List<T> datas;

	private int code = CODE_SUCCESS;

	public ResultBean() {
	}

	public ResultBean(T data) {
		this.data = data;
	}

	public ResultBean(List<T> datas) {
		this.datas = datas;
	}

	public ResultBean(String traceId, List<String> msgs) {
		this.msgs = msgs;
		this.traceId = traceId;
		this.code = CODE_FAILED;
	}

	public ResultBean(String traceId, String msg) {
		this.msgs = Arrays.asList(msg);
		this.traceId = traceId;
		this.code = CODE_FAILED;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<String> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
}