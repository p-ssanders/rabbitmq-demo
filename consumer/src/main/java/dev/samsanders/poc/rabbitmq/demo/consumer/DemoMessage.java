package dev.samsanders.poc.rabbitmq.demo.consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class DemoMessage {

  private final String text;

  @JsonCreator
  public DemoMessage(@JsonProperty("text") String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DemoMessage)) {
      return false;
    }
    DemoMessage that = (DemoMessage) o;
    return Objects.equals(text, that.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public String toString() {
    return "DemoMessage{" +
        "text='" + text + '\'' +
        '}';
  }
}
