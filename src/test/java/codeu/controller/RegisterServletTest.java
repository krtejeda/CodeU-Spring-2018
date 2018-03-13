package codeu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RegisterServletTest {

  private RegisterServlet registerServlet;
  private HttpServletRequest mockRequest;
  private PrintWriter mockPrintWriter;
  private HttpServletResponse mockResponse;


  @Before
  public void setup() throws IOException {
    registerServlet = new RegisterServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockPrintWriter = Mockito.mock(PrintWriter.class);
    mockResponse = Mockito.mock(HttpServletResponse.class);

    Mockito.when(mockResponse.getWriter()).thenReturn(mockPrintWriter);
  }

  @Test
  public void testDoGet() throws IOException, ServletException {
    registerServlet.doGet(mockRequest,mockResponse);

    Mockito.verify(mockPrintWriter).println("<h1>RegisterServlet GET request.</h1>");
  }
}
