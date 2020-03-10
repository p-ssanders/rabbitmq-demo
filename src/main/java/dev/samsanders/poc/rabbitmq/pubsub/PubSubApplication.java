package dev.samsanders.poc.rabbitmq.pubsub;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PubSubApplication {

  static final String EXCHANGE_NAME = "demo-exchange";
  static final String QUEUE_NAME = "demo-queue";

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
  SimpleMessageListenerContainer container(
      ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter
  ) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(QUEUE_NAME);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(MessageListener receiver) {
    return new MessageListenerAdapter(receiver);
  }

  @Bean
  MessageListener messageReceiver() {
    return new MessageListener();
  }

  @Bean
  MessagePublisher messagePublisher(RabbitTemplate rabbitTemplate) {
    rabbitTemplate.setExchange(EXCHANGE_NAME);
    rabbitTemplate.setRoutingKey(QUEUE_NAME);
    return new MessagePublisher(rabbitTemplate);
  }

  public static void main(String[] args) {
    SpringApplication.run(PubSubApplication.class, args);
  }

}
