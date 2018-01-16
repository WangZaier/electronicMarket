package com.taotao.service;

import java.util.List;

import com.taotao.cmmon.pojo.EasyUITreeNode;

public interface ItemCatService {
	
	List<EasyUITreeNode> getItemCatList(long parentId);
}
