package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import java.util.Comparator;
import java.util.Collections;

/** Servlet class responsible for the Activity Feed. */
public class ActivityServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the Activity page. It gets the conversations, messages,
	 * and users from their respective stores and sorts them into one list by the most recent creation time.
	 * It then forwards that list to activity.jsp in order to render it.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    List<Conversation> conversations = conversationStore.getAllConversations();
    List<Message> messages = messageStore.getAllMessages();
    List<User> users = userStore.getAllUsers();

    List<Object> activity = new ArrayList<>();

    int c = conversations.size() - 1;
    int m = messages.size() - 1;
    int u = users.size() - 1;

    while ((c >= 0) || (m >= 0) || (u >= 0)) {
      if ((c == -1 && u == -1) ||
          (c == -1 && m != -1 && u != -1 && messages.get(m).getCreationTime().isAfter(users.get(u).getCreationTime())) ||
          (u == -1 && m != -1 && c != -1 && messages.get(m).getCreationTime().isAfter(conversations.get(c).getCreationTime())) ||
          (c != -1 && m != -1 && u != -1 && messages.get(m).getCreationTime().isAfter(conversations.get(c).getCreationTime()) &&
             messages.get(m).getCreationTime().isAfter(users.get(u).getCreationTime()))) {
        activity.add(messages.get(m));
        m--;
      } else if ((c == -1 && m == -1) ||
       	         (c == -1 && u != -1 && m != -1 && users.get(u).getCreationTime().isAfter(messages.get(m).getCreationTime())) ||
                 (m == -1 && u != -1 && c != -1 && users.get(u).getCreationTime().isAfter(conversations.get(c).getCreationTime())) ||
                 (c != -1 && m != -1 && u != -1 && users.get(u).getCreationTime().isAfter(conversations.get(c).getCreationTime()) &&
                    users.get(u).getCreationTime().isAfter(messages.get(m).getCreationTime()))) {
        activity.add(users.get(u));
        u--;
      } else if ((u == -1 && m == -1) ||
                 (u == -1 && c != -1 && m != -1 && conversations.get(c).getCreationTime().isAfter(messages.get(m).getCreationTime())) ||
                 (m == -1 && c != -1 && u != -1 && conversations.get(c).getCreationTime().isAfter(users.get(u).getCreationTime())) ||
                 (c != -1 && m != -1 && u != -1 && conversations.get(c).getCreationTime().isAfter(users.get(u).getCreationTime()) &&
                    conversations.get(c).getCreationTime().isAfter(messages.get(m).getCreationTime()))) {
        activity.add(conversations.get(c));
        c--;
      }
    }
    request.setAttribute("activity", activity);
    request.getRequestDispatcher("/WEB-INF/view/activity.jsp").forward(request, response);
  }
}
