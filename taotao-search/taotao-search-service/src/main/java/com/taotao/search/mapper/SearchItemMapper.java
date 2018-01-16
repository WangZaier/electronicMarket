package com.taotao.search.mapper;

import java.util.List;

import com.taotao.cmmon.pojo.SearchItem;

public interface SearchItemMapper {
	
	SearchItem getItemById(long itemId);
	
	List<SearchItem> getItemList();
}
