package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

/**
 * 索引库维护Controller
 * 数据库上传solr
 * @author wangzhizhi
 *
 */
@Controller
public class IndexControllerManger {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult ImportIndex() {
		TaotaoResult result = searchItemService.importItemsToIndex();
		return result;
	}
}
