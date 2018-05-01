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
import java.util.stream.Collectors;

/** Servlet class responsible for the Activity Feed.
 * @author Kelvin Tejeda (ktejeda@codeustudents.com)
 */
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

    List<Object> activity = sortByCreationTime(conversations, messages, users);

    request.setAttribute("activity", activity);
    request.getRequestDispatcher("/WEB-INF/view/activity.jsp").forward(request, response);
  }

  private List<Object> sortByCreationTime(List<Conversation> conversations, List<Message> messages,
	    List<User> users) {
    List<Object> activity = new ArrayList<>();
    activity.addAll(conversations);
    activity.addAll(messages);
    activity.addAll(users);
    Comparator<Object> byCreationDate = Comparator.comparing(o -> getCreationDate(o)).reversed();
    return activity.stream().sorted(byCreationDate).collect(Collectors.toList());
  }

  private Instant getCreationDate(Object object) {
    if (object.getClass() == Conversation.class) {
      Conversation conversation = (Conversation) object;
      return conversation.getCreationTime();
    } else if (object.getClass() == Message.class) {
      Message message = (Message) object;
      return message.getCreationTime();
    } else if (object.getClass() == User.class) {
      User user = (User) object;
      return user.getCreationTime();
    }
    return null;
  }
}
