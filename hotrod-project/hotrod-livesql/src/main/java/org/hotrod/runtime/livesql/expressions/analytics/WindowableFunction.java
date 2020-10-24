package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public interface WindowableFunction {

//  default public WindowFunctionOverStage over() {
//    return new WindowFunctionOverStage(new WindowExpression(this));
//  }

  void renderTo(final QueryWriter w);

}
