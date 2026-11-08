package org.hotrod.livesql.expressions.character;

public abstract class ChShield {

  public static CharSQLInjection sqlInjection(final String value) {
    return new CharSQLInjection(value);
  }

}
