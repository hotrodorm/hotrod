package org.hotrod.runtime.livesql.util;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.general.Constant;

public class BoxUtil {

  // Utilities

  public static <T> Expression<T> boxTyped(final T value) {
    return new Constant<T>(value);
  }

}
