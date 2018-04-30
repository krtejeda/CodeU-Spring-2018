package codeu.model.data.user.chatbot;

import codeu.model.data.user.MessageSender;
import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;

public interface Chatbot extends MessageSender {

  String respondToMessageFrom(User sender, String messageContent);

  default UserGroup group() {
    return UserGroup.BOT;
  }
}
