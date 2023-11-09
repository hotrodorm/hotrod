package org.hotrod.torcs.ctp.autoconfig;

import org.hotrod.torcs.ctp.PlanRetrieverFactory;
import org.hotrod.torcs.ctp.TorcsCTP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackageClasses = TorcsCTP.class)
public class TorcsCTPAutoConfiguration {

  @Bean
  public PlanRetrieverFactory getPlanRetrieverFactory() {
    return new PlanRetrieverFactory();
  }

  @Bean
  public TorcsCTP getTorcsCTP(final PlanRetrieverFactory planRetrieverFactory) {
    return new TorcsCTP(planRetrieverFactory);
  }

}
