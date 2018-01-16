package com.taotao.freemark.test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class TestFreeMarker {
	
	
	@Test
	public void testFreemarker() throws Exception{
	//1.创建一个模版文件
	//2.创建一个Configuration
	Configuration configuration = new Configuration(Configuration.getVersion());
	//3.设置模版所在的路径
	configuration.setDirectoryForTemplateLoading(new File("/Users/wangzhizhi/Documents/WorkSpaces/taotao-item-web/src/main/webapp/WEB-INF/ftl")); 	
	//4.设置模版的字符集，一般utf-8
	configuration.setDefaultEncoding("utf-8");
	//5.使用Configuration对象加载一个模版文件,需要指定模版文件的文件名
	Template template = configuration.getTemplate("hello.ftl");
	//6.	创建一个数据集，可以是POJO，也可以是Map(推荐)
	Map data = new HashMap<>();
	data.put("hello", "hello freemark");
	//7.创建一个Writer对象,指定输出文件的路径以及文件名
	Writer out = new FileWriter(new File("/Users/wangzhizhi/Desktop/staticHTML/hello.txt"));
	//8.使用模版对象的process来输出文件
	template.process(data, out);
	//9.关闭流
	out.close();
	}	
	
	
	@Test
	public void freemarkerListTest() throws Exception{
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(new File("/Users/wangzhizhi/Documents/WorkSpaces/taotao-item-web/src/main/webapp/WEB-INF/ftl")); 
		Template template = configuration.getTemplate("people.ftl");
		List<People> peoList = new ArrayList<>();
		peoList.add(new People("王仔",17,"江苏"));
		peoList.add(new People("王仔",17,"江苏"));
		peoList.add(new People("王仔",17,"江苏"));
		peoList.add(new People("王仔",17,"江苏"));
		peoList.add(new People("王仔",17,"江苏"));
		Map data = new HashMap<>();
		data.put("peopleList",peoList);
		data.put("people", new People("王仔1号",17,"江苏"));
		data.put("hello", "hello freemark");
		data.put("date", new Date());
		Writer out = new FileWriter(new File("/Users/wangzhizhi/Desktop/staticHTML/people.html"));
		template.process(data,out);
		out.close();
	}
	
	@Test
	public void Stringcopy() {
		System.loadLibrary("");
		String myString = "faq";
		char cbuf[];
		cbuf = new char[1024];
		myString.getChars(0, 0+myString.length(), cbuf, 0);
		System.out.println(cbuf);
	}

}

