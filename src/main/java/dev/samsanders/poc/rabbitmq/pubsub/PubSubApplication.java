package dev.samsanders.poc.rabbitmq.pubsub;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PubSubApplication {

  static final String EXCHANGE_NAME = "demo-exchange";
  static final String QUEUE_NAME = "demo-queue";

  public static void main(String[] args) {
    SpringApplication.run(PubSubApplication.class, args);
  }

  @Bean
  Queue queue() {
    return new Queue(QUEUE_NAME, false);
  }

  @Bean
  DirectExchange exchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).withQueueName();
  }

  @Bean
  MessageListenerContainer messageListenerContainer(CachingConnectionFactory cachingConnectionFactory,
      MessageListenerAdapter listenerAdapter) {
    DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
    messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
    messageListenerContainer.setQueueNames(QUEUE_NAME);
    messageListenerContainer.setMessageListener(listenerAdapter);

    return messageListenerContainer;
  }

  @Bean
  MessageListenerAdapter messageListenerAdapter(DemoMessageListener messageListener) {
    return new MessageListenerAdapter(messageListener);
  }

  @Bean
  DemoMessageListener messageListener() {
    return new DemoMessageListener();
  }

  @Bean
  DemoMessagePublisher messagePublisher(CachingConnectionFactory cachingConnectionFactory,
      RabbitTemplate rabbitTemplate,
      ConfirmCallback confirmCallback) {

    cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

    rabbitTemplate.setExchange(EXCHANGE_NAME);
    rabbitTemplate.setRoutingKey(QUEUE_NAME);
    rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
    rabbitTemplate.setConfirmCallback(confirmCallback);

    return new DemoMessagePublisher(rabbitTemplate);
  }

  @Bean
  ConfirmCallback demoConfirmCallback() {
    return new DemoConfirmCallback();
  }

}
