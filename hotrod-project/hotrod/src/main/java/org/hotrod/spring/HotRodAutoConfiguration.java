package org.hotrod.spring;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.DynamicExpressionFactoryConfig;
import org.hotrod.dynamic.assembler.QueryAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = QueryAssembler.class)
public class HotRodAutoConfiguration {

  @Bean
  public SpringBeanObjectFactory getObjectFactory() {
    return new SpringBeanObjectFactory();
  }

  @Bean
  public DynamicExpressionFactory getExpressionFactory() {
    return DynamicExpressionFactoryConfig.getFactory();
  }

}
