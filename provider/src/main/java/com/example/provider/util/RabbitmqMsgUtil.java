package com.example.provider.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

/**
 * rabbitmq中消息对象message和对象之间的转化
 */
public class RabbitmqMsgUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Message objToMsg(Object obj) throws JsonProcessingException {
        if (null == obj) {
            return null;
        }

        Message message = MessageBuilder.withBody(objectMapper.writeValueAsString(obj).getBytes()).build();
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);// 消息持久化
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);

        return message;
    }

    public static <T> T msgToObj(Message message, Class<T> clazz) throws JsonProcessingException {
        if (null == message || null == clazz) {
            return null;
        }

        String str = new String(message.getBody());
        T obj =objectMapper.readValue(str, clazz);

        return obj;
    }
}
