package codeu.model.data.user.chatbot;

import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import java.time.Instant;
import java.util.UUID;

public class HelloChatbot implements Chatbot {
  protected final UUID id;
  protected final String name;
  protected final Instant creation;
  protected String description;

  public HelloChatbot(
      UUID id,
      String name,
      Instant creation)
  {
    this.id = id;
    this.name = name;
    this.creation = creation;
    this.description = "Bot's description";
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
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  public String respondToMessageFrom(User sender, String messageContent) {
    return "Hello " + sender.getName() + "!";
  }
}
