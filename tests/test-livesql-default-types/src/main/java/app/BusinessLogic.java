package app;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.DMLQuery;
import org.hotrod.runtime.livesql.queries.select.EntitySelect;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.daos.reporting.InvoiceVO;
import app.daos.reporting.primitives.InvoiceDAO;
import app.daos.reporting.primitives.InvoiceDAO.InvoiceTable;

@Component
public class BusinessLogic {

  @Autowired
  @Qualifier("liveSQL1")
  private LiveSQL sql;

  @Autowired
  private InvoiceDAO invoiceDAO;

  @Transactional
  public void forUpdate() {
//    generic();
    selectByCriteria();
  }

  private void generic() {
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

  private void selectByCriteria() {
    InvoiceTable i = InvoiceDAO.newTable("i");

    EntitySelect<InvoiceVO> q = this.invoiceDAO //
        .select(i, i.status.eq("UNPAID")) //
        .orderBy(i.amount).offset(3).limit(1).forUpdate();

    System.out.println(q.getPreview());
    System.out.println("rows:");
    q.execute().forEach(r -> System.out.println("invoice: " + r));

    DMLQuery u = this.sql.update(i).set(i.unpaidBalance, i.unpaidBalance.minus(1)).where(i.status.eq("UNPAID"));
    System.out.println(u.getPreview());

    int modified = u.execute();
    System.out.println("> " + modified + " modified rows:");
  }

}
