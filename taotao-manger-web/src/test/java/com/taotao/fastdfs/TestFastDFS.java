package com.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class TestFastDFS {
	
	@Test
	public void uploadFile() throws Exception{
		
		//1.向工程中添加jar包
		//2.创建一个配置文件，配置tracker服务器地址
		//3.加载配置文件
		ClientGlobal.init("/Users/wangzhizhi/Documents/WorkSpaces/taotao-manger-web/src/main/resources/resource/client.conf");
		//4.创建一个trackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//5.使用TrackerClient对象获得trackerserver对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//6.创建一个StorageServer的引用null就可以
		StorageServer storageServer = null;
			
		//7.创建一个StorageClient对象。trackerserver，StorageServer两个参数
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//8.使用StorageClient对象上传对象 
		String[] strings = storageClient.upload_file("/Users/wangzhizhi/Desktop/景色1.jpg", "jpg", null);
		for(String stirng:strings){
			System.out.println(stirng);
		}
	}


}
