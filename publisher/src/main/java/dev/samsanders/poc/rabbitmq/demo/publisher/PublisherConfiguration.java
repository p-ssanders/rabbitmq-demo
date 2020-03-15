package dev.samsanders.poc.rabbitmq.demo.publisher;

import org.springframework.amqp.core.FanoutExchange;
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
  FanoutExchange exchange(@Value("${app.exchange.name}") String exchangeName) {
    return new FanoutExchange(exchangeName, true, true);
  }

  @Bean
  DemoMessagePublisher messagePublisher(
      @Value("${app.exchange.name}") String exchangeName,
      CachingConnectionFactory cachingConnectionFactory,
      RabbitTemplate rabbitTemplate,
      DemoConfirmCallback demoConfirmCallback,
      Jackson2JsonMessageConverter jackson2JsonMessageConverter) {

    cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

    rabbitTemplate.setExchange(exchangeName);
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
