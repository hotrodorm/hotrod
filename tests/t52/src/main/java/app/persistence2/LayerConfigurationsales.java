package app.persistence2;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfiguration;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.stereotype.Component;

@Component
public class LayerConfigurationsales implements LayerConfiguration {

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();


    return rules;
  }

}
