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

@WebServlet("/t1")
public class Transaction extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println(">>> servlet");
    tx();
    resp.sendRedirect(req.getContextPath() + "/page2.html");
  }

  private void tx() throws ServletException {

    try {
    Beans.logic().updateBranches();
    } catch (RuntimeException e) {
      System.out.println("TX rolled back -- continue");
    }

    System.out.println("--- Listing ---");
    BranchTable b = BranchDAO.newTable("b");
    List<Row> rows = Beans.liveSQL() //
        .select() //
        .from(b) //
        .execute();
    rows.stream().forEach(r -> System.out.println(r));

  }

}
