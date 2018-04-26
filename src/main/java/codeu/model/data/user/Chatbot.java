package codeu.model.data.user;

import codeu.model.data.Conversation;

public interface Chatbot {

  void sendMessageToConversation(String messageContent, Conversation conversation);

}
