package app.test.functions;

import java.time.LocalDate;

import org.hotrod.livesql.expressions.Function;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFunction;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.numeric.NumericFunction;
import org.hotrod.livesql.util.BoxUtil;
import org.springframework.stereotype.Component;

import app.persistence.resources.LiveSQLFunctions;

@Component
public class H2Functions {

  public NumericFunction random(NumericExpression x) {
    return Function.returnsNumeric(LiveSQLFunctions.random, x);
  }

  public NumericFunction random(Number x) {
    return Function.returnsNumeric(LiveSQLFunctions.random, BoxUtil.box(x));
  }

  // Add days to a date

  public DateTimeFunction addDays(DateTimeExpression date, NumericExpression days) {
    return Function.returnsDateTime(LiveSQLFunctions.adddays, days, date);
  }

  public DateTimeFunction addDays(LocalDate date, NumericExpression days) {
    return Function.returnsDateTime(LiveSQLFunctions.adddays, days, BoxUtil.box(date));
  }

  public DateTimeFunction addDays(DateTimeExpression date, Number days) {
    return Function.returnsDateTime(LiveSQLFunctions.adddays, BoxUtil.box(days), date);
  }

  public DateTimeFunction addDays(LocalDate date, Number days) {
    return Function.returnsDateTime(LiveSQLFunctions.adddays, BoxUtil.box(days), BoxUtil.box(date));
  }

}
