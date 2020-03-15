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
import org.springframework.context.annotation.Profile;

@Configuration
public class ConsumerConfiguration {

  @Bean
  FanoutExchange exchange(@Value("${app.exchange.name}") String exchangeName) {
    return new FanoutExchange(exchangeName, false, true);
  }

  @Bean
  @Profile("!test")
  Queue queueA() {
    return new AnonymousQueue(new UUIDNamingStrategy());
  }

  @Bean
  Binding bindingA(Queue queueA, FanoutExchange exchange) {
    return BindingBuilder.bind(queueA).to(exchange);
  }

  @Bean
  MessageListenerContainer messageListenerContainerA(
      Queue queueA,
      CachingConnectionFactory cachingConnectionFactory,
      DemoMessageListener demoMessageListenerA) {
    DirectMessageListenerContainer messageListenerContainer = messageListenerContainer();
    messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
    messageListenerContainer.setQueueNames(queueA.getName());
    messageListenerContainer.setMessageListener(demoMessageListenerA);

    return messageListenerContainer;
  }

  @Bean
  DemoMessageListener demoMessageListenerA(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    return new DemoMessageListener(jackson2JsonMessageConverter);
  }

  @Bean
  @Profile("!test")
  Queue queueB() {
    return new AnonymousQueue(new UUIDNamingStrategy());
  }

  @Bean
  Binding bindingB(Queue queueB, FanoutExchange exchange) {
    return BindingBuilder.bind(queueB).to(exchange);
  }

  @Bean
  MessageListenerContainer messageListenerContainerB(
      Queue queueB,
      CachingConnectionFactory cachingConnectionFactory,
      DemoMessageListener demoMessageListenerB) {
    DirectMessageListenerContainer messageListenerContainer = messageListenerContainer();
    messageListenerContainer.setConnectionFactory(cachingConnectionFactory);
    messageListenerContainer.setQueueNames(queueB.getName());
    messageListenerContainer.setMessageListener(demoMessageListenerB);

    return messageListenerContainer;
  }
  @Bean
  DemoMessageListener demoMessageListenerB(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    return new DemoMessageListener(jackson2JsonMessageConverter);
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  private DirectMessageListenerContainer messageListenerContainer() {
    DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
    messageListenerContainer.setExclusive(true);
    messageListenerContainer.setConsumersPerQueue(1);
    messageListenerContainer.setPrefetchCount(1);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);

    return messageListenerContainer;
  }

}
