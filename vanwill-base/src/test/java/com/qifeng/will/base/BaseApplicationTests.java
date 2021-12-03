package com.qifeng.will.base;

import com.qifeng.will.base.rabbitconsumer.RabbitSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseApplicationTests {

	@Autowired
	private RabbitSender rabbitSender;

	/*@Autowired
    private MultiTopicProducer multiTopicProducer;*/

	@Test
	public void topicProducerTest() {
		rabbitSender.send1();
		rabbitSender.send2();
		//  multiTopicProducer.sendMessageByTopic();
	}

	@Test
	public void contextLoads() {
	}

}
