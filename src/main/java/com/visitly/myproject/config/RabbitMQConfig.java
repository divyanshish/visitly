package com.visitly.myproject.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_REGISTRATION_QUEUE = "user.registration.queue";
    public static final String USER_LOGIN_QUEUE = "user.login.queue";
    public static final String EVENTS_EXCHANGE = "user.events.exchange";
    public static final String REGISTRATION_ROUTING_KEY = "user.registered";
    public static final String LOGIN_ROUTING_KEY = "user.login";

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Queue registrationQueue() {
        return new Queue(USER_REGISTRATION_QUEUE, true);
    }

    @Bean
    public Queue loginQueue() {
        return new Queue(USER_LOGIN_QUEUE, true);
    }

    @Bean
    public Binding registrationBinding(Queue registrationQueue,
                                       TopicExchange eventsExchange) {
        return BindingBuilder.bind(registrationQueue)
                .to(eventsExchange)
                .with(REGISTRATION_ROUTING_KEY);
    }

    @Bean
    public Binding loginBinding(Queue loginQueue,
                                TopicExchange eventsExchange) {
        return BindingBuilder.bind(loginQueue)
                .to(eventsExchange)
                .with(LOGIN_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}