package dev.samsanders.poc.rabbitmq.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;

public class DemoConfirmCallback implements ConfirmCallback {

  private static final Logger logger = LoggerFactory.getLogger(DemoConfirmCallback.class);

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    logger.info(String.format("Message publish confirmed: %s", ack));
  }
}
