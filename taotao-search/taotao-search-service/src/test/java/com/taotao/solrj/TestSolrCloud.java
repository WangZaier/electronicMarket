package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {

	@Test
	public void testSolrCloudAdDocument() throws Exception {
		// 指定zookeeper集群地址
		CloudSolrServer cloudSolrServer = new CloudSolrServer("10.211.55.24:2181,10.211.55.24:2182,10.211.55.24:2183");

		// 设置默认collection
		cloudSolrServer.setDefaultCollection("collection2");
		// 创建文档
		SolrInputDocument document = new SolrInputDocument();

		// 将数据写入document
		document.addField("id", "test01");
		document.addField("item_title", "测试商品");
		document.addField("item_price", "100");
		// 写入索引库
		cloudSolrServer.add(document);
		cloudSolrServer.commit();
		System.out.println("success");
	}
}
