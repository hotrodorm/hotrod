package app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hello {

  @Autowired
  private DataSource dataSource;

  public void sayHello() {
    System.out.println("Hello!");
  }

  public DataSource getDataSource() {
    return dataSource;
  }

}
