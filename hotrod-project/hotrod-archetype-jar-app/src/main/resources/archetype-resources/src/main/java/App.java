package ${package};

import java.sql.SQLException;

public class App {

  public static void main(final String[] args) throws SQLException {
    System.out.println("[ Starting example ]");
    ExampleBean ex = SpringBeanRetriever.getBean("exampleBean");
    ex.execute();
    System.out.println("[ Example complete ]");
  }

}
