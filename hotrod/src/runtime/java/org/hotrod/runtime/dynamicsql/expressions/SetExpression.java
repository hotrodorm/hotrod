package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetExpression extends TrimExpression {

  public SetExpression(final DynamicExpression... expressions) {
    super(null, null, null, ",", expressions);
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    params.addAll(Arrays.asList(super.expressions));
    return params;
  }

}
