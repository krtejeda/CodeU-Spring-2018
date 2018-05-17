// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ConversationStore {

  /** Singleton instance of ConversationStore. */
  private static ConversationStore instance;

  /**
   * Returns the singleton instance of ConversationStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static ConversationStore getInstance() {
    if (instance == null) {
      instance = new ConversationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ConversationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ConversationStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Conversations from and saving Conversations
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory Conversations. */
  private Map<String, Conversation> conversationByTitle;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ConversationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    conversationByTitle = new HashMap<>();
  }

  /**
   * Load a set of randomly-generated Conversation objects.
   *
   * @return false if a error occurs.
   */
  public boolean loadTestData() {
    boolean loaded = false;
    try {
      conversationByTitle.putAll(
          conversationsToConversationByTitle(
              DefaultDataStore.getInstance().getAllConversations()));
      loaded = true;
    } catch (Exception e) {
      loaded = false;
      System.err.println("ERROR: Unable to establish initial store (conversations).");
    }
    return loaded;
  }

  /** Access the current set of conversationByTitle known to the application. */
  public List<Conversation> getAllConversations() {
    return conversationByTitle.values()
        .stream()
        .collect(Collectors.toList());
  }

  /** Add a new conversation to the current set of conversationByTitle known to the application. */
  public void addConversation(Conversation conversation) {
    conversationByTitle.put(conversation.getTitle(), conversation);
    persistentStorageAgent.writeThrough(conversation);
  }

  /** Check whether a Conversation title is already known to the application. */
  public boolean isTitleTaken(String title) {
    return conversationByTitle.containsKey(title);
  }

  /** Find and return the Conversation with the given title. */
  public Conversation getConversationWithTitle(String title) {
    return conversationByTitle.get(title);
  }

  /* Find and return the Conversation with the given UUID.
	 * Returns null if UUID corresponds to no known conversation.
	 */
  public Conversation getConversationByUUID(UUID id) {
    for (Conversation conversation : conversationByTitle.values()) {
      if (conversation.getId().equals(id)) {
        return conversation;
      }
    }
    return null;
  }

  /** Sets the List of Conversations stored by this ConversationStore. */
  public void setConversations(List<Conversation> conversations) {
    this.conversationByTitle = conversationsToConversationByTitle(conversations);
  }

  private Map<String, Conversation> conversationsToConversationByTitle(
      List<Conversation> conversations)
  {
    return conversations
        .stream()
        .collect(Collectors.toMap(
            Conversation::getTitle,
            Function.identity()));
  }
}
