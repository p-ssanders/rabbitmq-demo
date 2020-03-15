package dev.samsanders.poc.rabbitmq.demo.publisher;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;

public class DemoConfirmCallback implements ConfirmCallback {

  private static final Logger logger = LoggerFactory.getLogger(DemoConfirmCallback.class);
  private CountDownLatch countDownLatch;

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    logger.info(String.format("Message publish confirmed: %s", ack));

    // Here we could update our event store/table to mark this event as published, maybe with a timestamp
    // to prevent it from being published again, and to provide audit information

    if (countDownLatch != null) {
      countDownLatch.countDown();
    }
  }

  public void setCountDownLatch(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }
}
