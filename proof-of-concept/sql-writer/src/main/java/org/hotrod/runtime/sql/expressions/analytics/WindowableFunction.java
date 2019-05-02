package org.hotrod.runtime.sql.expressions.analytics;

import org.hotrod.runtime.sql.QueryWriter;

public interface WindowableFunction<T> {

  WindowFunctionOverStage<T> over();

  void renderBaseTo(final QueryWriter w);

}
