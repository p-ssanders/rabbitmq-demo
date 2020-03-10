package dev.samsanders.poc.rabbitmq.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class DemoMessagePublisher {

  private static final Logger logger = LoggerFactory.getLogger(DemoMessagePublisher.class);
  private final RabbitTemplate rabbitTemplate;

  public DemoMessagePublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishMessage(String message) {
    logger.info(String.format("Publishing message: %s%n", message));
    rabbitTemplate.convertAndSend(message);
  }

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    this.publishMessage("test message");
  }

}
