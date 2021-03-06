package com.example.provider.config;


import com.example.provider.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


    //创建一个Rabbitmq的连接容器
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    //RabbitMq的使用入口
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
        template.setMessageConverter(this.jsonMessageConverter());
        template.setMandatory(true);
        return template;
    }

    //消息转JSON字符串
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    //把交换机，队列，通过路由关键字进行绑定，写在RabbitConfig类当中

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(RabbitMqConstants.DEAD_LETTER_EXCHANGE);
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange(RabbitMqConstants.EXCHANGE_A);
    }



    /**
     * 获取队列A
     * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
     * exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
     * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
     * return new Queue("TestDirectQueue",true,true,false);
     * 一般设置一下队列的持久化就好,其余两个就是默认false
     *
     * @return
     */
    @Bean
    public Queue queueA() {
        Map<String, Object> args = new HashMap<>();
        // 当前队列绑定到死信交换机
        args.put("x-dead-letter-exchange", RabbitMqConstants.DEAD_LETTER_EXCHANGE);
        // 当前队列的死信路由
        args.put("x-dead-letter-routing-key", RabbitMqConstants.DEAD_LETTER_ROUTINGKEY);
        // 当前队列的消费失败时间
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(RabbitMqConstants.QUEUE_A).withArguments(args).build();

    }

    @Bean
    public Queue queueADead() {
        return new Queue(RabbitMqConstants.QUEUE_A_DEAD, true);//队列持久
    }

    //将队列和交换机绑定, 并设置用于匹配键：spring-boot-routingKey_A
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(testDirectExchange()).with(RabbitMqConstants.ROUTINGKEY_A);
    }

    //死信队列A，绑定死信交换机
    @Bean
    public Binding bindingADead() {
        return BindingBuilder.bind(queueADead()).to(deadLetterExchange()).with(RabbitMqConstants.DEAD_LETTER_ROUTINGKEY);
    }

    /**
     * Fanout Exchange广播式交换机使用
     *
     * @return
     */
    @Bean
    public FanoutExchange testFanoutExchange() {
        return new FanoutExchange(RabbitMqConstants.EXCHANGE_B);
    }


    @Bean
    public Queue queueB() {
        //队列持久
        return new Queue(RabbitMqConstants.QUEUE_B, true);
    }

    @Bean
    public Queue queueC() {
        //队列持久
        return new Queue(RabbitMqConstants.QUEUE_C, true);
    }


    @Bean
    public Binding bindFanoutExchangeB() {
        return BindingBuilder.bind(queueB()).to(testFanoutExchange());
    }

    @Bean
    public Binding bindFanoutExchangeC() {
        return BindingBuilder.bind(queueC()).to(testFanoutExchange());
    }


}
