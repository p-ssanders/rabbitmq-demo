package dev.samsanders.poc.rabbitmq.demo.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
class ConsumerApplicationTests {

  @Autowired
  EmbeddedAmqpBroker embeddedAmqpBroker;

  @Value("${app.exchange.name}")
  String exchangeName;

  @Autowired
  DemoMessageListener demoMessageListenerA;

  @Autowired
  DemoMessageListener demoMessageListenerB;

  @Autowired
  RabbitTemplate rabbitTemplate;

  @Autowired
  Queue queueA;

  @Autowired
  Queue queueB;

  @BeforeAll
  void beforeAll() {
    embeddedAmqpBroker.start();
    rabbitTemplate.setExchange(exchangeName);

    // Unset queue declaration argument that is not supported by Qpid
    queueA.setMasterLocator(null);
    queueB.setMasterLocator(null);
  }

  @AfterAll
  void afterAll() {
    embeddedAmqpBroker.stop();
  }

  @Test
  void contextLoads() {
  }

  @Test
  void listeners_receiveMessages() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(2);
    demoMessageListenerA.setCountDownLatch(countDownLatch);
    demoMessageListenerB.setCountDownLatch(countDownLatch);

    rabbitTemplate.convertAndSend(new TestMessage("some-text"));

    countDownLatch.await(5000, TimeUnit.MILLISECONDS);
    assertEquals(0, countDownLatch.getCount());
  }

  @Test
  @Disabled
  void listener_receivesMessageAndAcks() {
    // TODO prob have to register a bad listener i.e. nacker?
    // or maybe just check 0 messages in queue?
  }

  @Test
  @Disabled
  void listener_isExclusive() {

  }

  private static class TestMessage {

    private final String text;

    private TestMessage(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof TestMessage)) {
        return false;
      }
      TestMessage that = (TestMessage) o;
      return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
      return Objects.hash(text);
    }

    @Override
    public String toString() {
      return "TestMessage{" +
          "text='" + text + '\'' +
          '}';
    }
  }

}
