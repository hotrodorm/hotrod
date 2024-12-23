package app.daos.primitives;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.LayerConfigInterface;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.springframework.stereotype.Component;

@Component
public class LayerConfig implements LayerConfigInterface {

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();


    return rules;
  }

}
