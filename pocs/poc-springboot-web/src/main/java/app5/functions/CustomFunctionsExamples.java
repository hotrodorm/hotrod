package app5.functions;

import java.sql.Timestamp;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomFunctionsExamples {

  @Autowired
  PostgreSQL12Functions pg;

  @Autowired
  private LiveSQL sql;

  @Transactional
  public double useRandom() {
    return (double) sql.select(pg.random().as("v")).execute().get(0).get("v");
  }

  @Transactional
  public double useSin() {
    return (double) sql.select(pg.sin(1).as("v")).execute().get(0).get("v");
  }

  @Transactional
  public String useLeft() {
    return (String) sql.select(pg.left("abcdef", 3).as("v")).execute().get(0).get("v");
  }

  @Transactional
  public String useFormat() {
    return (String) sql.select(pg.format("Hello %s", sql.val("World")).as("v")).execute().get(0).get("v");
  }

  @Transactional
  public Timestamp useLocaltimestamp() {
    return (Timestamp) sql.select(pg.localtimestamp().as("v")).execute().get(0).get("v");
  }

  @Transactional
  public byte[] useSetByte() {
    byte[] data = new byte[] { 100, 101, 102 };
    return (byte[]) sql.select(pg.set_byte(data, 1, 3).as("v")).execute().get(0).get("v");
  }
}
