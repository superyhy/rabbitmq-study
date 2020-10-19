package com.example.provider.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public interface QueueMessageService extends RabbitTemplate.ConfirmCallback {

    /**
     *
     * @param message 消息内容
     * @param exchange 交换机配置
     * @param queueRoutingKey  routingKey的队列
     * @throws Exception
     */
    void send(Object message,String exchange,String queueRoutingKey) throws Exception;

}
