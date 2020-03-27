package org.hotrodorm.hotrod.utils;

import java.util.Map;

public interface SQLLogicalExpression {
  public String render();

  public Map<String, Object> getParameters();
}
