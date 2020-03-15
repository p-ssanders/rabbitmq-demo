package dev.samsanders.poc.rabbitmq.demo.consumer;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.UUIDNamingStrategy;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerConfiguration {

  @Bean
  FanoutExchange exchange(@Value("${app.exchange.name}") String exchangeName) {
    return new FanoutExchange(exchangeName, true, true);
  }

  @Bean
  Queue queue() {
    return new AnonymousQueue(new UUIDNamingStrategy());
  }

  @Bean
  Binding binding(Queue queue, FanoutExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange);
  }

  @Bean
  MessageListenerContainer messageListenerContainer(
      Queue queue,
      CachingConnectionFactory cachingConnectionFactory,
      DemoMessageListener messageListener) {
    DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
    messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
    messageListenerContainer.setQueueNames(queue.getName());
    messageListenerContainer.setMessageListener(messageListener);

    messageListenerContainer.setExclusive(true);
    messageListenerContainer.setConsumersPerQueue(1);

    messageListenerContainer.setPrefetchCount(1);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);

    return messageListenerContainer;
  }

  @Bean
  DemoMessageListener demoMessageListener(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    return new DemoMessageListener(jackson2JsonMessageConverter);
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
