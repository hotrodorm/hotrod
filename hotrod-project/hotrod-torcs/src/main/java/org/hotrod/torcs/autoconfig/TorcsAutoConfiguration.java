package org.hotrod.torcs.autoconfig;

import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.TorcsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorcsAutoConfiguration {

  @Bean
  public Torcs getSQLMetrics() {
    return new Torcs();
  }

  @Bean
  public TorcsAspect getTorcsAspect() {
    return new TorcsAspect();
  }

}
