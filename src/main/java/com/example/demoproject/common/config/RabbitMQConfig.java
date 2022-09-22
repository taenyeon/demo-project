//package com.example.demoproject.common.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableRabbit
//@RequiredArgsConstructor
//public class RabbitMQConfig {
//    private static final String CHAT_QUEUE_NAME = "chat.queue";
//    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
//    private static final String ROUTING_KEY = "room.*";
//    @Value("${spring.rabbitmq.host}")
//    private final String host;
//    @Value("${spring.rabbitmq.username}")
//    private final String userName;
//    @Value("${spring.rabbitmq.password}")
//    private final String password;
//    private final ObjectMapper objectMapper;
//
//    @Bean
//    public Queue queue() {
//        return new Queue(CHAT_QUEUE_NAME, true);
//    }
//
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(CHAT_EXCHANGE_NAME);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange topicExchange) {
//        return BindingBuilder
//                .bind(queue)
//                .to(topicExchange)
//                .with(ROUTING_KEY);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
//        rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer container(){
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames(CHAT_QUEUE_NAME);
//        container.setMessageListener(null);
//        return container;
//    }
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost(host);
//        factory.setUsername(userName);
//        factory.setPassword(password);
//        return factory;
//    }
//}
