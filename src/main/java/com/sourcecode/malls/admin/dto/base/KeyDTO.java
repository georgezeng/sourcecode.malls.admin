package com.sourcecode.malls.admin.dto.base;

import java.io.Serializable;
import java.util.List;

public class KeyDTO<K extends Serializable> {
	List<K> ids;

	public List<K> getIds() {
		return ids;
	}

	public void setIds(List<K> ids) {
		this.ids = ids;
	}
}
