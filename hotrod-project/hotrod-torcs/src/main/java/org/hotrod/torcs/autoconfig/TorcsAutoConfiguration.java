package org.hotrod.torcs.autoconfig;

import org.hotrod.torcs.Torcs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorcsAutoConfiguration {

  @Bean
  public Torcs torcs() {
    return new Torcs();
  }

}
