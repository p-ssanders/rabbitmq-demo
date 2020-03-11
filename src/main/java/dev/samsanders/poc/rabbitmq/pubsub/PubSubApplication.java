package dev.samsanders.poc.rabbitmq.pubsub;

import org.springframework.amqp.core.AcknowledgeMode;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PubSubApplication {

  public static void main(String[] args) {
    SpringApplication.run(PubSubApplication.class, args);
  }

  @Bean
  Queue queue(@Value("${app.queue.name}") String queueName) {
    return new Queue(queueName, true, false, true);
  }

  @Bean
  DirectExchange exchange(@Value("${app.exchange.name}") String exchangeName) {
    return new DirectExchange(exchangeName, true, true);
  }

  @Bean
  Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).withQueueName();
  }

  @Bean
  MessageListenerContainer messageListenerContainer(
      @Value("${app.queue.name}") String queueName,
      CachingConnectionFactory cachingConnectionFactory,
      DemoMessageListener messageListener) {
    DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
    messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
    messageListenerContainer.setQueueNames(queueName);
    messageListenerContainer.setMessageListener(messageListener);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);

    return messageListenerContainer;
  }

  @Bean
  DemoMessageListener demoMessageListener() {
    return new DemoMessageListener();
  }

  @Bean
  DemoMessagePublisher messagePublisher(
      @Value("${app.exchange.name}") String exchangeName,
      @Value("${app.queue.name}") String queueName,
      CachingConnectionFactory cachingConnectionFactory,
      RabbitTemplate rabbitTemplate,
      ConfirmCallback confirmCallback) {

    cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

    rabbitTemplate.setExchange(exchangeName);
    rabbitTemplate.setRoutingKey(queueName);
    rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
    rabbitTemplate.setConfirmCallback(confirmCallback);

    return new DemoMessagePublisher(rabbitTemplate);
  }

  @Bean
  ConfirmCallback demoConfirmCallback() {
    return new DemoConfirmCallback();
  }

}
