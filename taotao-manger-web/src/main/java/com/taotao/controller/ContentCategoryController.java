package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

/**
 * 内容分类管理Controller
 * @author wangzhizhi
 *
 */

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 这是查询门户网站分类的
	 * 例如：列表首页,大广告,涛涛快报
	 * @param parentId 当前节点的id为子节点的父节点id查询
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(
			@RequestParam(value="id" , defaultValue="0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
		return list;
	}
	
	/**
	 * 这是创建门户网站分类的
	 * @param parentId 传入当前父节点id,他的作用是判断这个节点是否是根节点.如果是,我们插入以后将它变为父节点
	 * @param name 只是当前节点的名字
	 * @return
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult addContentCategory(Long parentId , String name){
		TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
		
		return result;
	}
	
	/**
	 *  这是修改门户网站分类的
	 * @param id		修改名字所以要提供的节点id
	 * @param name	修改后的名字
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public void updateContentCategory(Long id,String name){
		contentCategoryService.updateContentCategory(id, name);
	}

	
	/**
	 * 这是删除门户网站分类的
	 * @param id		所需要删除的节点id.我们暂时处理不能直接删除父节点.
	 * @param name	呃这个名字好像没用到.不管他
	 * @return
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(Long id,String name){
		TaotaoResult result = contentCategoryService.deleteContentCategory(id);
		return result;
	}

}
