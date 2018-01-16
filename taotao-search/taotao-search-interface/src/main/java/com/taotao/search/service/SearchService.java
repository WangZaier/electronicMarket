package com.taotao.search.service;

import com.taotao.cmmon.pojo.SearchResult;

public interface SearchService {
	
	SearchResult search(String queryString ,int page ,int rows) throws Exception;
}
