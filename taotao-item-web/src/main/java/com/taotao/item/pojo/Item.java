package com.taotao.item.pojo;

import com.taotao.pojo.TbItem;

/**
 * 用来扩展Item的 , 为他新添加 getImages 方法 ，方便el表达式取值
 * 
 * @author wangzhizhi
 *
 */
public class Item extends TbItem {

	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());

	}

	public String[] getImages() {
		if (this.getImage() != null && !"".equals(this.getImage())) {
			String images = this.getImage();
			String[] strings = images.split(",");
			return strings;
		}
		return new String[]{"error:500","图片url为空"};
	}

}
