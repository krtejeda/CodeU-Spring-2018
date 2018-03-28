package codeu.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class RegisterServletTest {

  private RegisterServlet registerServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  @Before
  public void setup() {
    registerServlet = new RegisterServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);

    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/register.jsp")).thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    registerServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_BadUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("bad !@#$% username");

    registerServlet.doPost(mockRequest,mockResponse);

    Mockito.verify(mockRequest).setAttribute("error","Please enter only letters, numbers, and spaces.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest,mockResponse);
  }

  @Test
  public void testDoGet_ExistingUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("existing username");
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("existing username")).thenReturn(true);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest,mockResponse);

    Mockito.verify(mockRequest).setAttribute("error","That username is already taken.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest,mockResponse);
  }

  @Test
  public void testDoGet_NewUsername() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn("new username");
    Mockito.when(mockRequest.getParameter("password")).thenReturn("password");
    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered("new username")).thenReturn(false);
    registerServlet.setUserStore(mockUserStore);

    registerServlet.doPost(mockRequest,mockResponse);

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

    Mockito.verify(mockUserStore).addUser(userArgumentCaptor.capture());
    Assert.assertEquals(userArgumentCaptor.getValue().getName(),"new username");
    Assert.assertEquals(userArgumentCaptor.getValue().getPassword(),"password");

    Mockito.verify(mockResponse).sendRedirect("/login");

  }
}
