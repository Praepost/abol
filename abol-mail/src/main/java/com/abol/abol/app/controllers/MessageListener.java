package com.abol.abol.app.controllers;

import com.abol.abol.app.config.MQConfig;
import com.abol.abol.app.controllers.dto.CustomMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {
        System.out.println(message);
    }

}