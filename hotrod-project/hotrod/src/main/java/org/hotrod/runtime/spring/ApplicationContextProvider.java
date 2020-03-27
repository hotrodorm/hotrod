package org.hotrod.runtime.spring;

import org.springframework.context.ApplicationContext;

public class ApplicationContextProvider {

  private static ApplicationContext context = null;

  public static void setApplicationContext(ApplicationContext context) {
    ApplicationContextProvider.context = context;
  }

  public static ApplicationContext getApplicationContext() {
    if (ApplicationContextProvider.context == null) {
      throw new RuntimeException("Unable to get application context. Context not initialized yet!");
    }
    return context;
  }

}
