package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ActivityServletTest {

  private ActivityServlet activityServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;
	private ConversationStore mockConversationStore;
  private MessageStore mockMessageStore;
  private UserStore mockUserStore;

  @Before
  public void setup() {
    activityServlet = new ActivityServlet();

    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);

    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activity.jsp"))
        .thenReturn(mockRequestDispatcher);

    mockConversationStore = Mockito.mock(ConversationStore.class);
    activityServlet.setConversationStore(mockConversationStore);

    mockMessageStore = Mockito.mock(MessageStore.class);
    activityServlet.setMessageStore(mockMessageStore);

    mockUserStore = Mockito.mock(UserStore.class);
    activityServlet.setUserStore(mockUserStore);
  }

  @Test
  public void testDoGet_AllNotEmpty() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test_conversation",
            Instant.now().plusSeconds(1)));

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now().plusSeconds(2)));

    List<User> fakeUserList = new ArrayList<>();
    fakeUserList.add(
        new User(
            UUID.randomUUID(),
            "test username",
            "test password",
            Instant.now().plusSeconds(3)));

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

    // The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeUserList);
		fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeConversationList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_ConversationEmpty() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now().plusSeconds(2)));

    List<User> fakeUserList = new ArrayList<>();
    fakeUserList.add(
        new User(
            UUID.randomUUID(),
            "test username",
            "test password",
            Instant.now().plusSeconds(3)));

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

		// The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeUserList);
		fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeConversationList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_MessageEmpty() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test_conversation",
            Instant.now().plusSeconds(1)));

    List<Message> fakeMessageList = new ArrayList<>();

    List<User> fakeUserList = new ArrayList<>();
    fakeUserList.add(
        new User(
            UUID.randomUUID(),
            "test username",
            "test password",
            Instant.now().plusSeconds(3)));

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

		// The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeUserList);
		fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeConversationList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_UserEmpty() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test_conversation",
            Instant.now().plusSeconds(1)));

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            Instant.now().plusSeconds(2)));

   List<User> fakeUserList = new ArrayList<>();

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

		// The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeUserList);
		fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeConversationList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_AllEmpty() throws IOException, ServletException {
    List<Conversation> fakeConversationList = new ArrayList<>();
    List<Message> fakeMessageList = new ArrayList<>();
    List<User> fakeUserList = new ArrayList<>();

    Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
    Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
    Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

		// The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeUserList);
		fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeConversationList);

    activityServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

	@Test
	public void testDoGet_CreationTimeEqual() throws IOException, ServletException {
		Instant now = Instant.now();
		List<Conversation> fakeConversationList = new ArrayList<>();
    fakeConversationList.add(
        new Conversation(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test_conversation",
            now));

    List<Message> fakeMessageList = new ArrayList<>();
    fakeMessageList.add(
        new Message(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            "test message",
            now));

    List<User> fakeUserList = new ArrayList<>();
    fakeUserList.add(
        new User(
            UUID.randomUUID(),
            "test username",
            "test password",
            now));

		Mockito.when(mockConversationStore.getAllConversations()).thenReturn(fakeConversationList);
		Mockito.when(mockMessageStore.getAllMessages()).thenReturn(fakeMessageList);
		Mockito.when(mockUserStore.getAllUsers()).thenReturn(fakeUserList);

		// The order the objects should be in
		List<Object> fakeActivityList = new ArrayList<>();
		fakeActivityList.addAll(fakeConversationList);
    fakeActivityList.addAll(fakeMessageList);
		fakeActivityList.addAll(fakeUserList);

		activityServlet.doGet(mockRequest, mockResponse);

		Mockito.verify(mockRequest).setAttribute("activity", fakeActivityList);
		Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
	}
}
