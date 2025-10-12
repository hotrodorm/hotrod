package app.persistence.resources;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeHandler;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LayerResourcesBean {

  @Bean
  public LayerConfiguration layerConfig() {
    List<TypeRule> rules = new ArrayList<>();
    rules.add(TypeRule.of("columnName == 'dom'", TypeHandler.forClass(Long.class, TypeSource.RUNTIME_TYPESOLVER_RULE, "RT1")));
    rules.add(TypeRule.of("columnName.endsWith('_jld')", TypeHandler.forClass(Float.class, TypeSource.RUNTIME_TYPESOLVER_RULE, "RT2")));
    return () -> rules;
  }

}
