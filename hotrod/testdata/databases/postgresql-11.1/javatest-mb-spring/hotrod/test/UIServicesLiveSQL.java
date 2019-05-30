package hotrod.test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.queries.select.SelectLimitPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.AccountDAO.AccountTable;
import hotrod.test.generation.primitives.ItemDAO;
import hotrod.test.generation.primitives.ItemDAO.ItemTable;

@Component("uiServices2")
public class UIServicesLiveSQL {

  @Autowired
  private LiveSQL sql;

  public void runLiveSQL2() throws SQLException {

    AccountTable a = AccountDAO.newTable("a");
    AccountTable b = AccountDAO.newTable("b");

    String pattern = "" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789" //
        + "C'HK";

    SelectLimitPhase<Map<String, Object>> q = sql //
        .select(a.name, a.currentBalance) //
        .from(a) //
        .where(a.name.like(pattern + "%")) //
        .limit(2);

    String preview = q.getPreview();
    System.out.println("PREVIEW=" + preview);

    List<Map<String, Object>> rows = q.execute();

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

  public void runLiveSQL3Binary() throws SQLException {

    ItemTable i = ItemDAO.newTable("i");

    int bytes = 160;
    byte[] icon = new byte[bytes];
    for (int b = 0; b < bytes; b++) {
      icon[b] = (byte) b;
    }

    SelectLimitPhase<Map<String, Object>> q = sql //
        .select(i.id, i.description) //
        .from(i) //
        .where(i.icon.eq(icon)) //
        .limit(2);

    String preview = q.getPreview();
    System.out.println("PREVIEW=" + preview);

    List<Map<String, Object>> rows = q.execute();

    if (rows != null) {
      for (Map<String, Object> r : rows) {
        System.out.println("row: " + r);
      }
    }

  }

}
