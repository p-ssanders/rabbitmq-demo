package dev.samsanders.poc.rabbitmq.pubsub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	// TODO integration tests
	// publishing a message publishes
	// publishing a message invokes confirm callback
	// messagelistener receives messages when publishes
	// messagelistener acks messages when received

}
