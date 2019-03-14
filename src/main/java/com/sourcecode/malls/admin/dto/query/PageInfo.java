package com.sourcecode.malls.admin.dto.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

public class PageInfo {
	private int num;
	private int size;
	private String property;
	private Direction order;

	public Pageable pageable() {
		if (property == null) {
			return PageRequest.of(num - 1, size);
		}
		return pageable(order, property);
	}

	public Pageable pageable(Direction direction, String... properties) {
		return PageRequest.of(num - 1, size, direction, properties);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Direction getOrder() {
		return order;
	}

	public void setOrder(Direction order) {
		this.order = order;
	}
}