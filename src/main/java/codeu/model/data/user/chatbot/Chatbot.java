package codeu.model.data.user.chatbot;

import codeu.model.data.user.MessageSender;
import codeu.model.data.user.User;

public interface Chatbot extends MessageSender {

  String respondToMessageFrom(User sender, String messageContent);

}
