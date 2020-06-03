package ${package};

import java.sql.SQLException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("exampleBean")
public class ExampleBean {

  @Transactional
  public void execute() throws SQLException {

    // Use DAOs to retrieve and save to the database

  }

}
