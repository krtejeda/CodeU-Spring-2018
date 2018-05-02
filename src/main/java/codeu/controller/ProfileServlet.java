package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.user.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Servlet class responsible for individual profile page
 *
 * @author Elle Tojaroon (etojaroo@codeustudents.com)
 */
public class ProfileServlet extends HttpServlet {

    private static final String DISPLAY_MESSAGE_TIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
    private ConversationStore conversationStore;
    private MessageStore messageStore;
    private UserStore userStore;

    @Override
    public void init() throws ServletException {
        super.init();
        setConversationStore(ConversationStore.getInstance());
        setMessageStore(MessageStore.getInstance());
        setUserStore(UserStore.getInstance());
    }

    void setConversationStore(ConversationStore conversationStore) {
        this.conversationStore = conversationStore;
    }

    void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    void setUserStore(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * This function fires when a user navigates to the profile page. It gets
     * profile owner's name from the URL, finds all conversations and messages
     * the owner is in and sent. It then forwards the conversations, owner, and messages data
     * to profile.jsp for rendering.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String requestUrl = request.getRequestURI();
        String ownerName = requestUrl.substring("/profile/".length());
        User owner = userStore.getUser(ownerName);
        if (owner == null) {
            // owner was not found, direct to homepage
            response.sendRedirect("/conversations");
            return;
        }

        request.setAttribute("conversations", getConversationsOfUser(owner));
        request.setAttribute(
            "messageDisplayTimeToMessageContent",
            getMessageDisplayTimeToMessageContentOfUser(owner));
        request.setAttribute("owner", owner);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp")
            .forward(request, response);
    }

  /**
   * Get integration of all messages of {@code user} from message display time to message content.
   * Display time is in format e.g. "Mon Dec 03 21:44:50 EST 2018"
   * @param user  who sent messages
   * @return immutable integration from message display time to message content.
   */
  private ImmutableMap<String, String> getMessageDisplayTimeToMessageContentOfUser(User user) {
    return getMessagesOfUser(user)
        .stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(
                this::getDisplayMessageTime,
                Message::getContent,
                (firstContent, secondContent) -> firstContent),
            ImmutableMap::copyOf));
  }

  private String getDisplayMessageTime(Message message) {
    return DateTimeFormatter.ofPattern(DISPLAY_MESSAGE_TIME_PATTERN)
        .withZone(TimeZone.getDefault().toZoneId())
        .format(message.getCreationTime());
  }

  /**
   * Get all conversations that {@code user} has sent a message to
   *
   * @return list of conversations {@code user} has sent a message to
   */
  private List<Conversation> getConversationsOfUser(User user) {
    return conversationStore.getAllConversations()
        .stream()
        .filter(conversation ->
            messageStore.getMessagesInConversation(conversation.id)
                .stream()
                .filter(message -> message.getAuthorId().equals(user.getId()))
                .findAny()
                .isPresent())
        .collect(Collectors.toList());
  }

  /**
   * Get all messages that {@code user} sent in {@code conversations}, sorted in order of creation
   * time
   *
   * @param user user who sent the messages
   * @return messages that {@code user} sent
   */
  private List<Message> getMessagesOfUser(User user) {
    return getConversationsOfUser(user)
        .stream()
        .flatMap(conversation ->
            messageStore.getMessagesInConversation(conversation.id)
                .stream()
                .filter(message -> message.getAuthorId().equals(user.getId())))
        .sorted(Comparator.comparing(Message::getCreationTime).reversed())
        .collect(Collectors.toList());
  }

  /**
   * This function fires when a user submits the form to edit description on the profile page. It
   * gets the logged-in username from the session, verifying against username from the URL. It adds
   * the description to the user model, redirects back to the profile page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String urlName = requestUrl.substring("/profile/".length());

    String logInName = (String) request.getSession().getAttribute("user");
    if (logInName == null || !urlName.equals(logInName)) {
      // user is not logged in or not editing their own descriptions,
      // don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(logInName);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    String newDescription = request.getParameter("description");

    // this removes any HTML from the description content
    String cleanedNewDescription = Jsoup.clean(newDescription, Whitelist.none());

    user.setDescription(cleanedNewDescription);
    if (userStore.updateUserDescription(user, cleanedNewDescription)) {
      // redirect to a GET request
      response.sendRedirect("/profile/" + logInName);
    } else {
      // alert or something
      request.setAttribute(
          "updateDescriptionError",
          "Failed to update your description. Please try again later.");
      doGet(request, response);
    }
  }
}
