package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cmmon.pojo.EasyUIDataGridResult;
import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 分类管理
 * 
 * @author wangzhizhi
 *
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 根据parentI查询子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		// 查询出来的是数据库orm的格式,转换成EasyUITreeNode的形式
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		// 将查询出来的List遍历,然后把属性设置到EasyUITreeNode里返回到前端
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			// 添加到结果列表
			resultList.add(node);
		}
		// 转换成
		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		// 创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		// 补全对象的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// 状态 1-正常 2-删除
		contentCategory.setStatus(1);
		// 排序：默认为1
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());

		// 插入到数据库
		contentCategoryMapper.insert(contentCategory);

		// 判断父节点的状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 如果父节点为叶子节点应该改为父节点
			parent.setIsParent(true);
			// 更新父节点
			contentCategoryMapper.updateByPrimaryKey(parent);
		}

		// 返回结果
		return TaotaoResult.ok(contentCategory);
	}

	// 标题修改
	@Override
	public void updateContentCategory(Long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 重新修改标题
		contentCategory.setName(name);
		// 插入数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
	}

	@Override
	public TaotaoResult deleteContentCategory(Long id) {
		// 根据id查询
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);

		Boolean isParent = contentCategory.getIsParent();
		
		//contentCategoryMapper.deleteByPrimaryKey(id);
		
		if(isParent == false){
			contentCategoryMapper.deleteByPrimaryKey(id);
			return TaotaoResult.ok("删除成功");
		}else{
			return TaotaoResult.ok("无法直接删除父级目录");
		}
	}

}
