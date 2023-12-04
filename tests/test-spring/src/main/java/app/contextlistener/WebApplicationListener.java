package app.contextlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import app.Beans;

public class WebApplicationListener implements ServletContextListener {

  public void contextInitialized(final ServletContextEvent event) {
    System.out.println(">>> CONTEXT INIT");
    Beans.initialize();
  }

  public void contextDestroyed(final ServletContextEvent event) {
    System.out.println(">>> CONTEXT DESTROYED");
    Beans.close();
  }

}
