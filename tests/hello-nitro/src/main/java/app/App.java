package app;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.persistence.dao.AccountDAO;
import app.persistence.dao.ReportingDAO;
import app.persistence.model.Account;
import app.persistence.model.ReportingTotals;

@SpringBootApplication
@Configuration
public class App {

  @Autowired
  private AccountDAO accountDAO;

  @Autowired
  private ReportingDAO reportingDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoNitro1();
      demoNitro2();
      demoNitro3();
      demoNitro4();
    };
  }

  private void demoNitro1() {
    int count = this.accountDAO.applyMonthlyCharge(15);
    System.out.println("Montly charge applied to " + count + " account(s).");
  }

  private void demoNitro2() {
    List<Account> accounts = this.accountDAO.findSavingAccounts(2024);
    for (Account a : accounts) {
      System.out.println("Saving account:" + a);
    }
  }

  private void demoNitro3() {
    int count = this.reportingDAO.deleteOldNegativeAccounts(2015);
    System.out.println("Deleted a total " + count + " old account(s).");
  }

  private void demoNitro4() {
    ReportingTotals totals = this.reportingDAO.getTotals(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 30));
    System.out.println("September 2025: " + totals.getCount() + " accounts, $" + totals.getBalance() + " balance.");
  }

}
