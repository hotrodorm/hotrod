package app.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("null")
public class LayerConfigurationBean {

  @Bean
  public LayerConfiguration layerConfig() {
    List<TypeRule> rules = new ArrayList<>();
    return () -> rules;
  }

}
