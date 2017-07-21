package org.hotrod.generator.mybatis;

import org.hotrod.config.SQLParameter;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.identifiers.JavaIdentifier;

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
    JavaIdentifier id = new JavaIdentifier(parameter.getName(), parameter.getJavaType());
    return "#{" + id.getJavaMemberIdentifier() + ",jdbcType=" + parameter.getJdbcType() + "}";
  }

}
