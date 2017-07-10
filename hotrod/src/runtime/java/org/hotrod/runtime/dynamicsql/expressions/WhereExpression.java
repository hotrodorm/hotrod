package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhereExpression extends TrimExpression {

  public WhereExpression(final DynamicExpression... expressions) {
    super("where ", "and |or ", null, null, expressions);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    params.addAll(Arrays.asList(super.expressions));
    return params;
  }

}
