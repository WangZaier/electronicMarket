package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import sun.misc.Request;

@Controller
public class CartController {
	@Value("${CART_KEY")
	private String CART_KEY;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	@Autowired
	private ItemService itemService;

	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		
		//取购物车商品列表
		List<TbItem> carItemList = getCartItemList(request);
		//判断商品在购物车中是否存在
		boolean flag = false;
		for (TbItem tbItem : carItemList) {
			if(tbItem.getId() == itemId.longValue()) {
				//如果存在数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				//跳出循环,没必要继续循环了，num已经加上去了
				break;
			}
		}		
		//如果不存在，添加一个新的商品
		if(!flag) {
			TbItem tbItem = itemService.getItemId(itemId);
			
			//设置购物车下单个数
			tbItem.setNum(num);
			
			//设置只显示一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
				
			}
			//添加到购物车
			carItemList.add(tbItem);
		}
		//写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(carItemList) ,true);
		//需要调用服务取商品信息
		
		return "cartSuccess";

	}

	/**
	 * 从Cookie中取出所有的TTCART（按照key取出所有的cookie）
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartItemList(HttpServletRequest request){
		//从cookie中取购物车商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}

	@RequestMapping("cart/cart")
	public String showCartList(HttpServletRequest request){
		//从cookie中取出所有的商品信息
        List<TbItem> list = getCartItemList(request);
        //将商品信息放入到request中
        request.setAttribute("cartlist" , list);
		//返回逻辑试图
        return "cart";

	}

}
