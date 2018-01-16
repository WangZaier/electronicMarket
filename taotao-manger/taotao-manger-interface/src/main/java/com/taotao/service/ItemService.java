package com.taotao.service;

import java.util.List;

import com.taotao.cmmon.pojo.EasyUIDataGridResult;
import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	
	TbItem getItemId(long itemId);
	
	EasyUIDataGridResult getItemList(int page , int rows);
	
	TaotaoResult addItem(TbItem item , String desc);

	void deleteItem(List<Long> ids);

	void deleteOnceItem(Long id);
	
	TbItemDesc getItemDescById(long itemId);
}
