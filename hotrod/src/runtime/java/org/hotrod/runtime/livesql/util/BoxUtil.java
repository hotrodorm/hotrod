package org.hotrod.runtime.livesql.util;

import org.hotrod.runtime.livesql.expressions.Constant;
import org.hotrod.runtime.livesql.expressions.Expression;

public class BoxUtil {

  // Utilities

  public static <T> Expression<T> boxTyped(final T value) {
    return new Constant<T>(value);
  }

}
