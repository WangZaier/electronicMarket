package com.taotao.jedis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestRedisSpring {
	
	@Test
	public void testJedisClientPool() throws Exception{
		
		//初始化spring容器
		System.out.println("开始初始化容器");
		ApplicationContext application = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		System.out.println("初始化容器完成");
		//从容器中获取JedisClient对象
		JedisClient bean = application.getBean(JedisClient.class);
		//操作redis
		  
		bean.set("jedis", "test");
		String result = bean.get("jedis");
		System.out.println(result);
	}
}
