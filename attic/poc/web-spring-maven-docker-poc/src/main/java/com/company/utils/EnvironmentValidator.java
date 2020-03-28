package com.company.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentValidator {

  private static final Logger log = LogManager.getLogger(EnvironmentValidator.class);

  private static final String JDBC_URL = "JDBC_URL";
  private static final String JDBC_USERNAME = "JDBC_USERNAME";
  private static final String JDBC_PASSWORD = "JDBC_PASSWORD";

  private static enum ApplicationState {
    UNINITIALIZED, INITIALIZED_SUCCESSFUL, INITIALIZED_FAILED
  }

  private static ApplicationState applicationState = ApplicationState.UNINITIALIZED;
  private static String errorMessage;

  public static void validate() {

    String jdbcURL = System.getenv(JDBC_URL);
    String jdbcUsername = System.getenv(JDBC_USERNAME);
    String jdbcPassword = System.getenv(JDBC_PASSWORD);

    boolean propertiesAreMissing = false;

    if (isEmpty(jdbcURL)) {
      logError("OS-level environment property " + JDBC_URL + " is empty or not set.");
      propertiesAreMissing = true;
    }

    if (isEmpty(jdbcUsername)) {
      logError("OS-level environment property " + JDBC_USERNAME + " is empty or not set.");
      propertiesAreMissing = true;
    }

    if (isEmpty(jdbcPassword)) {
      logError("OS-level environment property " + JDBC_PASSWORD + " is empty or not set.");
      propertiesAreMissing = true;
    }

    if (propertiesAreMissing) {
      logError("The web application could not start " + "since some environment properties are missing.");
      applicationState = ApplicationState.INITIALIZED_FAILED;
      return;
    }

    // Mark as successful

    applicationState = ApplicationState.INITIALIZED_SUCCESSFUL;

  }

  // Helpers

  private static void logError(final String msg) {
    log.error(msg);
    if (errorMessage == null) {
      errorMessage = msg;
    } else {
      errorMessage = errorMessage + "\n" + msg;
    }
  }

  private static boolean isEmpty(final String s) {
    return s == null || s.isBlank();
  }

  // Getters

  public static boolean initializedSuccessfully() {
    return applicationState == ApplicationState.INITIALIZED_SUCCESSFUL;
  }

  public static String getErrorMessage() {
    if (applicationState == ApplicationState.UNINITIALIZED) {
      return "Application not initialized.";
    } else if (applicationState == ApplicationState.INITIALIZED_FAILED) {
      return errorMessage;
    } else {
      return null;
    }
  }

}
