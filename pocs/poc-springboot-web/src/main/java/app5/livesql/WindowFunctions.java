package app5.livesql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.SelectFromPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app5.persistence.primitives.IslandDAO;
import app5.persistence.primitives.IslandDAO.IslandTable;
import app5.persistence.primitives.ItemDAO;
import app5.persistence.primitives.ItemDAO.ItemTable;

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

    List<Row> rows = sql //
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

    for (Row r : rows) {
      System.out.println("row: " + r);
    }

  }

  public void windowFrames() {

    IslandTable i = IslandDAO.newTable("i");

    SelectFromPhase<Row> query = sql //
        .select(i.id, i.segment, i.xStart, i.xEnd, i.height, //
            sql.max(i.xEnd).over().partitionBy(i.segment).orderBy(i.xStart.asc(), i.id.asc()).rows()
                .betweenUnboundedPreceding().andPreceding(1).excludeNoOthers().end().as("prevEnd")) //
        .from(i);
    System.out.println(query.getPreview());

    List<Row> rows = query //
        .execute();

    for (Row r : rows) {
      System.out.println("row: " + r);
    }

  }

}
