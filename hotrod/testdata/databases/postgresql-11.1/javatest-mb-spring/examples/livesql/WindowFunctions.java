package examples.livesql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotrod.test.generation.primitives.ItemDAO;
import hotrod.test.generation.primitives.ItemDAO.ItemTable;

@Component("windowFunctions")
public class WindowFunctions {

  @Autowired
  private LiveSQL sql;

  public void aggregate() {

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

    List<Map<String, Object>> rows = sql //
        .select( //
            i.id, //
            i.storeCode, //
            i.createdOn, //
            i.price, //

            sql.max(i.price).over().partitionBy(i.price).orderBy(i.createdOn.desc()).end().as("maxP"), //
            sql.max(i.description).over().partitionBy(i.price).orderBy(i.createdOn.desc(), i.active.desc()).end()
                .as("maxD"), //
            sql.max(i.createdOn).over().end().as("maxC"), //

            sql.min(i.price).over().end().as("minP"), //
            sql.min(i.description).over().end().as("minD"), //
            sql.min(i.createdOn).over().end().as("minC") //

        ) //
        .from(i) //
        // .where( //
        // i.id.ge(minId) //
        // .and(i.description.like("%" + keyword + "%")) //
        // .and(i.createdOn.gt(fromDate)) //
        // .and(i.price.le(maxPrice)) //
        // .and(i.active.eq(active)) //
        // ) // ... and rest of SQL
        .orderBy(i.price.asc(), i.createdOn.desc()).execute();

    for (Map<String, Object> r : rows) {
      System.out.println("row: " + r);
    }

  }

}