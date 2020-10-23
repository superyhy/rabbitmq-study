package com.example.provider.service.impl;

import com.example.provider.service.QueueMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {

    private Logger log = LoggerFactory.getLogger(QueueMessageServiceImpl.class);

    //由于配置类中rabbitmq的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE
    private RabbitTemplate rabbitTemplate;

    //设置回调为当前类对象
    @Autowired
    public QueueMessageServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //指定 ConfirmCallback
        rabbitTemplate.setConfirmCallback(this);
        //指定 ReturnCallback
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void send(Object message, String exchange, String queueRoutingKey) throws Exception {
        //构建回调id为uuid
        String callBackId = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(callBackId);
        log.info("开始发送消息：{}", message.toString());
        //发送消息到消息队列
        rabbitTemplate.convertAndSend(exchange, queueRoutingKey, message, correlationId);


        log.info("发送定制的回调ID:{}", callBackId);

    }

    @Override
    public void sendToFanoutExchange(Object message, String exchange) {
        //构建回调id为uuid，为邮件添加唯一标识
        String callBackId = UUID.randomUUID().toString();
        CorrelationData correlationId = new CorrelationData(callBackId);
        log.info("开始发送消息：{}", message.toString());
        //发送消息到消息队列
        rabbitTemplate.convertAndSend(exchange, "", message, correlationId);

        log.info("发送定制的回调ID:{}", callBackId);
    }

    /**
     * 确认消息到达交换机
     *
     * @param correlationData 请求数据对象
     * @param ack             是否发送成功
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        log.info("回调id:{}", correlationData.getId());
        if (ack) {
            log.info("消息发送成功");
        } else {
            log.info("消息发送失败:{}", s);
        }

    }

    /**
     * 确认消息从交换机到达队列
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息从交换机到队列失败");
    }
}
