package hotrod.test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;

public class UIServicesLiveSQL {

  @Autowired
  private LiveSQL sql;

  public void runLiveSQL2() throws SQLException {

    AccountTable a = AccountDAO.newTable("a");
    AccountTable b = AccountDAO.newTable("b");
    // ClientTable c = ClientDAO.newTable("c");

    List<Map<String, Object>> rows = sql //
        .select(a.name, a.currentBalance) //
        .from(a) //
        .unionAll(sql.select(b.name, b.currentBalance).from(b).where(b.name.like("CHK%"))) //
        // .orderBy(a.currentBalance.asc())
        .limit(2) //
        .execute() //
    ;

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

}
