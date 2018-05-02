package codeu.model.data.user.chatbot;

import codeu.model.data.Conversation;
import codeu.model.data.user.MessageSender;
import codeu.model.data.user.User;
import codeu.model.data.user.UserGroup;
import java.util.Optional;

/**
 * Interface represents Chatbot, an automated program that may respond to
 * messages sent to a {@link Conversation}
 */
public interface Chatbot extends MessageSender {

  /**
   * Returns response from the {@code Chatbot} to {@code messageContent} from {@code sender}
   * @param sender          {@code User} that sends the {@code messageContent}
   * @param messageContent  content of the message
   * @return  Optional response from the Chatbot
   */
  Optional<String> respondToMessageFrom(User sender, String messageContent);

  default UserGroup group() {
    return UserGroup.BOT;
  }

  enum Type {
    HELLO;
  }

  Type getType();
}
