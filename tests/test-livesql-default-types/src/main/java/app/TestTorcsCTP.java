package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.spring.SpringBeanObjectFactory;
import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.ctp.TorcsCTP;
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
public class TestTorcsCTP {

  public static void main(String[] args) {
    SpringApplication.run(TestTorcsCTP.class, args);
  }

  @Autowired
  private LiveSQL sql;

  @Autowired
  private Torcs torcs;

  @Autowired
  private TorcsCTP torcsCTP;

  @Autowired
  private DataSource ds;

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Torcs Starting example ]");

//      this.executeSampleSql();
      this.executeSelect();

      System.out.println("-- Ranking ---");
      for (RankingEntry re : this.torcs.getDefaultRanking().getEntries()) {
        System.out.println(re);
//        String plan = this.torcs.getEstimatedExecutionPlan(re.getSlowestExecution());
        List<String> ctp = this.torcsCTP.getEstimatedCTPExecutionPlan(re.getSlowestExecution());
        System.out.println("CTP Plan:\n");
        ctp.stream().forEach(l -> System.out.println(l));
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

  private void executeSelect() {
    this.sql.select(sql.literal(3).mult(7)).execute();
  }

}
