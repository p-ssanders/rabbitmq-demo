package dev.samsanders.poc.rabbitmq.demo.consumer;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
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
  Queue queue(@Value("${app.queue.name}") String queueName) {
    return new Queue(queueName, true, false, true);
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
