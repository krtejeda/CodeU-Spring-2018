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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProfileServletTest {

    private ProfileServlet profileServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockRequestDispatcher;
    private ConversationStore mockConversationStore;
    private MessageStore mockMessageStore;
    private UserStore mockUserStore;

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
        Mockito
            .when(mockMessageStore.getMessagesInConversation(conversation.getId()))
            .thenReturn(Lists.newArrayList(message));

        profileServlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockRequest)
            .setAttribute("conversations", conversations);
        Mockito.verify(mockRequest)
            .setAttribute("owner", user);
        Mockito.verify(mockRequestDispatcher)
            .forward(mockRequest, mockResponse);
    }
}
