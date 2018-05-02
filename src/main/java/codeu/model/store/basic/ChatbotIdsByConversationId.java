package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.data.user.chatbot.Chatbot;
import com.google.appengine.repackaged.com.google.common.collect.HashMultimap;
import com.google.appengine.repackaged.com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Integration class of {@link ChatbotStore}, {@link MessageStore}, and {@link ConversationStore},
 * serving as in memory storage of chatbots in each conversation
 *
 * @author Elle Tojaroon (etojaroo@codeustudents.com)
 */
public class ChatbotIdsByConversationId {
  private static Multimap<UUID, UUID> conversationIdToChatbotIds;
  private MessageStore messageStore;
  private ConversationStore conversationStore;
  private ChatbotStore chatbotStore;

  /** Singleton instance of ChatbotIdsByConversationId. */
  private static ChatbotIdsByConversationId instance;

  private ChatbotIdsByConversationId(
      MessageStore messageStore,
      ConversationStore conversationStore,
      ChatbotStore chatbotStore)
  {
    this.messageStore = messageStore;
    this.conversationStore = conversationStore;
    this.chatbotStore = chatbotStore;
    setConversationIdToChatbotIds();
  }

  public static ChatbotIdsByConversationId getInstance(
      MessageStore messageStore,
      ConversationStore conversationStore,
      ChatbotStore chatbotStore)
  {
    if (instance == null) {
      instance = new ChatbotIdsByConversationId(messageStore, conversationStore, chatbotStore);
    }
    return instance;
  }

  /** Add a new chatbot to the current set of chatbots known to the application. */
  public void addChatbotInConversation(Chatbot chatbot, UUID conversationId) {
    chatbotStore.addChatbot(chatbot);
    conversationIdToChatbotIds.put(conversationId, chatbot.getId());
  }

  /**
   * Returns map from conversation id to chatbot ids.
   * This should only be called once, when the data is loaded from Datastore.
   */
  private void setConversationIdToChatbotIds() {
    this.conversationIdToChatbotIds = conversationStore.getAllConversations()
        .stream()
        .map(Conversation::getId)
        .collect(Collector.of(
            HashMultimap::create,
            (acc, conversationId) ->
                acc.putAll(
                    conversationId,
                    getChatbotIdsInConversationFromMessageStore(conversationId)),
            (map1, map2) -> {
                map1.putAll(map2);
                return map1;
            }));
  }

  /**
   * Get chatbot ids from conversation id by iterating over all messages in the conversation
   * and find those whose author is a chatbot
   * @param conversationId  id of the conversation
   * @return  {@code Chatbot} ids in conversation
   */
  private Collection<UUID> getChatbotIdsInConversationFromMessageStore(UUID conversationId) {
    return messageStore.getMessagesInConversation(conversationId)
        .stream()
        .filter(message -> chatbotStore.isChatbot(message.getAuthorId()))
        .map(message -> message.getAuthorId())
        .collect(Collectors.toList());
  }

  /**
   * Get {@code Chatbot} ids in conversation with given {@code conversationId}
   * from in memory data
   * @param conversationId  id of the conversation
   * @return  {@code Chatbot} ids in conversation
   */
  public Collection<UUID> getChatbotIdsInConversation(UUID conversationId) {
    if (conversationIdToChatbotIds.containsKey(conversationId)) {
      return conversationIdToChatbotIds.get(conversationId);
    }
    return Collections.EMPTY_LIST;
  }
}
