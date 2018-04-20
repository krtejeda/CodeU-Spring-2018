package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import java.time.Instant;
import java.util.UUID;

public class ForgetPasswordServletTest {

  private ForgetPasswordServlet forgetPasswordServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  private static final String TEST_USERNAME = "test username";
  private static final String TEST_PASSWORD = "original password";
  private static final String TEST_PASSWORD_HASH = BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt());
  private static final User USER = new User(
      UUID.randomUUID(),
      TEST_USERNAME,
      TEST_PASSWORD_HASH,
      Instant.now());

  @Before
  public void setup() {
    forgetPasswordServlet = new ForgetPasswordServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    forgetPasswordServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_UserNotFound() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(false);
    forgetPasswordServlet.setUserStore(mockUserStore);

    forgetPasswordServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "Username was not found");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ExistingUser() throws IOException, ServletException {
    String newPassword = "new password";
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn(newPassword);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(true);
    forgetPasswordServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(USER);
    Mockito.when(mockUserStore.updateUserPassword(USER, newPassword)).thenReturn(true);

    forgetPasswordServlet.doPost(mockRequest, mockResponse);

    Assert.assertTrue(BCrypt.checkpw(newPassword, USER.getPassword()));
    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoPost_UpdateFailure() throws IOException, ServletException {
    String newPassword = "new password";
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn(newPassword);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(true);
    forgetPasswordServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(USER);
    Mockito.when(mockUserStore.updateUserPassword(USER, newPassword)).thenReturn(false);

    forgetPasswordServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("UpdatePasswordError",
        "Failure to reset password. Please try again later.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}
