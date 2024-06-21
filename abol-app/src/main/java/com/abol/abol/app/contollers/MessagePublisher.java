package com.abol.abol.app.contollers;

import com.abol.abol.app.config.MQConfig;
import com.abol.abol.app.contollers.dto.FileMessage;
import com.abol.abol.app.contollers.dto.RegisterMessage;
import com.abol.abol.app.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MessagePublisher implements IMessagePublisher{

    private final RabbitTemplate template;
    private final PersonRepository personRepository;
    @GetMapping("/register")
    public ResponseEntity publishRegisterMessage(@RequestBody RegisterMessage message) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        personRepository.findByUsername(username);

        template.convertAndSend(MQConfig.REGISTER_EXCHANGE,
                MQConfig.ROUTING_KEY, message);

        return ResponseEntity.ok("Message Published");
    }

    @GetMapping("/file")
    public ResponseEntity publishFileMessage(@RequestBody FileMessage message) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        personRepository.findByUsername(username);

        template.convertAndSend(MQConfig.FILE_EXCHANGE,
                MQConfig.ROUTING_KEY, message);

        return ResponseEntity.ok("Message Published");
    }
}