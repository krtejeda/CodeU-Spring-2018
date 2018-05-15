package codeu.model.data.user.chatbot;

import codeu.model.data.user.User;
import codeu.model.dialogflow.DetectIntentTexts;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class FirstDialogflowChatbot implements Chatbot {
  private final UUID id;
  private final String name;
  private final Instant creation;
  private final Type type = Type.HELLO;

  private static final String PROJECT_ID = "code-u-team-27";
  private static final String SESSION_ID = UUID.randomUUID().toString();
  private static final String LANGUAGE_CODE = "en-US";

  public FirstDialogflowChatbot(
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
    // TODO Test dialogflow prints
    String respondMessage = "Hello " + sender.getName() + "!";
    try {
      respondMessage = DetectIntentTexts.detectIntentTexts(
          PROJECT_ID,
          messageContent,
          SESSION_ID,
          LANGUAGE_CODE);
    } catch (Exception e) {
      System.out.println("Fail to get results from dialogflow: " + e);
    }
    // TODO End test dialogflow prints

    return Optional.of(respondMessage);
  }

  @Override
  public Type getType() {
    return type;
  }
}
