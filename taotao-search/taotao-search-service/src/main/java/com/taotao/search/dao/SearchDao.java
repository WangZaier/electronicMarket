package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.cmmon.pojo.SearchItem;
import com.taotao.cmmon.pojo.SearchResult;

/**
 * 查询索引商品库
 * 
 * @author Administrator
 *
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	/**
	 * 负责向solr查询数据，并添加高亮 , 再拼装成我们所以要的结果,比如设置了高亮以后的数据
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		SearchResult result = new SearchResult();
		// 根据query查询结果
		QueryResponse response = solrServer.query(query);
		// 取查询结果
		SolrDocumentList solrDocumentList = response.getResults();

		// 结果总记录数目
		long numFound = solrDocumentList.getNumFound();
		result.setRecordCount(numFound);
		// 把结果封装到SearchItem中
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			// 创建SearchItem
			SearchItem item = new SearchItem();
			// 给SearchItem添加属性
			item.setId((String) solrDocument.get("id"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			// 取一张图片,因为我们查询出来是一个字符串,每个图片逗号隔开
			String image = (String) solrDocument.get("item_image");
			if (StringUtils.isNotBlank(image)) {
				image = image.split(",")[0];
			}
			item.setImage(image);
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			
			// 取高亮显示
			String title = "";
			// 添加所有的高亮.但我们只需要title的高亮。所以我们只要item_title的高亮取出来
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if (list != null && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			itemList.add(item);
		}
		result.setItemList(itemList);

		return result;
	}
}
