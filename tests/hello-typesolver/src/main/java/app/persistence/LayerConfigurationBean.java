package app.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayerConfigurationBean {

  @Bean
  public LayerConfiguration layerConfig() {
    List<TypeRule> rules = new ArrayList<>();
    rules.add(TypeRule.of("columnName == 'dom'", TypeHandler.forClass(Long.class, TypeSource.RUNTIME_TYPESOLVER_RULE, "RT1")));
    return () -> rules;
  }

}
