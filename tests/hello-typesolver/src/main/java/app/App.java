package app;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.InvoiceDAO;
import app.persistence.dao.InvoiceDAO.InvoiceTable;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.BigInvoice;
import app.persistence.model.Invoice;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger LOG = Logger.getLogger(App.class.getName());

  private static final LiveSQLLogging LIVESQL_LOG = LiveSQLLogging.of(() -> LOG.isLoggable(Level.FINE),
      msg -> LOG.fine(msg));

  @Autowired
  private LiveSQL sql;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  @Autowired
  private H2Functions h2;

  @Autowired
  private YNBooleanConverter ynBooleanConverter;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoTypeSolverCRUD();
      demoTypeSolverNitroSelect();
      demoTypeSolverLiveSQL();
    };
  }

  private void demoTypeSolverCRUD() {
    Invoice filter = new Invoice();
    filter.setStatus(InvoiceStatus.UNPAID);
    List<Invoice> unpaid = this.invoiceDAO.select(filter);
    for (Invoice inv : unpaid) {
      System.out.println("1. Unpaid invoice: " + inv);
    }
  }

  private void demoTypeSolverNitroSelect() {
    List<BigInvoice> bi = this.paymentDAO.getBigInvoices();
    for (BigInvoice i : bi) {
      System.out.println("2. Big Invoice: " + i);
    }
  }

  private void demoTypeSolverLiveSQL() {
    InvoiceTable i = this.invoiceDAO.newTable();
    List<Row> rows = this.sql
        .select(
          i.amount,
          i.status,
          i.created,
          i.active,
          i.category,
          i.amount.mult(1.30).as("gross").type(Double.class),
          sql.caseWhen(i.amount.ge(300), "Y").elseValue("N").end().as("vip").type(this.ynBooleanConverter),
          i.created.extract(DateTimeField.DAY).as("dom"),
          i.amount.mult(i.category).as("score_jld"),
          sql.caseWhen(i.status.eq(InvoiceStatus.PAID), i.amount).elseValue(0).end().as("paidAmount"),
          h2.randomUUID().as("globalId")
         )
        .from(i)
        .where(i.status.eq(InvoiceStatus.DRAFT))
        .execute(LIVESQL_LOG);
    for (Row r : rows) {
      System.out.println("3. row=" + r);
    }
  }

}
