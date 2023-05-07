package org.hotrod.runtime.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotRodAutoConfiguration {

  @Bean
  public SpringBeanObjectFactory getObjectFactory() {
    return new SpringBeanObjectFactory();
  }

}
