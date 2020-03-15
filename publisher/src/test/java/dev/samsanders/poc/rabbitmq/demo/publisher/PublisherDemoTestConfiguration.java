package dev.samsanders.poc.rabbitmq.demo.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class PublisherDemoTestConfiguration {

  @Value("${test.broker.config.file-location}")
  String brokerConfigFileLocation;

  @Bean
  public EmbeddedAmqpBroker embeddedAmqpBroker() {
    return new EmbeddedAmqpBroker(brokerConfigFileLocation);
  }

}
