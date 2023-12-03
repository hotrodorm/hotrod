package app.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import app.Beans;

@WebServlet("/s1")
public class S1 extends HttpServlet {

  private static final long serialVersionUID = 1L;

//  public void init(ServletConfig config) {
//    System.out.println(">>> servlet INIT 1 - config=" + config);
////    try {
////      super.init(config);
////    } catch (ServletException e) {
////      e.printStackTrace();
////    }
//    System.out.println(">>> servlet INIT 2 - config.getServletContext()=" + config.getServletContext());
//    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
//  }

//  @Autowired
//  private DataSource dataSource;

  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println(">>> servlet");

    DataSource ds = Beans.dataSource();
    try {
      ResultSet rs = ds.getConnection().createStatement().executeQuery("select count(*) from branch");
      while (rs.next()) {
        System.out.println(">> servlet: rs=" + rs.getString(1));
      }
    } catch (SQLException e) {
      throw new ServletException(e);
    }

    resp.sendRedirect(req.getContextPath() + "/page2.html");
  }

}
