package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.QueryWriter;

public interface WindowableFunction<T> {

  WindowFunctionOverStage<T> over();

  void renderBaseTo(final QueryWriter w);

}
