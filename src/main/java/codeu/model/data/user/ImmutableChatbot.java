package codeu.model.data.user;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.basic.MessageStore;
import java.time.Instant;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ImmutableChatbot extends User implements Chatbot {
  private MessageStore messageStore;

  public ImmutableChatbot(
      UUID id,
      String name,
      Instant creation,
      MessageStore messageStore)
  {
    super(
        id,
        name,
        null /* password */,
        creation, getDefaultDescription(name),
        UserGroup.BOT);

    this.messageStore = messageStore;
  }

  public void sendMessageToConversation(String messageContent, Conversation conversation) {
    if (conversation == null) {
      return;
    }

    String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());
    Message message =
        new Message(
            UUID.randomUUID(),
            conversation.getId(),
            id,
            cleanedMessageContent,
            Instant.now());
    messageStore.addMessage(message);
  }
}
