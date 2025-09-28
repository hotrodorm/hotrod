package app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.AccountDAO;
import app.persistence.dao.PaymentDAO;
import app.persistence.model.Account;
import app.persistence.model.BigInvoice;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoTypeSolver1();
      demoTypeSolver2();
    };
  }

  private void demoTypeSolver1() {
    Account acc = this.accountDAO.select(4);
    System.out.println("1. Account #4: " + acc);
  }

  private void demoTypeSolver2() {
    List<BigInvoice> bi = this.paymentDAO.getBigInvoices();
    for (BigInvoice i : bi) {
      System.out.println("2. Big Invoice: " + i);
    }
  }

}
