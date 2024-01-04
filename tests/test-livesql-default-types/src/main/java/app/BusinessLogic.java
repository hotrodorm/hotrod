package app;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.DMLQuery;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.daos.primitives.InvoiceDAO;
import app.daos.primitives.InvoiceDAO.InvoiceTable;

@Component
public class BusinessLogic {

  @Autowired
  private LiveSQL sql;

  @Transactional
  public void forUpdate() {

    InvoiceTable i = InvoiceDAO.newTable("i");

    Select<Row> q = this.sql //
        .select() //
        .from(i) //
        .where(i.status.eq("UNPAID")) //
        .orderBy(i.amount) //
        .forUpdate();

    System.out.println(q.getPreview());

    System.out.println("rows:");
    q.execute().forEach(r -> System.out.println("invoice: " + r));

    DMLQuery u = this.sql.update(i).set(i.unpaidBalance, i.unpaidBalance.minus(1)).where(i.status.eq("UNPAID"));
    System.out.println(u.getPreview());

    int modified = u.execute();

    System.out.println("> " + modified + " modified rows:");

  }

}
