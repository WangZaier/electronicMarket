package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;

/**
 * 商品分类管理Controller
 * @author wangzhizhi
 *
 */
@Controller
public class ItemCatController{
	
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 商品查询Controller
	 * 这是 通过一个parentid 来查询子节点
	 * 比如父节点的 id 对于子节点来说就是他的 parentId
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id" , defaultValue = "0")long parentId){
		
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		
		return list;
		
	}
}
