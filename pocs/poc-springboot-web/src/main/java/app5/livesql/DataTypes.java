package app5.livesql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app5.persistence.primitives.ItemDAO;
import app5.persistence.primitives.ItemDAO.ItemTable;

@Component("dataTypesExamples")
public class DataTypes {

  @Autowired
  private LiveSQL sql;

  public void filter() {

    ItemTable i = ItemDAO.newTable("i");

    int minId = 123;
    String keyword = "mountain";
    double maxPrice = 46.25;

    Date fromDate;
    try {
      fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-25");
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    boolean active = true;

    List<Row> rows = sql //
        .select() //
        .from(i) //
        .where( //
            i.id.ge(minId) //
                .and(i.description.like("%" + keyword + "%")) //
                .and(i.createdOn.gt(fromDate)) //
                .and(i.price.le(maxPrice)) //
                .and(i.active.eq(active)) //
        ) // ... and rest of SQL
        .execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}
