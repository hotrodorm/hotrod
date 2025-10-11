package app;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.LiveSQL;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression.DateTimeField;
import org.hotrod.livesql.queries.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.AccountDAO;
import app.persistence.dao.InvoiceDAO;
import app.persistence.dao.InvoiceDAO.InvoiceTable;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.Account;
import app.persistence.model.BigInvoice;

@SpringBootApplication
@Configuration
public class App {

  private static final Logger log = Logger.getLogger(App.class.getName());

  private static final LiveSQLLogging LIVESQLLOG = LiveSQLLogging.of(() -> log.isLoggable(Level.FINE),
      msg -> log.fine(msg));

  @Autowired
  private LiveSQL sql;

  @Autowired
  private AccountDAO accountDAO;

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
//      demoTypeSolverCRUD();
//      demoTypeSolverNitroSelect();
      demoTypeSolverLiveSQL();
    };
  }

  private void demoTypeSolverCRUD() {
    Account acc = this.accountDAO.select(4);
    System.out.println("1. Account #4: " + acc);
  }

  private void demoTypeSolverNitroSelect() {
    List<BigInvoice> bi = this.paymentDAO.getBigInvoices();
    for (BigInvoice i : bi) {
      System.out.println("2. Big Invoice: " + i);
    }
  }

  private void demoTypeSolverLiveSQLOld() {
    InvoiceTable i = this.invoiceDAO.newTable();
    Select<Row> q = this.sql.select( //
        i.id, // STATIC_DIALECT_RULE
        i.created, // STATIC_TYPESOLVER_RULE (type)
        i.invoiceTaxCodes, // STATIC_TYPESOLVER_RULE (converter)
        i.amount, //
        i.paid, //
        i.amount.mult(1.30).as("ag1").type(Double.class), // RUNTIME_DESIGNATED
        sql.caseWhen(i.amount.ge(500), "Y").elseValue("N").end().as("vip").type(this.ynBooleanConverter), //
        i.created.extract(DateTimeField.DAY).as("dom"), // RUNTIME_TYPESOLVER_RULE
        sql.caseWhen(i.paid.eq("Y"), i.amount).elseValue(0).end().as("paidAmount"), // RUNTIME_DIALECT_RULE
        h2.randomUUID().as("globalId") // RUNTIME_JDBC_DRIVER_DEFAULT
    ).from(i) //
        .where(i.id.eq(11));
//    System.out.println("query=" + q.getPreview(true));
    List<Row> rows = q.execute(LIVESQLLOG);
    for (Row r : rows) {
      System.out.println("r=" + r);
    }
    int count = this.sql.insert(i).columns(i.id).values(sql.val(123)).execute(LIVESQLLOG);
    System.out.println("inserted: " + count);
    count = this.invoiceDAO.delete(i, i.id.eq(1234)).execute(LIVESQLLOG);
    System.out.println("deleted: " + count);
  }
  

  private void demoTypeSolverLiveSQL() {
    InvoiceTable i = this.invoiceDAO.newTable();
    
//    select
//    i.amount // static designated class
//    i.status // static designated converter
//    i.created // static typesolver class
//    i.active // static typesolver converter
//    i.category // static dialect rule
//
//    i.amount.mult(1.30).as("gross").type(Double.class) // runtime designated class
//    sql.caseWhen(i.amount.ge(500), "Y").elseValue("N").end().as("vip").type(this.ynConverter) // runtime designated converter
//    i.created.extract(DateTimeField. DAY ).as("dom") // runtime typesolver class
//    i.expiration_jdn // runtime typesolver converter
//    sql.caseWhen(i.paid.eq("Y"), i.amount).elseValue(0).end().as("paidAmount"), // RUNTIME_DIALECT_RULE
//    h2.randomUUID().as("globalId") // RUNTIME_JDBC_DRIVER_DEFAULT


    
    
    Select<Row> q = this.sql.select( //
        i.amount, // STATIC_DESIGNATED
        i.id, // STATIC_DIALECT_RULE
        i.created, // STATIC_TYPESOLVER_RULE (type)
        i.invoiceTaxCodes, // STATIC_TYPESOLVER_RULE (converter)
        i.paid, //
        i.amount.mult(1.30).as("ag1").type(Double.class), // RUNTIME_DESIGNATED
        sql.caseWhen(i.amount.ge(500), "Y").elseValue("N").end().as("vip").type(this.ynBooleanConverter), //
        i.created.extract(DateTimeField.DAY).as("dom"), // RUNTIME_TYPESOLVER_RULE
        sql.caseWhen(i.paid.eq("Y"), i.amount).elseValue(0).end().as("paidAmount"), // RUNTIME_DIALECT_RULE
        h2.randomUUID().as("globalId") // RUNTIME_JDBC_DRIVER_DEFAULT
    ).from(i) //
        .where(i.id.eq(11));
//    System.out.println("query=" + q.getPreview(true));
    List<Row> rows = q.execute(LIVESQLLOG);
    for (Row r : rows) {
      System.out.println("r=" + r);
    }
    int count = this.sql.insert(i).columns(i.id).values(sql.val(123)).execute(LIVESQLLOG);
    System.out.println("inserted: " + count);
    count = this.invoiceDAO.delete(i, i.id.eq(1234)).execute(LIVESQLLOG);
    System.out.println("deleted: " + count);
  }

  
}
