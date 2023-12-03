package app.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hotrod.runtime.livesql.Row;

import app.Beans;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.EmployeeDAO;
import app.daos.primitives.EmployeeDAO.EmployeeTable;

@WebServlet("/s1")
public class S1 extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println(">>> servlet");
    crud();
    resp.sendRedirect(req.getContextPath() + "/page2.html");
  }

  private void crud() throws ServletException {

    EmployeeTable e = EmployeeDAO.newTable("e");
    BranchTable b = BranchDAO.newTable("b");

    List<Row> rows = Beans.liveSQL() //
        .select(e.star(), b.name.as("branchName")) //
        .from(e) //
        .join(b, b.id.eq(e.branchId)) //
        .where(e.lastName.lower().like("%smith%").and(b.type.in(2, 6, 7))) //
        .orderBy(b.name, e.lastName.desc()) //
        .execute();

    rows.stream().forEach(r -> System.out.println(r));

  }

}
