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

    JavaIdentifier id = new JavaIdentifier(
        parameter.isDefinition() ? parameter.getName() : parameter.getDefinition().getName(), parameter.getJavaType());

    if (parameter.isInTag()) {

      // The parameter occurrence is used inside a MyBatis tag condition
      // (i.e. dynamic SQL)
      return id.getJavaMemberIdentifier();

    } else {

      // The parameter occurrence is a normal SQL parameter
      String jdbcType = parameter.isDefinition() ? parameter.getJdbcType() : parameter.getDefinition().getJdbcType();
      return "#{" + id.getJavaMemberIdentifier() + ",jdbcType=" + jdbcType + "}";

    }

  }

}
