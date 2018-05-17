package codeu.model.data.user.chatbot;

import codeu.model.data.user.User;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class HelloChatbot implements Chatbot {
  private final UUID id;
  private final String name;
  private final Instant creation;
  private final Type type = Type.HELLO;

  public HelloChatbot(
      UUID id,
      String name,
      Instant creation)
  {
    this.id = id;
    this.name = name;
    this.creation = creation;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Instant getCreationTime() {
    return creation;
  }

  @Override
  public Optional<String> respondToMessageFrom(User sender, String messageContent) {
    return Optional.of("Hello " + sender.getName() + "!");
  }

  @Override
  public Type getType() {
    return type;
  }
}
