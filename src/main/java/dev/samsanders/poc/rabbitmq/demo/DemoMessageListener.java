package dev.samsanders.poc.rabbitmq.demo;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

public class DemoMessageListener implements ChannelAwareMessageListener {

  private static final Logger logger = LoggerFactory.getLogger(DemoMessageListener.class);

  @Override
  public void onMessage(Message message, Channel channel) throws Exception {
    logger.info(String.format("Received message: %s", message));
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
  }

}