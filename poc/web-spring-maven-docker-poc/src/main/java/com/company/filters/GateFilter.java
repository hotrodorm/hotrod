package com.company.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.utils.EnvironmentValidator;

public class GateFilter implements Filter {

  private static final Logger log = LogManager.getLogger(GateFilter.class);

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    log.trace("init()");
  }

  @Override
  public void destroy() {
    log.trace("destroy()");
  }

  @Override
  public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {

    if (EnvironmentValidator.initializedSuccessfully()) {
      chain.doFilter(request, response);
    } else {
      response.getWriter()
          .write("<html><body>" + "<h1>Web Application - Initialization Error</h1>"
              + "<p>The application didn't start up properly due to:</p>" + "<pre>"
              + EnvironmentValidator.getErrorMessage() + "</pre>" + "</body></html>");
      log.error("LZ Controller Dashboard web application did not start properly.");
    }

  }

}
