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

package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.time.Instant;
import java.util.UUID;

public class LoginServletTest {

  private LoginServlet loginServlet;
  private HttpServletRequest mockRequest;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  private static final String TEST_USERNAME = "test username";
  private static final String TEST_PASSWORD = "test password";
  private static final String TEST_PASSWORD_HASH = BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt());
  private static final User USER = new User(
      UUID.randomUUID(),
      TEST_USERNAME,
      TEST_PASSWORD_HASH,
      Instant.now());

  @Before
  public void setup() {
    loginServlet = new LoginServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/login.jsp"))
        .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockSession).removeAttribute("user");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_IncorrectPassword() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn("wrong password");

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(true);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(USER);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "Invalid password.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_MissingUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(false);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(USER);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "That username was not found.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoPost_ExistingUser() throws IOException, ServletException {
    Mockito.when(mockRequest.getParameter("username")).thenReturn(TEST_USERNAME);
    Mockito.when(mockRequest.getParameter("password")).thenReturn(TEST_PASSWORD);

    UserStore mockUserStore = Mockito.mock(UserStore.class);
    Mockito.when(mockUserStore.isUserRegistered(TEST_USERNAME)).thenReturn(true);
    loginServlet.setUserStore(mockUserStore);
    Mockito.when(mockUserStore.getUser(TEST_USERNAME)).thenReturn(USER);

    HttpSession mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    loginServlet.doPost(mockRequest, mockResponse);

    Mockito.verify(mockSession).setAttribute("user", TEST_USERNAME);
    Mockito.verify(mockResponse).sendRedirect("/conversations");
  }
}
