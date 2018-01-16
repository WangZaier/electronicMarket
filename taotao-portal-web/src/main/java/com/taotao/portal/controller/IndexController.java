package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;

/**
 * 首页展示Controller
 * 
 * @author wangzhizhi
 *
 */

@Controller
public class IndexController {

	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;

	@Autowired
	private ContentService contentService;

	/**
	 * 查询大广告的数据,将数据适配node
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex(Model model) {
		// 根据cid查询轮播图内容列表
		List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
		// 吧列表转换成AD1Node
		List<AD1Node> ad1Nodes = new ArrayList<>();
		for (TbContent tbContent : contentList) {
			AD1Node node = new AD1Node();
			node.setAlt(tbContent.getTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setHref(tbContent.getUrl());

			ad1Nodes.add(node);
		}
		// 再转换成json数据
		String ad1Json = JsonUtils.objectToJson(ad1Nodes);
		// 将json数据传送到页面
		model.addAttribute("ad1", ad1Json);
		return "index";
	}
}
