package org.hotrod.runtime.util;

import java.util.Map;

public interface SQLLogicalExpression {
  public String render();

  public Map<String, Object> getParameters();
}
