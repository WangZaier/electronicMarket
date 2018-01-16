package com.taotao.activemq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class springActivemq {

	@Test
	public void testJmsTemple() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		// 从context用类型的模式获取到Jms模版
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		// 获取我们Destination,这里是test-queue,就是Queue类型，这里我们强转成
		// Destination，而不转成queue/topic是为了他的通用型,这也是接口编程的好处
		Destination destination = (Destination) context.getBean("test-queue");
		// 然后调用send方法,spring整合过的,需要指定Destination ， 然后一个内部类,MessageCreator
		// ,他就靠他下面那个重写的CreateMessage的Message对象
		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage("spring activemq send message");
				return message;
			}
		});

	}
}
