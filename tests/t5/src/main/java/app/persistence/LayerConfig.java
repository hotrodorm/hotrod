package app.persistence;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.LayerConfigInterface;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler.TypeSource;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.springframework.stereotype.Component;

@Component
public class LayerConfig implements LayerConfigInterface {

  @Override
  public List<TypeRule> getTypeRules() {
    List<TypeRule> rules = new ArrayList<>();

    rules.add(TypeRule.of("precision < 2", TypeHandler.of(Byte.class, TypeSource.LIVESQL_RULES), 1));
    rules.add(TypeRule.of("scale > 0", TypeHandler.of(Short.class, TypeSource.LIVESQL_RULES), 2));

    return rules;
  }

}
