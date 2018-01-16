package com.taotao.solrj;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.cmmon.pojo.SearchItem;
import com.taotao.cmmon.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.mapper.SearchItemMapper;

public class TestSolrJ {
	
	@Test
	public void test() throws Exception{
		
//		ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
//		
//		SearchItemMapper searchItemMapper = application.getBean(SearchItemMapper.class);
//		
//		List<SearchItem> itemList = searchItemMapper.getItemList();
//		
//		for ( int i = 0 ; i < 1 ; i ++) {
//			System.out.println(itemList.get(i) + "\n");
//		} 
		
		//创建一个SolrServer对象
		//需要指定solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://118.31.42.117:8080/solr/collection1");
		
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//像文档中添加域，必须有id域，域的名称必须在schema.xml中定义
		document.addField("id", "test001");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", 1000);
		
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
		System.out.println("ok");
	}
	
	@Test
	public void searchDocument() throws Exception{
		//创建solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://118.31.42.117:8080/solr/collection1");
		//创建一个solrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件,过滤条件,分页条件,排序条件,高亮
		//solrQuery.set("q","*:*");
		solrQuery.setQuery("手机");
		//分页条件
		solrQuery.setStart(0);//开始页数
		solrQuery.setRows(10);//条数
		//搜索域 [搜索页面只需要item_keywords]
		solrQuery.set("df", "item_keywords");
		//高亮
		solrQuery.setHighlight(true);
		//高亮显示的域	
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");//前缀
		solrQuery.setHighlightSimplePost("</em>");//后缀
		//执行查询，得到一个response对象 
		QueryResponse response = solrServer.query(solrQuery);
		//取出查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		//取查询结果总记录数
		System.out.println("查询结果总记录数:" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示 先取id 后取item_title 我们的条件是手机 我们只需要在title中添加高亮
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			//这里因为我们一条一条遍历所以取出来的List只有一条
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			System.out.println(highlighting.get(solrDocument.get("id")));
			//显示的高亮
			String itemTitle = ""; 
			if(list != null && list.size() > 0 ) {
				//只有一条List，取0
				itemTitle = list.get(0);
			}else {
				//有可能list是空的,不显示空串,查找
				itemTitle = (String) solrDocument.get("item_title");
			}
			
//			System.out.println(itemTitle);
//			System.out.println(solrDocument.get("item_sell_point"));
//			System.out.println(solrDocument.get("item_price"));
//			System.out.println(solrDocument.get("item_image"));
//			System.out.println(solrDocument.get("item_category_name"));
//			System.out.println("==============================");
		}
		
		
	}

	@Test
	public void tt24t() throws Exception {
		ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-solr.xml");
		
		SearchDao search = application.getBean(SearchDao.class);
		
		
		//创建一个solrQuery对象
				SolrQuery solrQuery = new SolrQuery();
				//设置查询条件,过滤条件,分页条件,排序条件,高亮
				//solrQuery.set("q","*:*");
				solrQuery.setQuery("手机");
				//分页条件
				solrQuery.setStart(0);//开始页数
				solrQuery.setRows(10);//条数
				//搜索域 [搜索页面只需要item_keywords]
				solrQuery.set("df", "item_keywords");
				//高亮
				solrQuery.setHighlight(true);
				//高亮显示的域	
				solrQuery.addHighlightField("item_title");
				solrQuery.setHighlightSimplePre("<em>");//前缀
				solrQuery.setHighlightSimplePost("</em>");//后缀
		
		
		SearchResult result = search.search(solrQuery);
		List<SearchItem> itemList = result.getItemList();
		for (SearchItem searchItem : itemList) {
			System.out.println(itemList);
		}
	}

	
	
}
