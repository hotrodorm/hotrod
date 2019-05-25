package examples.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.FederalBranchDAO;
import hotrod.test.generation.primitives.FederalBranchDAO.FederalBranchTable;
import hotrod.test.generation.primitives.TransactionDAO;
import hotrod.test.generation.primitives.TransactionDAO.TransactionTable;

@Component("joinsExamples")
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
        .execute();

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
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

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
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

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
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void crossJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // CROSS JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .crossJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void naturalJoin() {

    // SELECT a.id, t.amount
    // FROM account a
    // NATURAL JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .naturalJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void unionJoin() {

    // Only HyperSQL supports (afaik) this SQL standard clause

    // SELECT a.id, t.amount
    // FROM account a
    // UNION JOIN transaction t

    AccountTable a = AccountDAO.newTable("a");
    TransactionTable t = TransactionDAO.newTable("t");

    List<Map<String, Object>> rows = sql //
        .select(a.id, t.amount) //
        .from(a) //
        .unionJoin(t) //
        // rest of SELECT here: where(), groupBy(), etc.
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
