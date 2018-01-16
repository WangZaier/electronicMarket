package com.taotao.content.service;

import java.util.List;

import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.cmmon.pojo.TaotaoResult;

public interface ContentCategoryService {
	
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	TaotaoResult addContentCategory(Long parentId , String name);
	
	void updateContentCategory(Long id , String name);
	
	TaotaoResult deleteContentCategory(Long id);
}
