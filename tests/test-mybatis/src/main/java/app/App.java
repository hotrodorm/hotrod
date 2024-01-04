package app;

import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan
@MapperScan(basePackageClasses = App.class)
public class App implements CommandLineRunner {

  @Autowired
  private MyMapper mm;

  @Override
  public void run(String... args) throws Exception {
    Map<String, Object> row = mm.getDT();
    for (String name : row.keySet()) {
      Object value = row.get(name);
      System.out.println(name + "=" + value + " (" + (value == null ? "null" : value.getClass().getName()) + ")");
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}