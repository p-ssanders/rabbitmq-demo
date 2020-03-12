package dev.samsanders.poc.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

public class DemoMessageListener implements ChannelAwareMessageListener {

  private static final Logger logger = LoggerFactory.getLogger(DemoMessageListener.class);
  private final Jackson2JsonMessageConverter messageConverter;
  private CountDownLatch countDownLatch;

  public DemoMessageListener(Jackson2JsonMessageConverter messageConverter) {
    this.messageConverter = messageConverter;
  }

  @Override
  public void onMessage(Message message, Channel channel) throws Exception {
    DemoMessage demoMessage = (DemoMessage) messageConverter.fromMessage(message);
    logger.info(String.format("Received message: %s", demoMessage));

    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    if (countDownLatch != null) {
      countDownLatch.countDown();
    }
  }

  public void setCountDownLatch(CountDownLatch countDownLatch) {
    this.countDownLatch = countDownLatch;
  }
}
