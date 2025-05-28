package app.persistence1;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.LayerConfigInterface;
import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.stereotype.Component;

@Component
public class LayerConfiguration1 implements LayerConfigInterface {

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();

    return rules;
  }

}
