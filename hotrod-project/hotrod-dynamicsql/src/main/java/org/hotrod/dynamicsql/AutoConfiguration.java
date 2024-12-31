package org.hotrod.dynamicsql;

import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = QueryAssembler.class)
public class AutoConfiguration {

  @Bean
  public DynamicExpressionFactory getExpressionFactory() {
    return DynamicExpressionFactoryConfig.getFactory();
  }

}
