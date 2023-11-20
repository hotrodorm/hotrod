package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.rankings.RankingEntry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
@MapperScan("mappers")
@ComponentScan(basePackageClasses = SpringBeanObjectFactory.class)
@PropertySource(value = { "file:application.properties",
    "classpath:application.properties" }, ignoreResourceNotFound = true)
public class TestTorcs {

  public static void main(String[] args) {
    SpringApplication.run(TestTorcs.class, args);
  }

  @Autowired
  private Torcs torcs;

  @Autowired
  private DataSource ds;

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Torcs Starting example ]");

      this.executeSampleSql();

      System.out.println("-- Ranking ---");
      for (RankingEntry re : this.torcs.getDefaultRanking().getRanking()) {
        System.out.println(re);
      }
      System.out.println("-- End of Ranking ---");

      System.out.println("[ Torcs Example complete ]");
    };
  }

  private void executeSampleSql() throws SQLException {
    Connection conn = ds.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("select 3 * 7");
    while (rs.next()) {
      System.out.println("line: " + rs.getString(1));
    }

  }

}
