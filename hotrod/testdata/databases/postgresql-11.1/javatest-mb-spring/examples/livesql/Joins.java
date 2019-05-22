package examples.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.FederalBranchDAO;
import hotrod.test.generation.primitives.FederalBranchDAO.FederalBranchTable;
import hotrod.test.generation.primitives.TransactionDAO;
import hotrod.test.generation.primitives.TransactionDAO.TransactionTable;

@Service("joinsExamples")
public class Joins {

  @Autowired
  private LiveSQL sql;

  public void innerJoin() {

    // SELECT a.id, a.name as "accountName", t.amount, b.name as "branchName"
    // FROM account a
    // JOIN transaction t on t.account_id = a.id
    // JOIN federal_branch b on b.id = t.fed_branch_id
    // WHERE a.type = 'CHK'

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");
    FederalBranchTable b = FederalBranchDAO.newTable("b");

    List<Map<String, Object>> rows = sql //
        .select(a.id, a.name.as("accountName"), t.amount, b.name.as("branchName")) //
        .from(a) //
        .join(t, t.accountId.eq(a.id)) //
        .join(b, b.id.eq(t.fedBranchId)) //
        .where(a.type.eq("CHK")) // ... and rest of SQL
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void leftOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // LEFT JOIN transaction t on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .leftJoin(t, t.accountId.eq(a.id)) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void rightOuterJoin() {

    // SELECT a.id, t.amount
    // FROM transaction t
    // RIGHT JOIN account a on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(t) //
        .rightJoin(a, t.accountId.eq(a.id)) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void fullOuterJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // FULL OUTER JOIN transaction t on t.account_id = a.id

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .fullOuterJoin(t, t.accountId.eq(a.id)) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void crossJoin() {

    // SELECT a.id, t.amount
    // FROM transaction t
    // CROSS JOIN account a

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .crossJoin(t) //
        .execute() //
    ;

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
