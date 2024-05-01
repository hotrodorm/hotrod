package app.test.types;

import java.time.LocalDateTime;

public class EmployeeEntity {

  public final TimestampExpression<LocalDateTime, LocalDateTime, NoConverter<LocalDateTime>> hired;

  public final TimestampExpression<LocalDateTime, String, STConverter> letGo;

  public EmployeeEntity() {
    this.hired = new TimestampExpression<>();
    this.letGo = new TimestampExpression<>();
  }

}
