package app.contextlistener;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import app.Beans;

public class WebApplicationListener implements ServletContextListener {

//  private static AnnotationConfigApplicationContext context;

  public void contextInitialized(final ServletContextEvent event) {
    System.out.println(">>> CONTEXT INIT");
    try {
      Beans.initialize();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void contextDestroyed(final ServletContextEvent event) {
    System.out.println(">>> CONTEXT DESTROYED");
    Beans.close();
  }

}
