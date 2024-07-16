package com.connectpublications.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NEW_PUBLICATIONS_QUEUE = "новые_публикации";
    public static final String SUBSCRIBER_NOTIFICATIONS_QUEUE = "уведомления_подписчикам";
    public static final String ACTIVITY_NOTIFICATIONS_QUEUE = "уведомления_владельцу";
    public static final String DIRECT_EXCHANGE_NAME = "direct-exchange";
    public static final String DIRECT_EXCHANGE_PUBLISHER = "publisher-exchange";
    public static final String DIRECT_EXCHANGE_OWNER = "owner-exchange";

    @Bean
    public Queue newPublicationsQueue() {
        return new Queue(NEW_PUBLICATIONS_QUEUE, true);
    }

    @Bean
    public Queue subscriberNotificationsQueue() {
        return new Queue(SUBSCRIBER_NOTIFICATIONS_QUEUE, true);
    }

    @Bean
    public Queue activityNotificationsQueue() {
        return new Queue(ACTIVITY_NOTIFICATIONS_QUEUE, true);
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(DIRECT_EXCHANGE_NAME);
        return rabbitTemplate;
    }

    @Bean RabbitTemplate rabbitTemplatePublish(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(DIRECT_EXCHANGE_PUBLISHER);
        return rabbitTemplate;
    }

    @Bean RabbitTemplate rabbitTemplateOwner(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(DIRECT_EXCHANGE_OWNER);
        return rabbitTemplate;
    }

    @Bean
    public Binding bindingForNewPublication(Queue subscriberNotificationsQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(subscriberNotificationsQueue)
                .to(directExchange)
                .with("newPublication");
    }

    @Bean
    public Binding bindingForNewComment(Queue subscriberNotificationsQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(subscriberNotificationsQueue)
                .to(directExchange)
                .with("newComment");
    }

    @Bean(name = "rabbitListenerContainerFactory1")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory1(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(6);
        return factory;
    }

    @Bean(name = "rabbitListenerContainerFactory2")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory2(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(5);
        return factory;
    }

    @Bean(name = "rabbitListenerContainerFactory3")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory3(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(5);
        return factory;
    }

    @Bean
    public ObjectMapper jacksonObjectMapper(){
        return new ObjectMapper();
    }
}
