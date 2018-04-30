package codeu.controller;

import codeu.model.data.user.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet class responsible for forget/reset password page
 * @author My Nguyen (mnguyen@codeustudents.com)
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
   * Sets the UserStore used by this servlet. This function provides a common
   * setup method for use by the test framework or the servlet's init()
   * function.
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
    request.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp")
        .forward(request,response);
  }

  /**
   * This function fires when user submits a form to reset password.
   * It checks if the username is registered and allows user to change password if so.
   * Otherwise, show an error to the user.
   * // TODO (My): Add verification method.
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    String username = request.getParameter("username");
    String newPassword = request.getParameter("password");

    if(userStore.isUserRegistered(username)) {
      User user = userStore.getUser(username);
      if(userStore.updateUserPassword(user, newPassword)) {
        user.setPassword(newPassword);
        response.sendRedirect("/login");
      } else {
        request.setAttribute("UpdatePasswordError",
            "Failure to reset password. Please try again later.");
        request.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp")
            .forward(request, response);
      }
    } else {
      request.setAttribute("error", "Username was not found");
      request.getRequestDispatcher("/WEB-INF/view/forgetpassword.jsp")
          .forward(request, response);
    }
  }
}
