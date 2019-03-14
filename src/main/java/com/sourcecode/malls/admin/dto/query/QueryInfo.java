package com.sourcecode.malls.admin.dto.query;

public class QueryInfo<T> {
	private T data;
	private PageInfo page;

	public QueryInfo() {
	}

	public QueryInfo(T data, PageInfo page) {
		this.data = data;
		this.page = page;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PageInfo getPage() {
		return page;
	}

	public void setPage(PageInfo page) {
		this.page = page;
	}
}