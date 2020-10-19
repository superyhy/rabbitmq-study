package com.example.provider.service.impl;

import com.example.provider.service.QueueMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {

    private Logger log = LoggerFactory.getLogger(QueueMessageServiceImpl.class);

    //由于配置类中rabbitmq的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE
    private RabbitTemplate rabbitTemplate;

    //设置回调为当前类对象
    @Autowired
    public QueueMessageServiceImpl(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        //设置回调为当前类对象
        rabbitTemplate.setConfirmCallback(this);
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

    /**
     * 消息回调确认方法
     *
     * @param correlationData 请求数据对象
     * @param ack  是否发送成功
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        log.info("回调id:{}",correlationData.getId());
        if (ack){
            log.info("消息发送成功");
        }else {
            log.info("消息发送失败:{}",s);
        }

    }
}
