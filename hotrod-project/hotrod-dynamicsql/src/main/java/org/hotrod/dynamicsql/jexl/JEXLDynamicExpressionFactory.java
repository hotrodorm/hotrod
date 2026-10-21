package org.hotrod.dynamicsql.jexl;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.ObjectContext;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class JEXLDynamicExpressionFactory extends DynamicExpressionFactory {

  private static final int JEXL_CACHE_MAX_EXPRESSIONS = 2000;

  private final JexlEngine jexlEngine;

  public JEXLDynamicExpressionFactory(String[] allowedPackages) {
    String[] packagesWhitelist = new String[allowedPackages.length];
    for (int i = 0; i < allowedPackages.length; i++) {
      packagesWhitelist[i] = allowedPackages[i] + ".*";
    }
    this.jexlEngine = new JexlBuilder().cache(JEXL_CACHE_MAX_EXPRESSIONS) //
        .permissions(JexlPermissions.RESTRICTED.compose(packagesWhitelist)) //
        .strict(true).debug(true).silent(false).create();
  }

  public DynamicExpression expression(String expression) {
    return JEXLDynamicExpression.of(this.jexlEngine, expression);
  }

  public Parameters newParameterContext() {
    return new JEXLParameterContext();
  }

  @Override
  public Parameters newObjectContext(Object wrapped) {
    return JEXLObjectParameterContext.of(new ObjectContext<>(jexlEngine, wrapped));
  }

}
