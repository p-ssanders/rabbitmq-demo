package dev.samsanders.poc.rabbitmq.pubsub;

public class DemoMessageListener {

  public void handleMessage(String message) {
    System.out.printf("Received message: %s%n", message);
  }

}