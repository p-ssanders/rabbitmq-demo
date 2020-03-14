#   RabbitMQ Demo

Demonstrate basic publish/subscribe with Spring AMQP.

A single message publisher publishes test messages every two seconds to a direct exchange
using a configured `RabbitTemplate`.
Published messages are confirmed, and confirmation is processed asynchronously using the
`DemoConfirmCallback` to demonstrate how message delivery can be guaranteed.

A single message listener is registered to asynchronously consume messages from the queue.
Messages are acknowledged manually to demonstrate the ability to process before acknowledgement.

Both the exchange, and the queue are durable (the queue will survive a broker restart), and
auto-delete (queue that has had at least one consumer is deleted when last consumer unsubscribes).

The integration tests use an embedded [Apache Qpid](https://qpid.apache.org/) AMQP broker to obviate
the need for a dedicated AMQP broker, or network connectivity for automated tests.

##  Build

`./mvnw clean install`

##  Run

```shell script
SPRING_RABBITMQ_HOST=<your rabbitmq host> \
SPRING_RABBITMQ_USERNAME=<your rabbitmq username> \
SPRING_RABBITMQ_PASSWORD=<your rabbitmq password> \
SPRING_RABBITMQ_VIRTUAL_HOST=<your rabbitmq virtual host, prob same as username> \
./mvnw spring-boot:run
```

##  References

* https://spring.io/guides/gs/messaging-rabbitmq
* https://www.rabbitmq.com/tutorials/amqp-concepts.html
* https://qpid.apache.org/releases/qpid-broker-j-7.1.8/book/index.html
* https://cwiki.apache.org/confluence/display/qpid/How+to+embed+Qpid+Broker-J