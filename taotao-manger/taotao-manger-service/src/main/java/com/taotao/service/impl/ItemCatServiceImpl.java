package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	
	/**
	 * 这就是查询商品列表的实现
	 * 指定parentId 来查询所有的list
	 * 比如我们点击 ："图书、音像、电子书刊" 这个按钮，我们传入他的id
	 * 然后我们通过他的这个id遍历他的子节点
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {

		// 根据父节点id查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		// 设置查询条件
		Criteria createCriteria = example.createCriteria();
		// 设置parentId
		createCriteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 转换成EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			// 节点下有字节点为closed ，没有为open
			node.setState(tbItemCat.getIsParent() ? "closed" : "open");
			// 添加到节点列表
			resultList.add(node);
		}

		return resultList;
	}

}
