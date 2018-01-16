package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 搜索服务SearchController
 * 
 * @author wangzhizhi
 *
 */

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	//每一页现实多少条数据
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	
	/**
	 * 查询页面,查询商品信息的方法，主要用于向页面添加数据
	 * @param queryString	查询条件
	 * @param page	查询的页数
	 * @param model	向页面添加视图
	 * @return
	 */
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, 
			@RequestParam(defaultValue = "1") Integer page, Model model) {
		try {
			//转码
			queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
			SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", searchResult.getTotalPages());
			model.addAttribute("itemList", searchResult.getItemList());
			model.addAttribute("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回逻辑视图
		return "search";
	}

}
