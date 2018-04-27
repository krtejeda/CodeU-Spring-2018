package codeu.model.data.user.chatbot;

import codeu.model.data.user.Chatbot;
import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import java.time.Instant;
import java.util.UUID;

public class HelloChatbot extends User implements Chatbot {

  public HelloChatbot(
      UUID id,
      String name,
      Instant creation)
  {
    super(
        id,
        name,
        null /* password */,
        creation,
        getDefaultDescription(name),
        UserGroup.BOT);
  }

  public String respondToMessageFrom(User sender, String messageContent) {
    return "Hello " + sender.getName() + "!";
  }
}
