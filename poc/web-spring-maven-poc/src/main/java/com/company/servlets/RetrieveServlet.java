package com.company.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.utils.SpringBeanRetriever;

public class RetrieveServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(RetrieveServlet.class);

  public RetrieveServlet() {
    super();
  }

  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    log.info("starting...");
    response.getWriter()
        .append("<html><body><h1>Artifact #1</h1>" + "<p>Page served at " + request.getContextPath() + "</p>");

    Queries queries = SpringBeanRetriever.getBean("queries");
    int rows = queries.runLiveSQL();
    response.getWriter().append("<p>Rows: " + rows + "</p></body></html>");

  }

  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

}
