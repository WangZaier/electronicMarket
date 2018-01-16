package com.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cmmon.pojo.EasyUIDataGridResult;
import com.taotao.cmmon.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品管理controller
 * 
 * @author wangzhizhi
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	
	/**
	 *  后台管理系统商品单条记录查询
	 *  虽然这个Controller我们好像没有用到,但是getItemId这个service我们还是用到了
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemId(itemId);
		return tbItem;
	}

	
	/**
	 * 查询后台页面的列表
	 * @param page  查询的页数
	 * @param rows	想要查询一页有多少行
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 后台管理系统添加商品数据
	 * @param item	这里前端标签的name和pojo对应,自动注入
	 * @param desc	这是商品描述信息,这条是要写到tb_item_desc表中的,因为tb_item_desc表的主键作为item表外建,所以只需要这条数据就够了
	 * @return
	 */
	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem item, String desc) {
		TaotaoResult result = itemService.addItem(item, desc);
		return result;
	}

	
	/**
	 * 后台管理系统删除商品数据
	 * @param ids 需要删除的数据的id , 可能是多条,也可能是一条,这里做了分类调用
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public void deleteItem(String ids){
		if(ids.contains(",")){
			//创建一个List集合用来传送id的集合 这里使用long的原因是逆向工程生成的pojo和接口方参数都是Long类型的
			List<Long> del_ids = new ArrayList<>();
			//将id各个值取出来存放在 String[]中
			String[] str_ids = ids.split(",");
			//遍历 str_ids 然后把id 存在到 list 中 用作 Service 的参数 
			for(String stirng : str_ids){
					del_ids.add(Long.parseLong(stirng));
			}
			itemService.deleteItem(del_ids);
		}else{
			//调用单个删除 删除记录
			Long id = Long.parseLong(ids);
			itemService.deleteOnceItem(id);
		}
	}
	
}
