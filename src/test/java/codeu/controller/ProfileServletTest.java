package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ProfileServletTest {

    private ProfileServlet profileServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private ConversationStore mockConversationStore;
    private MessageStore mockMessageStore;
    private UserStore mockUserStore;
    private HttpSession mockSession;

    @Before
    public void setup() {
        mockRequest = Mockito.mock(HttpServletRequest.class);
        mockResponse = Mockito.mock(HttpServletResponse.class);
        mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
        mockConversationStore = Mockito.mock(ConversationStore.class);
        mockMessageStore = Mockito.mock(MessageStore.class);
        mockUserStore = Mockito.mock(UserStore.class);
        profileServlet = new ProfileServlet();
        profileServlet.setConversationStore(mockConversationStore);
        profileServlet.setMessageStore(mockMessageStore);
        profileServlet.setUserStore(mockUserStore);
        mockSession = Mockito.mock(HttpSession.class);
        Mockito.when(mockRequest.getSession()).thenReturn(mockSession);
        Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/profile.jsp"))
            .thenReturn(mockRequestDispatcher);
    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        String username = "Mary";
        User user =
            new User(
                UUID.randomUUID(),
                username,
                username,
                Instant.now());
        Mockito.when(mockRequest.getRequestURI())
            .thenReturn("/profile/" + username);
        Mockito.when(mockUserStore.getUser(username))
            .thenReturn(user);

        Conversation conversation =
            new Conversation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "mock conversation",
                Instant.now()
            );
        List<Conversation> conversations = Lists.newArrayList(conversation);
        Mockito.when(mockConversationStore.getAllConversations())
            .thenReturn(conversations);

        Message message =
            new Message(
                UUID.randomUUID(),
                conversation.getId(),
                user.getId(),
                "some message content",
                Instant.now()
            );
        List<Message> messages = Lists.newArrayList(message);
        Mockito
            .when(mockMessageStore.getMessagesInConversation(conversation.getId()))
            .thenReturn(messages);

        profileServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest)
            .setAttribute("conversations", conversations);
        Mockito.verify(mockRequest)
            .setAttribute("owner", user);
        Mockito.verify(mockRequest)
            .setAttribute("messages", messages);
        Mockito.verify(mockRequestDispatcher)
            .forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPost_OwnerEdit() throws IOException {
      User mockOwner = Mockito.mock(User.class);
      String ownerName = "ownerName";
      Mockito.when(mockRequest.getRequestURI())
          .thenReturn("/profile/" + ownerName);
      Mockito.when(mockSession.getAttribute("user"))
          .thenReturn(ownerName);
      Mockito.when(mockUserStore.getUser(ownerName))
          .thenReturn(mockOwner);
      String newDescription = "I'm Olaf. I like warm hugs.";
      String cleanedNewDescription = "I'm Olaf. I like warm hugs.";
      Mockito.when(mockRequest.getParameter("description"))
          .thenReturn(newDescription);
      profileServlet.doPost(mockRequest, mockResponse);
      Mockito.verify(mockOwner).setDescription(cleanedNewDescription);
      Mockito.verify(mockResponse).sendRedirect("/profile/" + ownerName);
    }

    @Test
    public void testDoPost_UserNotLoggedIn() throws IOException {
      Mockito.when(mockRequest.getRequestURI())
          .thenReturn("/profile/" + "ownerName");
      Mockito.when(mockSession.getAttribute("user"))
          .thenReturn(null);
      profileServlet.doPost(mockRequest, mockResponse);
      Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoPost_UserNotOwner() throws IOException {
      Mockito.when(mockRequest.getRequestURI())
          .thenReturn("/profile/" + "ownerName");
      Mockito.when(mockSession.getAttribute("user"))
          .thenReturn("notOwnerName");
      profileServlet.doPost(mockRequest, mockResponse);
      Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoPost_InvalidUser() throws IOException {
      String ownerName = "ownerName";
      Mockito.when(mockRequest.getRequestURI())
          .thenReturn("/profile/" + ownerName);
      Mockito.when(mockSession.getAttribute("user"))
          .thenReturn(ownerName);
      Mockito.when(mockUserStore.getUser(ownerName))
          .thenReturn(null);
      profileServlet.doPost(mockRequest, mockResponse);
      Mockito.verify(mockResponse).sendRedirect("/login");
    }

    @Test
    public void testDoPost_CleansHtmlContent() throws IOException {
      User mockOwner = Mockito.mock(User.class);
      String ownerName = "ownerName";
      Mockito.when(mockRequest.getRequestURI())
          .thenReturn("/profile/" + ownerName);
      Mockito.when(mockSession.getAttribute("user"))
          .thenReturn(ownerName);
      Mockito.when(mockUserStore.getUser(ownerName))
          .thenReturn(mockOwner);
      String newDescription =
          "<p>I'm <strong>Olaf</strong>.</p></br></br>"
          + "<script>JavaScript</script>"
          + "<div> I like warm hugs.</div>";
      String cleanedNewDescription = "I'm Olaf. I like warm hugs.";
      Mockito.when(mockRequest.getParameter("description"))
          .thenReturn(newDescription);
      profileServlet.doPost(mockRequest, mockResponse);
      Mockito.verify(mockOwner).setDescription(cleanedNewDescription);
      Mockito.verify(mockResponse).sendRedirect("/profile/" + ownerName);
    }
}
