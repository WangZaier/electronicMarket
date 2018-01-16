package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.cmmon.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * 监听商品添加事件 ->同步索引库
 * 这里我们載spring中配置过,监听会一直保存在容器中
 * @author wangzhizhi
 *
 */
public class ItemAddMessageListener implements MessageListener{
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 将商品添加到索引库中
	 */
	
	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getText();
			long itemId = Long.parseLong(text);
			//根据商品id查询数据库,取出商品信息
			Thread.sleep(1000); //等数据插入,等他一秒
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			//像文件中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_asmdesc", searchItem.getItem_desc());
			//把文档写入索引库
			solrServer.add(document);
			//提交
			System.out.println("activeMq队列开始提交");
			solrServer.commit();
			System.out.println("提交完成");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
