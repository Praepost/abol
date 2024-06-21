package com.abol.abol.app.controllers;

import com.abol.abol.app.config.MQConfig;
import com.abol.abol.app.controllers.dto.FileMessage;
import com.abol.abol.app.controllers.dto.RegisterMessage;
import com.abol.abol.app.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageListener {
    private final MessageService messageService;

    @RabbitListener(queues = MQConfig.REGISTER_QUEUE)
    public void listener(RegisterMessage message) {
        messageService.sendEmail(message.getEmail(), message.getMessage(), "Register in abol");
    }

    @RabbitListener(queues = MQConfig.FILE_EXCHANGE)
    public void listener2(FileMessage message) {
        messageService.sendEmail(message.getEmail(), "You successfully uplod file, size: "+ message.getSize()
                , "Success upload file");

    }

}