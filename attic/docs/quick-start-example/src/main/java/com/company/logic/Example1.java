package com.company.logic;

import java.sql.Date;
import java.sql.SQLException;

import com.company.daos.AccountDAO;
import com.company.daos.AccountDebitDAO;
import com.company.daos.BigDepositDAO;
import com.company.daos.TransactionDAO;

public class Example1 {

  public static void main(final String[] args) throws SQLException {

    // Note: this simple example does NOT use database transactions.

    Date now = new Date(System.currentTimeMillis());

    AccountDAO a = new AccountDAO();
    a.setName("CHK1067");
    a.setCurrentBalance(100);
    a.setCreatedOn(now);
    a.insert();

    TransactionDAO t = new TransactionDAO();
    t.setAccountId(a.getId());
    t.setAmount(40);
    t.setCompletedAt(now);
    t.insert();
    int tx1 = t.getId();

    t.setAmount(500);
    t.insert();
    int tx2 = t.getId();

    t.setAmount(-440);
    t.insert();
    int tx3 = t.getId();

    System.out.println("Account " + a.getId() + " created, "
        + "with transactions " + tx1 + ", " + tx2 + ", and " + tx3
        + " also created.");

    AccountDebitDAO filter = new AccountDebitDAO();
    for (AccountDebitDAO d : filter.select()) {
      System.out.println("Debit found: tx " + d.getId() 
          + " with amount $" + d.getAmount());
    }

    for (BigDepositDAO b : BigDepositDAO.select(300)) {
      System.out.println("Big deposit found: tx " + b.getId()
          + " with amount $" + b.getAmount());
    }

  }

}
