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

  public NumericExpression castNumeric(String type) {
    return new NumericCast(this, type);
  }

  public CharExpression castChar(String type) {
    return new CharCast(this, type);
  }

  public Predicate castBoolean(String type) {
    return new BooleanCast(this, type);
  }

  public DateTimeExpression castDateTime(String type) {
    return new DateTimeCast(this, type);
  }

  public BinaryExpression castBinary(String type) {
    return new BinaryCast(this, type);
  }

  public ObjectExpression castObject(String type) {
    return new ObjectCast(this, type);
  }

}
