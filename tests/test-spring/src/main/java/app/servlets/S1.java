package app.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.hotrod.runtime.livesql.Row;

import app.Beans;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.EmployeeDAO;
import app.daos.primitives.EmployeeDAO.EmployeeTable;

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

//    basicSQL();
    crud();

    resp.sendRedirect(req.getContextPath() + "/page2.html");
  }

  private void crud() throws ServletException {
//    BranchTable b = BranchDAO.newTable("b");
//    Beans.getBranchDAO().select(b, b.typ)
    
    

    EmployeeTable e = EmployeeDAO.newTable("e");
    BranchTable b = BranchDAO.newTable("b");
    
    List<Row> rows = Beans.liveSQL()
      .select(e.star(), b.name.as("branchName"))
      .from(e)
      .join(b, b.id.eq(e.branchId))
      .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7)))
      .orderBy(b.name, e.lastName.desc())
      .execute();

    rows.stream().forEach(r -> System.out.println(r));
  }

  private void basicSQL() throws ServletException {
    DataSource ds = Beans.dataSource();
    try {
      ResultSet rs = ds.getConnection().createStatement().executeQuery("select count(*) from branch");
      while (rs.next()) {
        System.out.println(">> servlet: rs=" + rs.getString(1));
      }
    } catch (SQLException e) {
      throw new ServletException(e);
    }
  }

}
