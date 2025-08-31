package app.persistence2;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("sales-config")
public class LayerBeanConfig {

  @Bean("sales")
  public LayerConfiguration bean() {
    List<TypeRule> rules = new ArrayList<>();
    return () -> rules;
  }

}
