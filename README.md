#   RabbitMQ Demo

Demonstrate publish/subscribe with Spring AMQP and RabbitMQ.

There are two applications:
*   `publisher`
*   `consumer`

Unit tests and integration tests help document the functionality of each.

##  Publisher
The `publisher` application creates a single message publisher, and schedules it to publish test
messages every two seconds to a fanout exchange named `demo-app-events`. Messages are serialized to JSON
using a `Jackson2JsonMessageConverter`.

Published messages are confirmed by the broker, and the confirmation is processed asynchronously
using the `DemoConfirmCallback`.

The `ConfirmCallback` demonstrates how message delivery can be guaranteed by providing an
opportunity to mark messages as published once the callback is invoked. For example, the callback
would be a good place to update a corresponding row in a table that contains events to be published
with the `published_date_time` to prevent re-publishing, and provide audit information.

The exchange is:
*   non-durable (the exchange will be deleted if the broker shuts down)
*   auto-delete (exchange is deleted when all queues have finished using it)

##  Consumer
The `consumer` application creates two `AnonymousQueue`s, and binds them to an exchange named
`demo-app-events`. The queues are named using a `UUIDNamingStrategy`.

A message listener is registered to asynchronously, and exclusively consume messages from each
queue. Messages are deserialized from JSON using a `Jackson2JsonMessageConverter`. Since the type of
the message published by the `publisher` doesn't exist in the `consumer` app, a "conversion hint" is
supplied to the converter at runtime.

Messages are manually acknowledged to demonstrate the ability to process the message before
acknowledgement.
For example, the listener could insert a row into a table before acknowledging receipt of the
message to the broker.

The queues are:
*   non-durable (the queue will be deleted if the broker shuts down)
*   exclusive (can be used by only one connection)
*   auto-delete (will be deleted when consumer unsubscribes)

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
SPRING_RABBITMQ_VIRTUAL_HOST=<your rabbitmq virtual host> \
./mvnw spring-boot:run -pl publisher
```

Start the consumers
```shell script
SPRING_RABBITMQ_HOST=<your rabbitmq host> \
SPRING_RABBITMQ_USERNAME=<your rabbitmq username> \
SPRING_RABBITMQ_PASSWORD=<your rabbitmq password> \
SPRING_RABBITMQ_VIRTUAL_HOST=<your rabbitmq virtual host> \
./mvnw spring-boot:run -pl consumer
```

##  References

* https://docs.spring.io/spring-amqp/reference/html
* https://www.rabbitmq.com/tutorials/amqp-concepts.html
* https://cwiki.apache.org/confluence/display/qpid/How+to+embed+Qpid+Broker-J