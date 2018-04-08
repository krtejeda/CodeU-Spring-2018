package codeu.controller;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.store.basic.UserStore;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.Instant;
import org.mindrot.jbcrypt.BCrypt;


/**
 * Servlet class responsible for user forgetting/ resetting password
 */
public class ForgetPasswordServlet extends HttpServlet {

  private UserStore userStore;

  /**
   * Set up state for handling forget password requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */

  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when user navigates to Forget Password Page
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    request.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp").forward(request,response);
  }

  /**
   * This function fires when user submits a form to reset password. It checks if the username is registered
   * and allows user to change password if it is. Otherwise, show an error to the user.
   * // TODO : Add verification method. For now it just lets anyone reset any username's password.
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    String username = request.getParameter("username");
    String newPasswordHash = BCrypt.hashpw(request.getParameter("password"),BCrypt.gensalt());

    if(userStore.isUserRegistered(username)) {
      User user = userStore.getUser(username);
      user.setPassword(newPasswordHash);
      response.sendRedirect("/login");
    } else {
      request.setAttribute("error", "That username was not found");
      request.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp").forward(request,response);
    }
  }
}
