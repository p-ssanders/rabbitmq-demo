package dev.samsanders.poc.rabbitmq.pubsub;

public class MessageListener {

  public void handleMessage(String message) {
    System.out.println("Received message: " + message);
  }

}
