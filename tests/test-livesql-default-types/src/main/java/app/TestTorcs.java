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

import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;
import app.daos.primitives.InvoiceDAO;
import app.daos.primitives.InvoiceDAO.InvoiceTable;

//@Configuration
//@SpringBootApplication
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
  private LiveSQL sql;

  @Autowired
  private Torcs torcs;

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
        String plan = this.torcs.getEstimatedExecutionPlan(re.getSlowestExecution());
        System.out.println("Plan:\n" + plan);
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

    InvoiceTable i = InvoiceDAO.newTable("i");
    BranchTable b = BranchDAO.newTable("b");
    InvoiceTable j = InvoiceDAO.newTable("j");
    InvoiceTable k = InvoiceDAO.newTable("k");

    this.sql.select() //
        .from(i) //
        .leftJoin(b, b.id.eq(i.branchId)) //
        .where(i.branchId.notIn( //
            this.sql.select(sql.min(j.branchId)).from(j) //
                .where(j.type.ne("VIP")
                    .and(j.amount.gtAny(this.sql.select(k.amount).from(k)
                        .where(k.accountId.ne(i.accountId).and(k.accountId.ne(j.accountId)))))))
            .or(i.amount.ge(b.id.mult(1000).minus(b.id)))) //
        .execute();

    this.sql.select(sql.literal(3).mult(7)).execute();
  }

}
