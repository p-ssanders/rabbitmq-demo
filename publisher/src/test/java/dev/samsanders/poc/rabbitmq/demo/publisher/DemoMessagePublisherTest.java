package dev.samsanders.poc.rabbitmq.demo.publisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

class DemoMessagePublisherTest {

  @Test
  void publishMessage() {
    RabbitTemplate mockRabbitTemplate = mock(RabbitTemplate.class);
    doNothing().when(mockRabbitTemplate).convertAndSend(any());
    DemoMessagePublisher demoMessagePublisher = new DemoMessagePublisher(mockRabbitTemplate, "some-message-prefix");

    demoMessagePublisher.publishMessage();

    verify(mockRabbitTemplate).convertAndSend(new DemoMessage("some-message-prefix 1"));
  }

}