package dev.samsanders.poc.rabbitmq.demo.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class ConsumerDemoTestConfiguration {

  @Value("${test.broker.config.file-location}")
  String brokerConfigFileLocation;

  @Bean
  public EmbeddedAmqpBroker embeddedAmqpBroker() {
    return new EmbeddedAmqpBroker(brokerConfigFileLocation);
  }

}
