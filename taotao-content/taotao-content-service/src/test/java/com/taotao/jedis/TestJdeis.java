package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;



public class TestJdeis {
	
	
	@Test
	public void testJedis() throws Exception{
		//创建一个Jedis对象，创建服务所需要的ip和端口号
		Jedis jedis = new Jedis("118.31.42.117", 7001);
		//直接操作数据库
		jedis.set("jedis-key", "1234");
		String result = jedis.get("jedis-key");
		jedis.set("i love you", "666");
		String result1 = jedis.get("i love you");
		System.out.println(result + result1);
		//关闭Jedis
		jedis.close();
	}
	
	@Test
	public void testJedisPool() throws Exception{
		//创建一个数据库连接池对象（单例） ， 需要指定服务ip和端口
		JedisPool jedisPool = new JedisPool("118.31.42.117", 6379);
		//从连接池中获得链接
		Jedis jedis = jedisPool.getResource();
		//操作竹居裤，方法级别使用
		String result = jedis.get("jedis-key");
		System.out.println(result);
		//一定要关闭jedis连接
		jedis.close();
		//系统关闭之前关闭连接池
		jedisPool.close();
	}
	
	
	//连接集群
	@Test
	public void testJedisCluster() throws Exception{
		//创建一个JedisCluster对象，构造参数Set类型，集合中每个元素是HostAndPort类型
		Set<HostAndPort> nodes = new HashSet<>();
		//添加节点
		nodes.add(new HostAndPort("118.31.42.117", 7001));
		nodes.add(new HostAndPort("118.31.42.117", 7002));
		nodes.add(new HostAndPort("118.31.42.117", 7003));
		nodes.add(new HostAndPort("118.31.42.117", 7004));
		nodes.add(new HostAndPort("118.31.42.117", 7005));
		nodes.add(new HostAndPort("118.31.42.117", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		jedisCluster.set("cluster","hello-Cluster");
		System.out.println("success");
		String result = jedisCluster.get("cluster");
		System.out.println(result);
		//关闭
		jedisCluster.close();
	}
}
