package app;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence1.dao.AccountDAO;
import app.persistence1.dao.AccountDAO.AccountTable;
import app.persistence1.model.Account;
import app.persistence2.dao.InvoiceDAO;
import app.persistence2.dao.InvoiceDAO.InvoiceTable;
import app.persistence2.model.Invoice;

@Configuration
@SpringBootApplication
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  @Qualifier("liveSQL:accounting") // bean name defined in DataSourceConfig1.java
  private LiveSQL sql1;

  @Autowired
  @Qualifier("liveSQL:sales") // bean name defined in DataSourceConfig2.java
  private LiveSQL sql2;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("[ Starting example ]");
      searching();
      System.out.println("[ Example complete ]");
    };
  }

  private void searching() throws DynamicExpressionException, SQLException {

    System.out.println();

    // Use a DAO to search in datasouce #1
    {
      Account a = this.accountDAO.select(2);
      System.out.println("1. Account #2: " + a);
      System.out.println();
    }

    // Use a DAO to search in datasouce #2
    {
      Invoice i = this.invoiceDAO.select(103);
      System.out.println("2. Invoice #103: " + i);
      System.out.println();
    }

    // Use LiveSQL to search in datasource #1
    {
      AccountTable a = this.accountDAO.newTable();
      List<Row> rows = this.sql1.select().from(a).where(a.owner.like("%m%")).execute();
      System.out.println("3. Account which owner names' have an 'm':");
      for (Row r : rows) {
        System.out.println(r);
      }
      System.out.println();
    }

    // Use LiveSQL to search in datasource #2
    {
      InvoiceTable i = this.invoiceDAO.newTable();
      List<Row> rows = this.sql2.select().from(i).where(i.amount.ge(300)).execute();
      System.out.println("4. Invoices for more than $300:");
      for (Row r : rows) {
        System.out.println(r);
      }
      System.out.println();
    }

  }

}
