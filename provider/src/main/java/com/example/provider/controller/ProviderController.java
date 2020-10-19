package com.example.provider.controller;


import com.example.provider.config.RabbitmqConfig;
import com.example.provider.entity.Message;
import com.example.provider.service.QueueMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/provider")
public class ProviderController {
    private Logger log = LoggerFactory.getLogger(ProviderController.class);

    @Autowired
    private QueueMessageService queueMessageService;

    @PostMapping("/sendMessage")
    public void sendMessage() {

        List<Message> messageList=new ArrayList<>();
        Message testMessage1 = new Message();
        testMessage1.setId(UUID.randomUUID().toString());
        testMessage1.setName("生产者消息120");
        Message testMessage2 = new Message();
        testMessage2.setId(UUID.randomUUID().toString());
        testMessage2.setName("生产者消息121");
        Message testMessage3 = new Message();
        testMessage3.setId(UUID.randomUUID().toString());
        testMessage3.setName("生产者消息122");
        messageList.add(testMessage1);
        messageList.add(testMessage2);
        messageList.add(testMessage3);

        try{
            queueMessageService.send(messageList, RabbitmqConfig.EXCHANGE_A, RabbitmqConfig.ROUTINGKEY_A);
        } catch (Exception e) {
            log.error("消息发送失败：",e);
        }
    }


}
