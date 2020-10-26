package com.yhy.consumer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.yhy.consumer.constants.RabbitMqConstants;
import com.yhy.consumer.entity.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

@Component
public class ConsumerService {
    private Logger log = LoggerFactory.getLogger(ConsumerService.class);

//    @RabbitListener(queues = RabbitMqConstants.QUEUE_A)
//    @RabbitHandler
//    public void consumer(String message,Channel channel) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<Messages> messageList = objectMapper.readValue(message, new TypeReference<List<Messages>>() {
//            });
//            for (Messages m : messageList) {
//                log.info("收到的消息：{}", m);
//            }
//            channel.basicAck(0,false);
//        } catch (Exception e) {
//            log.error("异常");
//        }
//
//    }

    @RabbitListener(queues = RabbitMqConstants.QUEUE_A)
    @RabbitHandler
    public void consumer2(@Payload String message, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Messages> messageList = objectMapper.readValue(message, new TypeReference<List<Messages>>() {
            });
            for (Messages m : messageList) {
                log.info("收到的消息：{}", m);
            }
            // 确认收到消息，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息
            channel.basicAck((Long) headers.get(DELIVERY_TAG), false);
        } catch (Exception e) {

            System.out.println("消息即将再次返回队列处理！");
            // requeue为是否重新回到队列，true重新入队
            channel.basicNack((Long) headers.get(DELIVERY_TAG), false, true);
            e.printStackTrace();
        }


    }

    /**
     * 死信队列消费者
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = RabbitMqConstants.QUEUE_A_DEAD)
    @RabbitHandler
    public void deadConsumerA(String message, Channel channel) throws Exception {

        log.info("队列A的死信信息：" + message);
    }
}

