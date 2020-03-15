package dev.samsanders.poc.rabbitmq.demo.publisher;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class PublisherConfiguration {

  @Profile("!test")
  @EnableScheduling
  public static class SchedulingConfiguration {

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
  DemoMessagePublisher messagePublisher(
      @Value("${app.exchange.name}") String exchangeName,
      @Value("${app.queue.name}") String queueName,
      CachingConnectionFactory cachingConnectionFactory,
      RabbitTemplate rabbitTemplate,
      DemoConfirmCallback demoConfirmCallback,
      Jackson2JsonMessageConverter jackson2JsonMessageConverter) {

    cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

    rabbitTemplate.setExchange(exchangeName);
    rabbitTemplate.setRoutingKey(queueName);
    rabbitTemplate.setConnectionFactory(cachingConnectionFactory);
    rabbitTemplate.setConfirmCallback(demoConfirmCallback);
    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);

    return new DemoMessagePublisher(rabbitTemplate);
  }

  @Bean
  DemoConfirmCallback demoConfirmCallback() {
    return new DemoConfirmCallback();
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
