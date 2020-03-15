#   RabbitMQ Demo

Demonstrate publish/subscribe with Spring AMQP.

There are two applications:
*   `publisher`
*   `consumer`

##  Publisher
The `publisher` application creates a single message publisher, and schedules it to publish test
messages every two seconds to a fanout exchange named `app-events` using a configured
`RabbitTemplate`.

Published messages are confirmed, and confirmation is processed asynchronously using the
`DemoConfirmCallback`. A `ConfirmCallback` demonstrates how message delivery can be guaranteed by
providing an opportunity to mark messages as published once the callback is invoked. For example,
the callback would be a good place to update a row in a table with a `published_date_time`.

The exchange is durable (the exchange will survive a broker restart), and auto-delete (exchange is
deleted when all queues have finished using it).

##  Consumer
The `consumer` application creates a queue named `consumer-app` and binds it to an exchange named
`app-events`.
A single message listener is registered to asynchronously consume messages from the queue.

Messages are acknowledged manually to demonstrate the ability to process before acknowledgement.
For example, the listener could insert a row into a table for later processing before
acknowledging receipt of the message to the broker.

The queue is durable (the queue will survive a broker restart), and auto-delete (queue that has had
at least one consumer is deleted when last consumer unsubscribes).

##  Build

`./mvnw clean install`

The integration tests use an embedded [Apache Qpid](https://qpid.apache.org/) AMQP broker to obviate
the need for a dedicated AMQP broker, or network connectivity for automated tests.

##  Run

Start the publisher
```shell script
SPRING_RABBITMQ_HOST=<your rabbitmq host> \
SPRING_RABBITMQ_USERNAME=<your rabbitmq username> \
SPRING_RABBITMQ_PASSWORD=<your rabbitmq password> \
SPRING_RABBITMQ_VIRTUAL_HOST=<your rabbitmq virtual host, prob same as username> \
./mvnw spring-boot:run -pl publisher
```

##  References

* https://spring.io/guides/gs/messaging-rabbitmq
* https://www.rabbitmq.com/tutorials/amqp-concepts.html
* https://qpid.apache.org/releases/qpid-broker-j-7.1.8/book/index.html
* https://cwiki.apache.org/confluence/display/qpid/How+to+embed+Qpid+Broker-J