package org.hotrod.generator.mybatis;

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
    return "#{" + parameter.getId().getJavaMemberName() + ",jdbcType=" + parameter.getJdbcType() + "}";
  }

}
