package org.hotrod.torcs.ctp.autoconfig;

import org.hotrod.torcs.ctp.CTPPlanRetrieverFactory;
import org.hotrod.torcs.ctp.TorcsCTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorcsCTPAutoConfiguration {

  @Bean
  public CTPPlanRetrieverFactory getPlanRetrieverFactory() {
    return new CTPPlanRetrieverFactory();
  }

  @Bean
  public TorcsCTP getTorcsCTP() {
    return new TorcsCTP();
  }

}
