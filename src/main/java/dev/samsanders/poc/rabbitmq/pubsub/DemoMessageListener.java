package dev.samsanders.poc.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

public class DemoMessageListener implements ChannelAwareMessageListener {

  @Override
  public void onMessage(Message message, Channel channel) throws Exception {
    System.out.printf("Received message: %s%n", message);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
  }

}
