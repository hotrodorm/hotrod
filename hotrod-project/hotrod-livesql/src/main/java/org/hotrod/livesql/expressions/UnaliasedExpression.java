package org.hotrod.livesql.expressions;

import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.binary.BinaryCast;
import org.hotrod.livesql.expressions.binary.BinaryExpression;
import org.hotrod.livesql.expressions.bool.BooleanCast;
import org.hotrod.livesql.expressions.character.CharCast;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeCast;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericCast;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.expressions.object.ObjectCast;
import org.hotrod.livesql.expressions.object.ObjectExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.utils.SUtil;

public abstract class UnaliasedExpression extends Expression {

  protected UnaliasedExpression(int precedence) {
    super(precedence);
  }

  // Aliasing

  public final AliasedExpression as(final String alias) {
    if (SUtil.isEmpty(alias)) {
      throw new LiveSQLException("An alias specified with the .as() method cannot be null");
    }
    return new AliasedExpression(this, alias);
  }

  // Cast

  public NumericExpression castNumeric(String sqlType) {
    return new NumericCast(this, sqlType);
  }

  public CharExpression castChar(String sqlType) {
    return new CharCast(this, sqlType);
  }

  public Predicate castBoolean(String sqlType) {
    return new BooleanCast(this, sqlType);
  }

  public DateTimeExpression castDateTime(String sqlType) {
    return new DateTimeCast(this, sqlType);
  }

  public BinaryExpression castBinary(String sqlType) {
    return new BinaryCast(this, sqlType);
  }

  public ObjectExpression castObject(String sqlType) {
    return new ObjectCast(this, sqlType);
  }

}
