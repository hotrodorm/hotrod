package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.InvoiceDAO;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.Invoice;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoTypeSolver1();
    };
  }

  private void demoTypeSolver1() {
    Invoice inv = this.invoiceDAO.select(11);
    System.out.println("Invoice #10: " + inv);
  }

}
