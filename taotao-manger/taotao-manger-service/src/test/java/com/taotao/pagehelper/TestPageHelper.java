package com.taotao.pagehelper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cmmon.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemCatExample.Criteria;

public class TestPageHelper {
	
	public static void main(String[] args){
		
		//PageHelper.startPage(1, 10);
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		
		TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
		
		TbItemCatMapper itemCatMapper = applicationContext.getBean(TbItemCatMapper.class);
		
//		//创建Example对象
//		TbItemExample example = new TbItemExample();
//		//Criteria createCriteria = example.createCriteria();
//		List<TbItem> list = itemMapper.selectByExample(example);
//		
//		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
//		
//		System.out.println("查询总记录数" + pageInfo.getTotal() );
//		System.out.println("总记录数" + pageInfo.getPages());
//		System.out.println("返回的记录数" + list.size());
		
		// 根据父节点id查询子节点列表
				TbItemCatExample example = new TbItemCatExample();
				// 设置查询条件
				Criteria createCriteria = example.createCriteria();
				// 设置parentId
				createCriteria.andParentIdEqualTo((long) 11);
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
					System.out.println(resultList);
					System.out.println("====");
				}
		System.out.println(resultList);
		
	}
}
