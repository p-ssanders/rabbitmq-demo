package dev.samsanders.poc.rabbitmq.pubsub;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public class MessagePublisher {

  private final RabbitTemplate rabbitTemplate;

  public MessagePublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishMessage(String message) {
    System.out.printf("Publishing message: %s%n", message);
    rabbitTemplate.convertAndSend(message);
  }

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    this.publishMessage("test message");
  }

}
