package com.example.provider.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public interface QueueMessageService extends RabbitTemplate.ConfirmCallback {

    /**
     * Direct Exchange（直连交换机）
     *
     * @param message         消息内容
     * @param exchange        交换机配置
     * @param queueRoutingKey routingKey的队列
     * @throws Exception
     */
    void send(Object message, String exchange, String queueRoutingKey) throws Exception;


    /**
     * Fanout Exchange(广播式交换机),使用时RoutingKey传空就行
     *
     * @param message
     * @param exchange
     */
    void sendToFanoutExchange(Object message, String exchange);

}
