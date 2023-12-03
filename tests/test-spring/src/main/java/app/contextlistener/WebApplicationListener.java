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
      initializeSpringBeans();
    } catch (SQLException e) {
      e.printStackTrace();
    }

//    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, event.getServletContext());

  }

//  public static <T> T getBean(final String name, final Class<T> c) {
//    return context.getBean(c);
//  }

  private void initializeSpringBeans() throws SQLException {
    Beans.initialize();
//    context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//    Hello h = context.getBean("hello", Hello.class);
//    h.sayHello();
//    Bye b = context.getBean("bye", Bye.class);
//    b.sayBye();
//
//    ResultSet rs = h.getDataSource().getConnection().createStatement().executeQuery("select count(*) from branch");
//    while (rs.next()) {
//      System.out.println("rs=" + rs.getString(1));
//    }
  }

  public void contextDestroyed(final ServletContextEvent event) {
    System.out.println(">>> CONTEXT DESTROYED");
    Beans.close();
  }

}
