package app;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class App {

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting... ]");
      test();
      System.out.println("[ Ending ]");
    };
  }

  private void test() throws SQLException {
    try (Connection conn = this.dataSource.getConnection()) {
      DatabaseMetaData dm = conn.getMetaData();
      System.out.println("Connected to database: " + dm.getDatabaseProductName());
    }
  }

}
