package com.yhy.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhy.consumer.constants.RabbitMqConstants;
import com.yhy.consumer.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsumerService {
    private Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @RabbitListener(queues = RabbitMqConstants.QUEUE_A)
    @RabbitHandler
    public void consumer(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Message> messageList = objectMapper.readValue(message, new TypeReference<List<Message>>() {
        });
        for (Message m : messageList) {
            log.info("收到的消息：{}", m);
        }
    }
}
