package com.yhy.consumer.constants;




public class RabbitMqConstants {

    //交换机
    public static final String EXCHANGE_A = "my-mq-direct_exchange";
    public static final String EXCHANGE_B = "my-mq-fanout_exchange";
    public static final String EXCHANGE_C = "my-mq-exchange_C";

    //队列
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_A_FAIL = "QUEUE_A_FAIL";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_A_DEAD = "QUEUE_DEAD";

    //路由
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_A_FAIL = "spring-boot-routingKey_A_FAIL";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";


}
