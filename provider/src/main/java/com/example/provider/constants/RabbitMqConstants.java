package com.example.provider.constants;




public class RabbitMqConstants {

    //交换机
    public static final String EXCHANGE_A = "my-mq-direct_exchange";
    public static final String EXCHANGE_B = "my-mq-fanout_exchange";
    public static final String EXCHANGE_C = "my-mq-exchange_C";
    //死信交换机
    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";

    //队列
    public static final String QUEUE_A = "QUEUE_A";
    public static final String QUEUE_A_FAIL = "QUEUE_A_FAIL";
    public static final String QUEUE_B = "QUEUE_B";
    public static final String QUEUE_C = "QUEUE_C";
    public static final String QUEUE_A_DEAD = "QUEUE_DEAD"; //死信队列

    //路由
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";
    public static final String ROUTINGKEY_A_FAIL = "spring-boot-routingKey_A_FAIL";
    public static final String ROUTINGKEY_B = "spring-boot-routingKey_B";
    public static final String DEAD_LETTER_ROUTINGKEY = "dead-letter-routingKey"; //死信


}
