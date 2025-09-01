package cfg;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.D;
import api.L;

@Configuration
public class LConfig {

  @Bean
  public D d2() { return new D(); }

  @Bean
  public D d3() { return new D(); }

  @Bean
  public L l2(@Qualifier("d2") D d) { L l = new L(d); return l; }

  @Bean
  public L l3(@Qualifier("d3") D d) { L l = new L(d); return l; }
  
}