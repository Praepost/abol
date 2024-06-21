package com.abol.abol.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String REGISTER_QUEUE = "register_queue";
    public static final String FILE_QUEUE = "register_queue";
    public static final String REGISTER_EXCHANGE = "register_exchange";
    public static final String FILE_EXCHANGE = "file_exchange";

    public static final String ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue queue() {
        return  new Queue(REGISTER_QUEUE);
    }
    @Bean
    public Queue queue2() {
        return  new Queue(FILE_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(REGISTER_EXCHANGE);
    }

    @Bean
    public TopicExchange exchange2() {
        return new TopicExchange(FILE_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding binding2(Queue queue2, TopicExchange exchange2) {
        return BindingBuilder
                .bind(queue2)
                .to(exchange2)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }

}