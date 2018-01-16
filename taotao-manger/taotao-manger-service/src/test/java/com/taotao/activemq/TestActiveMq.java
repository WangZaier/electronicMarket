package com.taotao.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMq {
	
	//queue
	@Test
	public void testQueueProducer() throws Exception{
		//1.创建一个连接工厂对象ConnectionFactory对象,需要指定mq的ip以及端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://118.31.42.117:61616");
		//2.使用ConnectionFactory来创建一个连接 Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启链接.调用Connection的start方法
		connection.start();
		//4.用Connection对象来创建一个Session对象 
		//第一个参数是是否开启事务。 一般不用事物（分布式事务一旦失败全部回滚,我们要求快速响应），保证数据的最终一致性 ，采用消息队列实现
		//如果第一个参数为true第二个参数自动忽略，如果不开启事务false ，第二个参数为消息应答的模式 ， 一般自动应答就可以了。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用 Session 对象创建一个Destination 对象，两种形式：queue和topic ，我们使用queue
		Queue queue = session.createQueue("test-queue");
		//6.创建一个生产者对象:发布消息 Producer对象  指定生产者往这个队列里发送消息
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个TextMessage对象
			//TextMessage textMessage = new ActiveMQTextMessage();
			//textMessage.setText("hello activemq");
		TextMessage textMessage = session.createTextMessage("hello activemq");
		 
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		connection.close();
		session.close();
		producer.close();
	}
	
	@Test
	public void testQueueConsumer() throws JMSException, Exception {
		//创建一个工厂对对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://118.31.42.117:61616");
		//使用工厂开启一个连接
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//通过这个链接获得session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Destination ， Destination应该和消息的发送端一致
		Queue queue = session.createQueue("test-queue");
		//使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		//向Consumer对象中设置一个messageListener对象,用来接受消息  这是一个监听会一直接受数据
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//取消息的内容
				if(message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					try {
						String text = textMessage.getText();
						//打印消息内容				
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		//手动等待
		System.in.read();
		
		//关闭资源
		connection.close();
		session.close();
		consumer.close();
	}
}
