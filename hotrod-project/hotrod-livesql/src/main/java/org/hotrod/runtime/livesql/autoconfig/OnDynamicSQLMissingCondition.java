package org.hotrod.runtime.livesql.autoconfig;

import java.util.Map;

import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnDynamicSQLMissingCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    ConfigurableListableBeanFactory f = context.getBeanFactory();
    Map<String, DynamicSQL> beans = f.getBeansOfType(DynamicSQL.class);
    return beans.isEmpty();
  }

}
