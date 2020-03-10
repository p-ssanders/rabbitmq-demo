package dev.samsanders.poc.rabbitmq.pubsub;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;

public class DemoConfirmCallback implements ConfirmCallback {

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    System.out.printf("Message publish confirmed: %s%n", ack);
  }
}
