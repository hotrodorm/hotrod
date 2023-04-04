package org.hotrod.generator.mybatisspring;

import org.hotrod.config.SQLParameter;
import org.hotrod.generator.ParameterRenderer;

public class MyBatisParameterRenderer implements ParameterRenderer {

  /**
   * <pre>
   *  Example:
   * 
   *  #{id,jdbcType=NUMERIC}
   * 
   * </pre>
   */
  @Override
  public String render(final SQLParameter parameter) {
    if (parameter.isInternal()) { // foreach, bind declare internal parameters
      return "#{" + parameter.getName() + "}";
    } else {
      return "#{" + parameter.getId().getJavaMemberName() + ",jdbcType=" + parameter.getJdbcType() + "}";
    }
  }

}
