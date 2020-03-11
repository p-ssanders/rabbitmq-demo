package dev.samsanders.poc.rabbitmq.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@TestInstance(Lifecycle.PER_CLASS)
class DemoApplicationTests {

  @Autowired
  EmbeddedAmqpBroker embeddedAmqpBroker;

  @Autowired
  DemoMessagePublisher demoMessagePublisher;

  @Autowired
  DemoConfirmCallback demoConfirmCallback;

  @Autowired
  DemoMessageListener demoMessageListener;

  @BeforeAll
  void beforeAll() {
    embeddedAmqpBroker.start();
  }

  @AfterAll
  void afterAll() {
    embeddedAmqpBroker.stop();
  }

  @Test
  void contextLoads() {
  }

  @Test
  void publish() {
    demoMessagePublisher.publishMessage();
  }

  @Test
  void publish_confirmCallbackInvoked() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    demoConfirmCallback.setCountDownLatch(countDownLatch);

    demoMessagePublisher.publishMessage();

    countDownLatch.await(5000, TimeUnit.MILLISECONDS);
    assertEquals(0, countDownLatch.getCount());
  }

  @Test
  void listener_receivesMessage() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    demoMessageListener.setCountDownLatch(countDownLatch);

    demoMessagePublisher.publishMessage();

    countDownLatch.await(5000, TimeUnit.MILLISECONDS);
    assertEquals(0, countDownLatch.getCount());
  }

  @Test
  @Disabled
  void listener_receivesMessageAndAcks() {
    // TODO prob have to register a bad listener i.e. nacker?
    // or maybe just check 0 messages in queue?
  }

}
