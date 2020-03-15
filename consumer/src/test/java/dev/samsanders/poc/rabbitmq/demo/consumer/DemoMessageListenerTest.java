package dev.samsanders.poc.rabbitmq.demo.consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.base.Charsets;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

class DemoMessageListenerTest {

  @Test
  void onMessage_withRealConverter() throws Exception {
    Jackson2JsonMessageConverter realMessageConverter = new Jackson2JsonMessageConverter();
    DemoMessageListener demoMessageListener = new DemoMessageListener(realMessageConverter);

    Channel mockChannel = mock(Channel.class);
    byte[] messageBody = "{\"text\":\"test 1\"}".getBytes(Charsets.UTF_8);
    MessageProperties messageProperties = new MessageProperties();
    messageProperties.setContentType("application/json");
    messageProperties.setContentEncoding("UTF-8");
    messageProperties.setDeliveryTag(999);
    messageProperties.setHeader("__TypeId__", "shouldnt-matter");
    Message message = new Message(messageBody, messageProperties);

    demoMessageListener.onMessage(message, mockChannel);

    verify(mockChannel).basicAck(999, false);
  }
}