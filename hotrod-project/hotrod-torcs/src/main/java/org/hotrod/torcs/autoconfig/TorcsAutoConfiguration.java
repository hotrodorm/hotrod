package org.hotrod.torcs.autoconfig;

import org.hotrod.torcs.Torcs;
import org.hotrod.torcs.TorcsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorcsAutoConfiguration {

  @Bean
  public Torcs getTorcs() {
    System.out.println("getTorcs() -- 1");
    return new Torcs();
  }

  @Bean
  public TorcsAspect getTorcsAspect() {
    return new TorcsAspect();
  }

}
