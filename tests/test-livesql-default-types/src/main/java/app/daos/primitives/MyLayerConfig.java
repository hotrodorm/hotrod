package app.daos.primitives;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.LayerConfig;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.springframework.stereotype.Component;

@Component
public class MyLayerConfig implements LayerConfig {

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();

    rules.add(TypeRule.of("scale > 0", TypeHandler.of(BigDecimal.class)));

    return rules;
  }

}
