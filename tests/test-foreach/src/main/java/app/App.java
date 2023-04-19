package app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.EVO;
import app.daos.PlayerMODEL;
import app.daos.primitives.EmployeeDAO;
import app.daos.primitives.PlayerDAO;
import app.daos.primitives.QueriesDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  @Autowired
  private QueriesDAO queriesDAO;

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private PlayerDAO playerDAO;

  @Autowired
  private LiveSQL sql;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      converterPGArray();
//      converter();
//      forEach();
      System.out.println("[ Example complete ]");
    };
  }

  private void converterPGArray() {
    PlayerMODEL p = this.playerDAO.select(101);
    System.out.println("> Player:" + p.getId() + " cards="
        + Stream.of(p.getCards()).map(c -> "" + c).collect(Collectors.joining("|")));
    p.setCards(new Integer[] { 7, 8, 8, 9 });
    this.playerDAO.update(p);
    System.out.println("> Player updated.");
  }

  private void converter() {
    System.out.println("Employee:" + this.employeeDAO.select(123));
  }

  private void forEach() {

    List<Integer> ids = Arrays.asList(101, 102, 200);
    List<String> names = Arrays.asList("Alice", "Steve");

    List<EVO> evos = this.queriesDAO.findEmployees(ids, names);
    System.out.println("EVOs:");
    for (EVO evo : evos) {
      System.out.println(evo);
    }

  }

}
