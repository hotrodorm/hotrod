package org.hotrod.torcs.autoconfig;

import org.hotrod.torcs.TorcsMetrics;
import org.hotrod.torcs.TorcsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorcsAutoConfiguration {

  @Bean
  public TorcsMetrics getSQLMetrics() {
    return new TorcsMetrics();
  }

  @Bean
  public TorcsAspect getTorcsAspect() {
    return new TorcsAspect();
  }

}
