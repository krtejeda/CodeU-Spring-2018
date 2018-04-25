package codeu.controller;

import codeu.model.data.UserGroup;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.store.basic.UserStore;
import codeu.model.data.User;
import java.util.UUID;
import java.time.Instant;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet class responsible for user registration.
 */
public class RegisterServlet extends HttpServlet {

  /**
   * Store class that gives access to Users.
   */
  private UserStore userStore;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    request.getSession().removeAttribute("user");
    request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    String username = request.getParameter("username");
    String passwordHash = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt());

    if(!username.matches("[\\w*\\s*]*")) {
      request.setAttribute("error", "Please enter only letters, numbers, and spaces.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }

    if(userStore.isUserRegistered(username)) {
      request.setAttribute("error", "That username is already taken.");
      request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
      return;
    }
    User user =
        new User(
            UUID.randomUUID(),
            username,
            passwordHash,
            Instant.now(),
            UserGroup.REGULAR_USER);

    // set first registered user as root
    if (userStore.getUsersCount() == 0) {
      user.setGroup(UserGroup.ROOT);
    }

    userStore.addUser(user);

    response.sendRedirect("/login");
  }

  /**
   * Set up state for handling registration-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a comoon setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
}
