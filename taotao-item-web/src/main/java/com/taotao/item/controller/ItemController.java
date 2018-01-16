package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

/**
 * 商品详情页面的展示
 * @author wangzhizhi
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 把商品的数据添加入页面返回逻辑试图
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId , Model model) {
		//取商品基本信息并封装
		TbItem tbItem = itemService.getItemId(itemId);
		Item item = new Item(tbItem);
		System.out.println("item:" + item);
		System.out.println("itemImages" + item.getImages());
		//取商品详情
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		//添加页面数据
		model.addAttribute("item", item);
		model.addAttribute("itemDesc" , itemDesc);
		//返回逻辑试图
		return "item";
		
	}
	
}



