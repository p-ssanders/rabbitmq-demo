#   RabbitMQ Demo

Demonstrate basic publish/subscribe with RabbitMQ

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