package org.hotrod.runtime.livesql.autoconfig;

import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnLiveSQLMissingCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    ConfigurableListableBeanFactory f = context.getBeanFactory();
    Map<String, LiveSQL> beans = f.getBeansOfType(LiveSQL.class);
    return beans.isEmpty();
  }

}
