package com.yhy.consumer.service;

import com.yhy.consumer.constants.RabbitMqConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {
    private Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @RabbitListener(queues = RabbitMqConstants.QUEUE_A)
    @RabbitHandler
    public void consumer(String message){
          log.info("收到的消息：{}",message);
    }
}
