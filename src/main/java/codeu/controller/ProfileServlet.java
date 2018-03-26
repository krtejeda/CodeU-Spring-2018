package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class ProfileServlet extends HttpServlet {

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

        List<Conversation> ownerConversations = getConversationsOfUser(owner);
        List<Message> ownerMessages = getMessagesOfUser(owner, ownerConversations);

        request.setAttribute("conversations", ownerConversations);
        request.setAttribute("messages", ownerMessages);
        request.setAttribute("owner", owner);
        request.getRequestDispatcher("/WEB-INF/view/profile.jsp")
            .forward(request, response);
    }

    /**
     * Get all conversations that {@code user} has sent a message to
     * @param user
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
     * Get all messages that {@code user} sent in {@code conversations}
     * @param user              user who sent the messages
     * @param conversations     conversations to look for the messages
     * @return  messages that {@code user} sent
     */
    private List<Message> getMessagesOfUser(User user, Collection<Conversation> conversations) {
        return conversations
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
        throws IOException {
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
      userStore.updateUserDescription(user, cleanedNewDescription);

      // redirect to a GET request
      response.sendRedirect("/profile/" + logInName);
    }
}