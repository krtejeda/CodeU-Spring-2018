package codeu.model.store.basic;

import codeu.model.data.user.chatbot.Chatbot;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ChatbotStore {

  /** Singleton instance of ChatbotStore. */
  private static ChatbotStore instance;

  /**
   * Returns the singleton instance of ChatbotStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static ChatbotStore getInstance() {
    if (instance == null) {
      instance = new ChatbotStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ChatbotStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ChatbotStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory Chatbots. */
  private Map<UUID, Chatbot> chatbotById;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ChatbotStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    chatbotById = new HashMap<>();
  }

  /**
   * Access the Chatbot object with the given UUID.
   *
   * @return null if the UUID does not match any existing Chatbot.
   */
  public Chatbot getChatbot(UUID id) {
    return chatbotById.get(id);
  }

  /**
   * Returns true if there exists a Chatbot object with the given UUID.
   *
   * @return false if the UUID does not match any existing Chatbot.
   */
  public boolean isChatbot(UUID id) {
    return chatbotById.containsKey(id);
  }

  /** Add a new chatbot to the current set of chatbots known to the application. */
  void addChatbot(Chatbot chatbot) {
    chatbotById.put(chatbot.getId(), chatbot);
    persistentStorageAgent.writeThrough(chatbot);
  }

  /**
   * Sets the List of Users stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setChatbots(List<Chatbot> chatbots) {
    this.chatbotById = chatbots.stream()
        .collect(Collectors.toMap(Chatbot::getId, Function.identity()));
  }
}
