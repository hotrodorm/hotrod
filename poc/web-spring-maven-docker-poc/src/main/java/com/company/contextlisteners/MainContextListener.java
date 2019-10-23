package com.company.contextlisteners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.company.utils.EnvironmentValidator;

public class MainContextListener implements ServletContextListener {

  private static final String APP_NAME = "Artifact #1";

  public void contextInitialized(final ServletContextEvent event) {
    String ctx = event.getServletContext().getContextPath();
    display("");
    display("--- " + APP_NAME + " Web Application Starting... context=" + ctx + " ---");

    EnvironmentValidator.validate();
    display("> Initialized Successfully: " + EnvironmentValidator.initializedSuccessfully());
    display(">            Error Message: " + EnvironmentValidator.getErrorMessage());
  }

  public void contextDestroyed(final ServletContextEvent event) {
    display("");
    display("--- " + APP_NAME + " Web Application Shutting down... ---");
    display("");
  }

  // Helpers

  public static void display(final String txt) {
    System.out.println(txt);
  }

}
