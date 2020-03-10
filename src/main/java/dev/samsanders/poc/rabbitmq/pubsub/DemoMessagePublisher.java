package dev.samsanders.poc.rabbitmq.pubsub;

import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class DemoMessagePublisher {

  private static final Logger logger = LoggerFactory.getLogger(DemoMessagePublisher.class);
  private final RabbitTemplate rabbitTemplate;
  private final AtomicInteger counter = new AtomicInteger();

  public DemoMessagePublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Scheduled(fixedRate = 2000L)
  public void publishMessage() {
    String message = "test message" + counter.getAndIncrement();
    logger.info(String.format("Publishing message: %s", message));
    rabbitTemplate.convertAndSend(message);
  }

}
